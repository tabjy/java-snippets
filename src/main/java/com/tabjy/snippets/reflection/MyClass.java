package com.tabjy.snippets.reflection;

public class MyClass {
    private static int thePrivate;
    
    public static class MyInnerClass {
        public static void printThePrivate() {
            System.out.println(thePrivate);
        }
    }
    
    public static void main(String[] args) {
        MyInnerClass.printThePrivate();
    }
}