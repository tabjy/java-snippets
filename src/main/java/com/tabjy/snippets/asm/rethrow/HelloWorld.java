package com.tabjy.snippets.asm.rethrow;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        dangerousPrint();
    }

    public static void dangerousPrint() throws Exception {
        // to be made safe with bytecode manipulation
        try {
            print();
        } catch (Exception e) {
            System.out.println("caught by inner try");
            throw e;
        }
    }

    public static void dangerousPrint2() throws Exception {
        // to be made safe with bytecode manipulation
        try {
            try {
                print();
            } catch (Exception e) {
                System.out.println("caught by inner try");
            }
        } catch (Exception e) {
            System.err.println("caught by outer try");
        }
    }

    public static void safePrint() throws Exception {
        try {
            print();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public static void print() throws Exception {
        doPrint();
        throw new Exception("oops!");
    }

    private static void doPrint() {
        System.out.println("Hello World!");
    }
}
