package com.tabjy.snippets.asm.path_syntax_evaluation;

import java.lang.reflect.Field;

public class Target {
    private static int STATIC_FIELD = 42;
    private int dynamicField = 42;

    private static Pojo STATIC_OBJECT_FIELD = new Pojo();
    public Integer test;
    
    private boolean booleanZero;
    private byte byteZero;
    private char charZero;
    private short shotZero;
    private int intZero;
    private float floatZero;
    private long longZero;
    private double doubleZero;
    private Object[] arrayZero;
    private Object objectZero;
    
    public void test2() {
        int integer = 42;
        long longInt = 42;
        double doulbeNum = 42;
        
        if (intZero == 0){
            String msg = "foo";
            System.out.println(msg);
        }

        System.out.println("rest");
    }

    public void test(){
//        case Type.BOOLEAN:
//        case Type.BYTE:
//        case Type.CHAR:
//        case Type.SHORT:
//        case Type.INT:
//        case Type.FLOAT:
//        case Type.LONG:
//        case Type.DOUBLE:
//        case Type.ARRAY:
//        case Type.OBJECT:
//        case Type.INTERNAL:
//        case Type.METHOD:
//        case Type.VOID:
        
        booleanZero = false;
        byteZero = 0;
        charZero = 0;
        shotZero = 0;
        intZero = 0;
        floatZero = 0;
        longZero = 0;
        doubleZero = 0;
        arrayZero = null;
        objectZero =  null;
    }
    
    public void dynamicTargetFunction() {
        // no-op
//        test = 42;
//        System.out.println(dynamicField);
        Object event = new Object();
        
        Boolean b = Boolean.valueOf(null);
        if (b != null) {
            System.out.println("not null");
        }
        {
            int n = 123;
            System.out.println("new frame: "  + n);
        }
        System.out.println("continue");
//        System.out.println(event.toString());
        
        Integer integer = 2;
    }

    public static void staticTargetFunction() {
//        System.out.println(STATIC_FIELD);
        Object event = new Object();
        
        Boolean b = Boolean.valueOf(null);
        if (b != null) {
            System.out.println("not null");
        }
        System.out.println("continue");
//        System.out.println(event.toString());

        Integer integer = 2;
    }

    public static void staticSampleFunction() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
//        System.out.println(STATIC_FIELD);

        if (STATIC_OBJECT_FIELD != null) {
//            System.out.println(STATIC_OBJECT_FIELD.dynamicField);
        }

        Field f = STATIC_OBJECT_FIELD.getClass().getDeclaredField("dynamicField");
        f.isAccessible();
        int num = Pojo.theProtected;
//        Class<?> TARGETCLASS = STATIC_OBJECT_FIELD.getClass();
//        Object obj = TARGETCLASS.newInstance();
//        System.out.println(f.canAccess(obj));

//        
//        System.out.println(f.canAccess(new Object()));
//        f.canAccess()
//        Type type = f.getType();
//        System.out.println(type.getTypeName());
    }
}
