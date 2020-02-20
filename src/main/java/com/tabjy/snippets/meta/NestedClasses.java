package com.tabjy.snippets.meta;

public class NestedClasses {
    public static void main(String[] args) {
        verifyNestmates(NestedClasses.class, InnerClass1.class);
        verifyNestmates(InnerClass1.class, InnerClass2.class);
        verifyNestmates(NestedClasses.class, InnerClass1.InnerInnerClass1.class);
        verifyNestmates(InnerClass1.InnerInnerClass1.class, InnerClass2.InnerInnerClass2.class);
    }
    
    private static void verifyNestmates(Class<?> lhs, Class<?> rhs) {
        if (lhs.isNestmateOf(rhs)) {
            System.out.printf("%s is a nestmate of %s\n", lhs.getName(), rhs.getName());
        } else {
            System.out.printf("%s is not a nestmate of %s\n", lhs.getName(), rhs.getName());
        }

        if (lhs.isNestmateOf(rhs) != areNestMates(lhs, rhs)) {
            System.out.println("^ error in implementation");
        }
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

    static void foo() {}
    
    public static class InnerClass1 {
        static void foo() {}
        
        public static class InnerInnerClass1 {
            static void foo() {}
        }
    }

    public static class InnerClass2 {
        static void foo() {}

        public static class InnerInnerClass2 {
            static void foo() {}
        }
    }
}
