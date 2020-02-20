package com.tabjy.snippets.meta;

public class ImplicitDynamicReference {
    public int field = 42;
    
    public static void main(String[] args) throws InterruptedException {
        ImplicitDynamicReference app = new ImplicitDynamicReference();
        
        while (true) {
            app.foo();
            Thread.sleep(1000);
        }
    }
    
    public void foo() {
        new InnerClass().foo();
    }
    
    
    public class InnerClass {
        public void foo() {
            System.out.println(field);
        }
    }
}
