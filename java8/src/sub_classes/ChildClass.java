package sub_classes;

public class ChildClass extends BaseClass {
    public int prop = 0;

    @Override
    public void setProp(int i) {
        this.prop = i;
    }

    @Override
    public int getProp() {
        return this.prop;
    }

    public void setSuperProp(int i) {
        super.prop = i;
    }

    public int getSuperProp() {
        return super.prop;
    }
}
