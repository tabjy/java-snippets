package com.tabjy.snippets.asm.rethrow;

import org.objectweb.asm.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

public class HelloWorldDynamic {

    static class MyClassLoader extends ClassLoader {
        public Class defineClass(String clazz, byte[] bytes) {
            return defineClass(clazz, bytes, 0, bytes.length);
        }
    }

    static class MyMethodVisitor extends MethodVisitor {

        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();

        public MyMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            visitLabel(label0);
        }

        @Override
        public void visitEnd() {
            visitLabel(label1);
            visitLabel(label2);

            visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");

            visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
            visitVarInsn(ASTORE, 0);
            visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            visitVarInsn(ALOAD, 0);
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;", false);
            visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"caught by asm : \u0001"});
            visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            // rethrow
            visitVarInsn(ALOAD, 0);
            visitInsn(ATHROW);

            visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            visitInsn(RETURN);
            super.visitEnd();
        }
    }

    static class MyClassVisitor extends ClassVisitor {

        public MyClassVisitor(ClassWriter cw) {
            super(Opcodes.ASM5, cw);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!name.equals("dangerousPrint")) {
                return visitor;
            }

            return new MyMethodVisitor(visitor);
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
        FileInputStream fis = new FileInputStream("/home/kxu/Documents/github.com/tabjy/compiler-snippets/out/production/compiler-snippets/com/tabjy/snippets/asm/HelloWorld.class");
        byte[] original = fis.readAllBytes();
        byte[] altered = instrument(original);

        {
            System.out.println("original code:");
            output(original,"tmp/original/HelloWorld.class");
            MyClassLoader loader = new MyClassLoader();
            Class<?> clazz = loader.defineClass("com.tabjy.snippets.asm.rethrow.HelloWorld", original);

            Method m = clazz.getDeclaredMethod("dangerousPrint", null);

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
            output(altered,"tmp/altered/HelloWorld.class");
            MyClassLoader loader = new MyClassLoader();
            Class<?> clazz = loader.defineClass("com.tabjy.snippets.asm.rethrow.HelloWorld", altered);

            Method m = clazz.getDeclaredMethod("dangerousPrint", null);

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
