public class Example {
    static private int theStaticPrivate;
    private int theDynamicPrivate;
    
    public static class InnerClass {
        public static int accessTheStaticPrivate() {
            return theStaticPrivate;
        }

//        public int acessTheDyanmicPrivate() {
//            return theDynamicPrivate;
//        }
    }
    
    public static void main(String[] args) {
        InnerClass.accessTheStaticPrivate();
    }
}
