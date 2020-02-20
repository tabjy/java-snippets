package com.tabjy.snippets.reflection;

import com.tabjy.snippets.asm.path_syntax_evaluation.Pojo;
import com.tabjy.snippets.asm.path_syntax_evaluation.SubPojo;
import com.tabjy.snippets.asm.path_syntax_evaluation.Target;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class CheckAccess {
    static boolean isAccessible(Class<?> targetClass, String prop, Class<?> currentClass) throws NoSuchFieldException {
        Field field = targetClass.getDeclaredField(prop);

        if (field == null) {
            return false;
        }
        int modifiers = field.getModifiers();

        Class<?> memberClass = field.getDeclaringClass();
        if (Modifier.isStatic(modifiers)) {
            targetClass = null;
        }

        return verifyMemberAccess(currentClass, memberClass, targetClass, modifiers);
    }

    public static boolean verifyMemberAccess(Class<?> currentClass,
                                             Class<?> memberClass,
                                             Class<?> targetClass,
                                             int modifiers)
    {
        if (currentClass == memberClass) {
            // Always succeeds
            return true;
        }

        if (!verifyModuleAccess(currentClass.getModule(), memberClass)) {
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
                targetClass != currentClass)
        {
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

    public static boolean verifyModuleAccess(Module currentModule, Class<?> memberClass) {
        Module memberModule = memberClass.getModule();
        if (currentModule == memberModule) {
            // same module (named or unnamed) or both null if called
            // before module system is initialized, which means we are
            // dealing with java.base only.
            return true;
        } else {
            String pkg = memberClass.getPackageName();
            return memberModule.isExported(pkg, currentModule);
        }
    }

    private static boolean isSameClassPackage(Class<?> c1, Class<?> c2) {
        if (c1.getClassLoader() != c2.getClassLoader())
            return false;
        return Objects.equals(c1.getPackageName(), c2.getPackageName());
    }

    // non-native polyfill
    public static int getClassAccessFlags(Class<?> c) {
        return c.getModifiers(); // See Reflection.getClassAccessFlags(Class<?> c)
    }

    // non-native polyfill
    public static boolean areNestMates(Class<?> currentClass, Class<?> memberClass) {
        return currentClass.isNestmateOf(memberClass);
    }

    public static void main(String[] args) throws NoSuchFieldException {
        // same class
        assertAccessible(Pojo.class, "thePublic", Pojo.class);
        assertAccessible(Pojo.class, "theProtected", Pojo.class);
        assertAccessible(Pojo.class, "thePrivate", Pojo.class);
        assertAccessible(Pojo.class, "thePackagePrivate", Pojo.class);
        
        // Nestmate class
        // XXX: nestmate access must be polyfill with bridge methods before java 11
        assertAccessible(Pojo.class, "thePublic", Pojo.NestPojo.class);
        assertAccessible(Pojo.class, "theProtected", Pojo.NestPojo.class);
        assertAccessible(Pojo.class, "thePrivate", Pojo.NestPojo.class);
        assertAccessible(Pojo.class, "thePackagePrivate", Pojo.NestPojo.class);
        
        // Same package subclass
        assertAccessible(Pojo.class, "thePublic", SubPojo.class);
        assertAccessible(Pojo.class, "theProtected", SubPojo.class);
        assertNotAccessible(Pojo.class, "thePrivate", SubPojo.class);
        assertAccessible(Pojo.class, "thePackagePrivate", SubPojo.class);
        
        // Same package non-subclass
        assertAccessible(Pojo.class, "thePublic", Target.class);
        assertAccessible(Pojo.class, "theProtected", Target.class);
        assertNotAccessible(Pojo.class, "thePrivate", Target.class);
        assertAccessible(Pojo.class, "thePackagePrivate", Target.class);
        
        // Different package subclass
        assertAccessible(Pojo.class, "thePublic", MyPojo.class);
        assertAccessible(Pojo.class, "theProtected", MyPojo.class);
        assertNotAccessible(Pojo.class, "thePrivate", MyPojo.class);
        assertNotAccessible(Pojo.class, "thePackagePrivate", MyPojo.class);

        // Different package non-subclass
        assertAccessible(Pojo.class, "thePublic", CheckAccess.class);
        assertNotAccessible(Pojo.class, "theProtected", CheckAccess.class);
        assertNotAccessible(Pojo.class, "thePrivate", CheckAccess.class);
        assertNotAccessible(Pojo.class, "thePackagePrivate", CheckAccess.class);
    }
    
    public static void assertAccessible(Class<?> to, String field, Class<?> from) throws NoSuchFieldException {
        if (!isAccessible(to, field, from)) {
            throw new RuntimeException(String.format("%s.%s should be accessible from %s", Pojo.class.getName(), field, from.getName()));
        }

        System.out.printf("%s.%s is accessible from %s\n", to.getName(), field, from.getName());
    }

    public static void assertNotAccessible(Class<?> to, String field, Class<?> from) throws NoSuchFieldException {
        if (isAccessible(to, field, from)) {
            throw new RuntimeException(String.format("%s.%s should be accessible from %s", Pojo.class.getName(), field, from.getName()));
        }

        System.out.printf("%s.%s is not accessible from %s\n", to.getName(), field, from.getName());
    }

    static boolean isSubclassOf(Class<?> queryClass,
                                Class<?> ofClass)
    {
        while (queryClass != null) {
            if (queryClass == ofClass) {
                return true;
            }
            queryClass = queryClass.getSuperclass();
        }
        return false;
    }
    
    class MyPojo extends Pojo {}
}
