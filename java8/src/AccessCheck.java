import the_package.MyObject;
import the_package.OtherObject;
import the_package.SomeOtherImplementation;
import the_package.SubMyObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class AccessCheck {
    public static boolean isAccessible(Class<?> targetClass, String prop, Class<?> currentClass) throws NoSuchFieldException {
        Field field = getAnyFieldOf(targetClass, prop);
        int modifiers = field.getModifiers();

        Class<?> memberClass = field.getDeclaringClass();
        if (Modifier.isStatic(modifiers)) {
            targetClass = null;
        }

        if (currentClass == memberClass) {
            return true;
        }

        if (!verifyModuleAccess(memberClass, currentClass)) {
            return false;
        }

        boolean gotIsSameClassPackage = false;
        boolean isSameClassPackage = false;

        if (!Modifier.isPublic(getClassAccessFlags(memberClass))) {
            isSameClassPackage = isSameClassPackage(currentClass, memberClass);
            gotIsSameClassPackage = true;
            if (!isSameClassPackage) {
                return false;
            }
        }

        // At this point we know that currentClass can access memberClass.

        if (Modifier.isPublic(modifiers)) {
            return true;
        }

        // Check for nestmate access if member is private
        if (Modifier.isPrivate(modifiers)) {
            // Note: targetClass may be outside the nest, but that is okay
            //       as long as memberClass is in the nest.
            if (areNestMates(currentClass, memberClass)) {
                return true;
            }
        }

        boolean successSoFar = false;

        if (Modifier.isProtected(modifiers)) {
            // See if currentClass is a subclass of memberClass
            if (isSubclassOf(currentClass, memberClass)) {
                successSoFar = true;
            }
        }

        if (!successSoFar && !Modifier.isPrivate(modifiers)) {
            if (!gotIsSameClassPackage) {
                isSameClassPackage = isSameClassPackage(currentClass,
                        memberClass);
                gotIsSameClassPackage = true;
            }

            if (isSameClassPackage) {
                successSoFar = true;
            }
        }

        if (!successSoFar) {
            return false;
        }

        // Additional test for protected instance members
        // and protected constructors: JLS 6.6.2
        if (targetClass != null && Modifier.isProtected(modifiers) &&
                targetClass != currentClass) {
            if (!gotIsSameClassPackage) {
                isSameClassPackage = isSameClassPackage(currentClass, memberClass);
                gotIsSameClassPackage = true;
            }
            if (!isSameClassPackage) {
                if (!isSubclassOf(targetClass, currentClass)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean verifyModuleAccess(Class<?> targetClass, Class<?> callerClass) {
        String version = System.getProperty("java.version");
        if (Integer.parseInt(version.substring(0, version.indexOf("."))) < 9) {
            return true; // There is no module for pre-java 9
        }
        
        Object targetModule;
        Object callerModule;
        try {
            Method getModuleMethod = Class.class.getDeclaredMethod("getModule");
            targetModule = getModuleMethod.invoke(targetClass);
            callerModule = getModuleMethod.invoke(callerClass);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e); // this should not happen
        }
        
        if (targetModule == callerModule) {
            return true;
        }
        
        String pkg = getPackageName(targetClass);
        try {
            Method isExportedMethod = targetModule.getClass().getDeclaredMethod("isExported", String.class, Class.forName("java.lang.Module"));
            return (boolean) isExportedMethod.invoke(targetModule, pkg, callerModule);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e); // this should not happen
        }
    }

    public static Field getAnyFieldOf(Class<?> clazz, String name) throws NoSuchFieldException {
        Queue<Class<?>> q = new LinkedList<>();
        q.add(clazz);

        while (!q.isEmpty()) {
            Class<?> targetClass = q.remove();
            try {
                return targetClass.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // ignore
            }

            q.addAll(Arrays.asList(targetClass.getInterfaces()));
            Class<?> superClass = targetClass.getSuperclass();
            if (superClass != null) {
                q.add(targetClass.getSuperclass());
            }
        }
        
        throw new NoSuchFieldException(name);
    }

    private static String getPackageName(Class<?> clazz) {
        // TODO: verify same behaviour as Class.getPackageName()
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        if (clazz.isPrimitive()) {
            return "java.lang";
        }

        String cn = clazz.getName();
        int dot = cn.lastIndexOf('.');
        return (dot != -1) ? cn.substring(0, dot).intern() : "";
    }

    private static int getClassAccessFlags(Class<?> c) {
        return c.getModifiers(); // See Reflection.getClassAccessFlags(Class<?> c)
    }

    private static boolean isSameClassPackage(Class<?> lhs, Class<?> rhs) {
        if (lhs.getClassLoader() != rhs.getClassLoader())
            return false;
        return Objects.equals(getPackageName(lhs), getPackageName(rhs));
    }

    private static boolean areNestMates(Class<?> currentClass, Class<?> memberClass) {
        // TODO: verify same behaviour as Class.isNestmateOf
        return currentClass.getName().startsWith(memberClass.getName() + "$") ||
                memberClass.getName().startsWith(currentClass.getName() + "$");
    }

    private static boolean isSubclassOf(Class<?> queryClass,
                                        Class<?> ofClass) {
        while (queryClass != null) {
            if (queryClass == ofClass) {
                return true;
            }
            queryClass = queryClass.getSuperclass();
        }
        return false;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        // same class
        assertAccessible(MyObject.class, "thePublic", MyObject.class);
        assertAccessible(MyObject.class, "theProtected", MyObject.class);
        assertAccessible(MyObject.class, "thePrivate", MyObject.class);
        assertAccessible(MyObject.class, "thePackagePrivate", MyObject.class);

        // Nestmate class
        // XXX: nestmate access must be polyfill with bridge methods before java 11
        assertAccessible(MyObject.class, "thePublic", MyObject.MyInnerObject.class);
        assertAccessible(MyObject.class, "theProtected", MyObject.MyInnerObject.class);
        assertAccessible(MyObject.class, "thePrivate", MyObject.MyInnerObject.class);
        assertAccessible(MyObject.class, "thePackagePrivate", MyObject.MyInnerObject.class);

        // Same package subclass
        assertAccessible(MyObject.class, "thePublic", SubMyObject.class);
        assertAccessible(MyObject.class, "theProtected", SubMyObject.class);
        assertNotAccessible(MyObject.class, "thePrivate", SubMyObject.class);
        assertAccessible(MyObject.class, "thePackagePrivate", SubMyObject.class);

        // Same package non-subclass
        assertAccessible(MyObject.class, "thePublic", OtherObject.class);
        assertAccessible(MyObject.class, "theProtected", OtherObject.class);
        assertNotAccessible(MyObject.class, "thePrivate", OtherObject.class);
        assertAccessible(MyObject.class, "thePackagePrivate", OtherObject.class);

        // Different package subclass
        assertAccessible(MyObject.class, "thePublic", OtherSubMyObject.class);
        assertAccessible(MyObject.class, "theProtected", OtherSubMyObject.class);
        assertNotAccessible(MyObject.class, "thePrivate", OtherSubMyObject.class);
        assertNotAccessible(MyObject.class, "thePackagePrivate", OtherSubMyObject.class);

        // Different package non-subclass
        assertAccessible(MyObject.class, "thePublic", AccessCheck.class);
        assertNotAccessible(MyObject.class, "theProtected", AccessCheck.class);
        assertNotAccessible(MyObject.class, "thePrivate", AccessCheck.class);
        assertNotAccessible(MyObject.class, "thePackagePrivate", AccessCheck.class);
        
        // Different module non-subclass
        assertAccessible(MyObject.class, "thePublic", String.class);
        assertNotAccessible(MyObject.class, "theProtected", String.class);
        assertNotAccessible(MyObject.class, "thePrivate", String.class);
        assertNotAccessible(MyObject.class, "thePackagePrivate", String.class);
        
        // Same as above but accessing a child class
        // same class
        assertAccessible(MyObject.class, "thePublic", MyObject.class);
        assertAccessible(MyObject.class, "theProtected", MyObject.class);
        assertAccessible(MyObject.class, "thePrivate", MyObject.class);
        assertAccessible(MyObject.class, "thePackagePrivate", MyObject.class);

        // Nestmate class
        // XXX: nestmate access must be polyfill with bridge methods before java 11
        assertAccessible(SubMyObject.class, "thePublic", MyObject.MyInnerObject.class);
        assertAccessible(SubMyObject.class, "theProtected", MyObject.MyInnerObject.class);
        assertAccessible(SubMyObject.class, "thePrivate", MyObject.MyInnerObject.class);
        assertAccessible(SubMyObject.class, "thePackagePrivate", MyObject.MyInnerObject.class);

        // Same package subclass
        assertAccessible(SubMyObject.class, "thePublic", SubMyObject.class);
        assertAccessible(SubMyObject.class, "theProtected", SubMyObject.class);
        assertNotAccessible(SubMyObject.class, "thePrivate", SubMyObject.class);
        assertAccessible(SubMyObject.class, "thePackagePrivate", SubMyObject.class);

        // Same package non-subclass
        assertAccessible(SubMyObject.class, "thePublic", OtherObject.class);
        assertAccessible(SubMyObject.class, "theProtected", OtherObject.class);
        assertNotAccessible(SubMyObject.class, "thePrivate", OtherObject.class);
        assertAccessible(SubMyObject.class, "thePackagePrivate", OtherObject.class);

        // Different package subclass
        assertAccessible(SubMyObject.class, "thePublic", OtherSubMyObject.class);
        assertAccessible(SubMyObject.class, "theProtected", OtherSubMyObject.class);
        assertNotAccessible(SubMyObject.class, "thePrivate", OtherSubMyObject.class);
        assertNotAccessible(SubMyObject.class, "thePackagePrivate", OtherSubMyObject.class);

        // Different package non-subclass
        assertAccessible(SubMyObject.class, "thePublic", AccessCheck.class);
        assertNotAccessible(SubMyObject.class, "theProtected", AccessCheck.class);
        assertNotAccessible(SubMyObject.class, "thePrivate", AccessCheck.class);
        assertNotAccessible(SubMyObject.class, "thePackagePrivate", AccessCheck.class);

        // Different module non-subclass
        assertAccessible(SubMyObject.class, "thePublic", String.class);
        assertNotAccessible(SubMyObject.class, "theProtected", String.class);
        assertNotAccessible(SubMyObject.class, "thePrivate", String.class);
        assertNotAccessible(SubMyObject.class, "thePackagePrivate", String.class);

        // Same as above but accessing a implementation of a interface
        assertAccessible(SomeOtherImplementation.class, "thePublic1", SomeOtherImplementation.class);
        assertAccessible(SomeOtherImplementation.class, "thePublic2", SomeOtherImplementation.class);
    }

    public static void assertAccessible(Class<?> to, String field, Class<?> from) throws NoSuchFieldException {
        if (!isAccessible(to, field, from)) {
            throw new RuntimeException(String.format("%s.%s should be accessible from %s", to.getName(), field, from.getName()));
        }

        System.out.printf("%s.%s is accessible from %s\n", to.getName(), field, from.getName());
    }

    public static void assertNotAccessible(Class<?> to, String field, Class<?> from) throws NoSuchFieldException {
        if (isAccessible(to, field, from)) {
            throw new RuntimeException(String.format("%s.%s should be accessible from %s", to.getName(), field, from.getName()));
        }

        System.out.printf("%s.%s is not accessible from %s\n", to.getName(), field, from.getName());
    }
    
    class MyMyObjection extends MyObject{}
}
