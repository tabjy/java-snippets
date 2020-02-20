package com.tabjy.snippets.asm.path_syntax_evaluation;

import java.lang.reflect.Method;

public class Executor {

    static class MyClassLoader extends ClassLoader {
        public Class defineClass(String clazz, byte[] bytes) {
            return defineClass(clazz, bytes, 0, bytes.length);
        }
    }

    public static void main(String[] args) throws Exception {
        MyClassLoader loader = new MyClassLoader();
        Class<?> clazz = loader.defineClass("com.tabjy.snippets.asm.path_syntax_evaluation.Target", TargetDump.dump());
        Method m = clazz.getDeclaredMethod("staticFunction", null);
        m.invoke(null, null) ;
    }
}
