package sub_classes;

public class GrandChildClass extends ChildClass {
    public static int grandChildProp;
    
    public int foo = 0;
    
    public void bar() {
//        foo();
//        sub_classes.GrandChildClass.super.foo();
//        ChildClass.super.foo();'
//        GrandChildClass.foo = 2;
    }
    
    public static void test() {
//        this.test();
    } 
}
