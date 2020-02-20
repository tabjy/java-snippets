package com.tabjy.snippets.asm.path_syntax_evaluation;

public class Pojo {
    public static int STATIC_FIELD = 42;
    private int dynamicField = 42;
    
    public static int thePublic = 42;
    protected static int theProtected = 42;
    private static int thePrivate = 42;
    static int thePackagePrivate = 42;
    
    public static Pojo INSTANCE = null;
    
    public class NestPojo {
        
        
    }

    public static void main(String[] args) {
        System.out.println(NestPojo.class.getName());
    }
}
