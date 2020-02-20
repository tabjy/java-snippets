package sub_classes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class OuterClass {
    public static void main(String[] args) throws NoSuchMethodException {
        Method method = OuterClass.InnerClass.class.getMethod("getId");
        System.out.println(Modifier.isStatic(method.getModifiers()));
    }

    public OuterClass() {
        super();
    }

    public OuterClass(InnerClass innerClass1, InnerClass innerClass2) {
        super();
        this.innerClass1 = innerClass1;
        this.innerClass2 = innerClass2;
    }

    private InnerClass innerClass1;
    private InnerClass innerClass2;

    public class InnerClass {
        public InnerClass() {
            super();
        }

        public InnerClass(int id, String name, String rollNo) {
            super();
            this.id = id;
            this.name = name;
            this.rollNo = rollNo;
        }

        private int id;
        private String name;
        private String rollNo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }
    }

    public InnerClass getInnerClass1() {
        return innerClass1;
    }

    public void setInnerClass1(InnerClass innerClass1) {
        this.innerClass1 = innerClass1;
    }

    public InnerClass getInnerClass2() {
        return innerClass2;
    }

    public void setInnerClass2(InnerClass innerClass2) {
        this.innerClass2 = innerClass2;
    }
}