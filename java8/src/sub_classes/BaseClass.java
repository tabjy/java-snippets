package sub_classes;

public class BaseClass {
    public int prop = 0;

    public void setProp(int i) {
        this.prop = i;
    }
    
    public int getProp() {
        return this.prop;
    }
    
    public static void main(String [] args) {
        ChildClass c1 = new ChildClass();
        ChildClass c2 = new ChildClass();
        
        System.out.println(c1.getSuperProp());
        System.out.println(c2.getSuperProp());

        c1.setSuperProp(42);

        System.out.println(c1.getSuperProp());
        System.out.println(c2.getSuperProp());
    }
}
