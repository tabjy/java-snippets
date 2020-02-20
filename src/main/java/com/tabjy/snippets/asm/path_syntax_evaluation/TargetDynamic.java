package com.tabjy.snippets.asm.path_syntax_evaluation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

public class TargetDynamic {

    static class MyClassLoader extends ClassLoader {
        public Class defineClass(String clazz, byte[] bytes) {
            return defineClass(clazz, bytes, 0, bytes.length);
        }
    }

    static class MyMethodVisitor extends AdviceAdapter {

        private int numLocal;
        
        public MyMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM5, mv, access, name, desc);
        }

        @Override
        protected void onMethodEnter() {
            printVar();
        }
        
        private void printVar() {
            numLocal = newLocal(Type.INT_TYPE);
            
            visitFieldInsn(GETSTATIC, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "STATIC_FIELD", "I");
            visitVarInsn(ISTORE, numLocal);

            visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            visitVarInsn(ILOAD, numLocal);
            visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        }
    }

    static class MyClassVisitor extends ClassVisitor {

        public MyClassVisitor(ClassWriter cw) {
            super(Opcodes.ASM5, cw);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            return visitor;

//            if (!name.equals("staticTargetFunction")) {
//                return visitor;
//            }
//
//            return new MyMethodVisitor(visitor, access, name, descriptor);
        }
    }

    private static byte[] instrument(byte[] buffer) {
        ClassReader reader = new ClassReader(buffer);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new MyClassVisitor(writer);

        reader.accept(visitor, 0);

        return writer.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("/home/kxu/Documents/github.com/tabjy/java-snippets/target/classes/com/tabjy/snippets/asm/path_syntax_evaluation/Target.class");
        byte[] original = fis.readAllBytes();
//        byte[] original = TargetDump.dump();
        byte[] altered = instrument(original);

        {
            System.out.println("original code:");
            output(original,"tmp/original/Target.class");
            MyClassLoader loader = new MyClassLoader();
            Class<?> clazz = loader.defineClass("com.tabjy.snippets.asm.path_syntax_evaluation.Target", original);

            Method m = clazz.getDeclaredMethod("staticSampleFunction", null);

            try {
                m.invoke(null, null);
            } catch (Exception e) {
                System.out.println("Uncaught exception: ");
                e.printStackTrace();
            }
        }

        System.out.println();

        {
            System.out.println("altered code:");
            output(altered,"tmp/altered/Target.class");
            MyClassLoader loader = new MyClassLoader();
            Class<?> clazz = loader.defineClass("com.tabjy.snippets.asm.path_syntax_evaluation.Target", altered);

            Method m = clazz.getDeclaredMethod("staticSampleFunction", null);

            try {
                m.invoke(null, null);
            } catch (Exception e) {
                System.out.println("Uncaught exception: ");
                e.printStackTrace();
            }
        }
    }

    public static void output(byte[] bytes, String path) throws Exception {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(bytes);
    }
}
