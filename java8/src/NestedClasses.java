import org.junit.jupiter.api.Nested;

public class NestedClasses {
    int prop = 123; 
    
    public static void main(String[] args) {
//        verifyNestmates(NestedClasses.class, InnerClass1.class);
//        verifyNestmates(InnerClass1.class, InnerClass2.class);
//        verifyNestmates(NestedClasses.class, InnerClass1.InnerInnerClass1.class);
//        verifyNestmates(InnerClass1.InnerInnerClass1.class, InnerClass2.InnerInnerClass2.class);
        NestedClasses instance = new NestedClasses();
        instance.bar();
        
    }
    
    
    
    private void bar() {
        new InnerClass1().foo();
    }
    
    private static void verifyNestmates(Class<?> lhs, Class<?> rhs) {
//        if (lhs.isNestmateOf(rhs)) {
//            System.out.printf("%s is a nestmate of %s\n", lhs.getName(), rhs.getName());
//        } else {
//            System.out.printf("%s is not a nestmate of %s\n", lhs.getName(), rhs.getName());
//        }
//
//        if (lhs.isNestmateOf(rhs) != areNestMates(lhs, rhs)) {
//            System.out.println("^ error in implementation");
//        }
    }

    private static boolean areNestMates(Class<?> lhs, Class<?> rhs) {
        String lhsBaseName = lhs.getName();
        if (lhsBaseName.indexOf("$") > 0) {
            lhsBaseName = lhsBaseName.substring(0, lhsBaseName.indexOf("$"));
        }
        String rhsBaseName = rhs.getName();
        if (rhsBaseName.indexOf("$") > 0) {
            rhsBaseName = rhsBaseName.substring(0, rhsBaseName.indexOf("$"));
        }
        
        return lhsBaseName.equals(rhsBaseName);
    }

    void foo() {}
    
    String foobar = "foobar";

    @Override
    public String toString() {
        return "wtf";
    }
    
    public class BaseClass {
        public int propty = 123;
        @Override
        public String toString() {
            return "base";
        }
    }

    public class InnerClass1 extends BaseClass {
        public int property = 0;
        
        @Override
        public String toString() {
            return "wtf2";
        }

        void foo() {
            new InnerInnerClass1().foo();
        }

        public class InnerInnerClass1 {
            void foo() {
//                NestedClasses.super.toString();
//                System.out.println(super.foobar);
                System.out.println(InnerClass1.super.propty);
//                NestedClasses.super.toString();
            }
        }
    }

    public static class InnerClass2 {
        static void foo() {
        }

        public static class InnerInnerClass2 {
            static void foo() {}
        }
    }
}
