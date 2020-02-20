import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SyntaxValidation {
    
    private Boolean FALSE;

    public static void main(String[] args) {
        ExpressionStateMachine machine = new ExpressionStateMachine(SyntaxValidation.class, "ExpressionStateMachine.TestClass.caller");
        List<FieldReference> refs = machine.solve();
        System.out.println(refs);
    }

    public static class ExpressionStateMachine {
        public static class TestClass {
            public static int caller;
        }
        
        private final Class<?> caller;
        private final String expression;

//        private String packageName;
//        private String className;

        private Class<?> memberingClass;
        private Queue<String> tokens;
//        private State nextState;

        private List<FieldReference> referenceChain;

        private enum State {
            START_STATE,
            PACKAGE_NAME_STATE,
            CLASS_NAME_STATE,
            FIELD_NAME_STATE,
            NESTED_CLASS_NAME_STATE,
            THIS_LITERAL_STATE,
            CLASS_LITERAL_STATE,
            REJECTED_STATE
        }

        public ExpressionStateMachine(Class<?> caller, String expression) {
            this.caller = caller;
            this.expression = expression;
        }

        public List<FieldReference> solve() {
            tokens = new LinkedList<>(Arrays.asList(expression.split("\\.")));
            referenceChain = new LinkedList<>();

            startState();
//            nextState = State.START_STATE;
//            while (!tokens.isEmpty() && nextState != State.REJECTED_STATE) {
//                switch (nextState) {
//                    case START_STATE:
//                        startState();
//                        break;
//                    case PACKAGE_NAME_STATE:
//                        packageNameState();
//                        break;
//                    case CLASS_NAME_STATE:
//                        classNameState();
//                        break;
//                    case FIELD_NAME_STATE:
//                        fieldNameState();
//                        break;
//                    case NESTED_CLASS_NAME_STATE:
//                        nestedClassNameState();
//                        break;
//                    case THIS_LITERAL_STATE:
//                        thisLiteralState();
//                        break;
//                    case CLASS_LITERAL_STATE:
//                        classLiteralState();
//                        break;
//                    case REJECTED_STATE:
//                        rejectedState();
//                        break;
//                }
//            }

            return referenceChain;
        }

        private void startState() {
            memberingClass = caller;

            String token = tokens.poll();
            if (token == null) {
                rejectedState();
            }

            if ("this".equals(token)) {
                thisLiteralState(caller);
                return;
            }

            {
                Field field = null;
                try {
                    field = AccessUtils.getFieldOnHierarchy(memberingClass, token);
                } catch (NoSuchFieldException e) {
                    // no op
                }
                if (field != null) {
                    fieldNameState(field);
                    return;
                }
            }

            {
                Class<?> nestedClass = null;
                for (Class<?> clazz : caller.getDeclaredClasses()) {
                    if (clazz.getSimpleName().equals(token)) {
                        nestedClass = clazz;
                        break;
                    }
                }
                if (nestedClass != null) {
                    classNameState(nestedClass);
                    return;
                }
            }

            {
                String className = caller.getPackage().getName() + "." + token;
                if (caller.getPackage().getName().isEmpty()) {
                    className = token;
                }
                
                try {
                    Class<?> clazz = caller.getClassLoader().loadClass(className);
                    classNameState(clazz);
                    return;
                } catch (ClassNotFoundException e) {
                    // no op
                }
            }

            {
                String className = "java.lang." + token;
                try {
                    Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
                    classNameState(clazz);
                    return;
                } catch (ClassNotFoundException e) {
                    // no op
                }
            }

            {
                // TODO: use class loader
                for (Package pkg : Package.getPackages()) {
                    if (pkg.getName().startsWith(token)) {
                        packageNameState(token);
                        return;
                    }
                }
            }

            rejectedState();
        }

        private void packageNameState(String partialPackageName) {
            String token = tokens.poll();
            if (token == null) {
                rejectedState();
                return;
            }

            // TODO: use class loader
            if (Package.getPackage(partialPackageName) != null) {
                try {
                    Class<?> clazz = caller.getClassLoader().loadClass(partialPackageName + "." + token);
                    classNameState(clazz);
                    return;
                } catch (ClassNotFoundException e) {
                    // no op
                }
            }

            partialPackageName = partialPackageName + "." + token;
            for (Package pkg : Package.getPackages()) {
                if (pkg.getName().startsWith(partialPackageName)) {
                    packageNameState(partialPackageName);
                    return;
                }
            }

            rejectedState();
        }

        private void classNameState(Class<?> clazz) {
            memberingClass = clazz;

            String token = tokens.poll();
            if (token == null) {
                rejectedState();
                return;
            }

            if ("this".equals(token)) {
                thisLiteralState(clazz);
                return;
            }

            if ("class".equals(token)) {
                classLiteralState();
                return;
            }

            {
                for (Class<?> c : memberingClass.getDeclaredClasses()) {
                    if (c.getSimpleName().equals(token)) {
                        classNameState(c);
                        return;
                    }
                }
            }

            try {
                Field field = AccessUtils.getFieldOnHierarchy(memberingClass, token);
                fieldNameState(field);
            } catch (NoSuchFieldException e) {
                rejectedState();
            }
        }

        private void fieldNameState(Field field) {
            FieldReference ref = new FieldReference(memberingClass, field);
            referenceChain.add(ref);
            memberingClass = field.getType();

            String token = tokens.poll();
            if (token == null) {
                return;
            }

            try {
                field = AccessUtils.getFieldOnHierarchy(memberingClass, token);
                fieldNameState(field);
            } catch (NoSuchFieldException e) {
                rejectedState();
            }
        }

        void thisLiteralState(Class<?> target) {
            if (target == null) {
                throw new IllegalArgumentException();
            }

            if (caller.equals(target)) {
                referenceChain.add(new FieldReference.ThisReference(caller));
            } else {
                Class<?> clazz = caller;
                while (!target.equals(clazz)) {
                    if (clazz == null) {
                        rejectedState(); // cannot cast
                        return;
                    }

                    referenceChain.add(new FieldReference.OutwardsCastingReference(clazz, clazz.getEnclosingClass()));
                    clazz = clazz.getEnclosingClass();
                }
            }

            memberingClass = target;

            String token = tokens.poll();
            if (token == null) {
                return;
            }

            try {
                Field field = AccessUtils.getFieldOnHierarchy(memberingClass, token);
                fieldNameState(field);
            } catch (NoSuchFieldException e) {
                rejectedState();
            }
        }

        void classLiteralState() {
            throw new UnsupportedOperationException();
        }

        void rejectedState() throws IllegalArgumentException {
            throw new IllegalArgumentException();
        }
    }
}
