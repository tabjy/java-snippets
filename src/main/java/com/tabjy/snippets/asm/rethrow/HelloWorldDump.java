package com.tabjy.snippets.asm.rethrow;

import org.objectweb.asm.*;
public class HelloWorldDump implements Opcodes {

    public static byte[] dump () throws Exception {

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;

        classWriter.visit(V12, ACC_PUBLIC | ACC_SUPER, "com/tabjy/snippets/asm/HelloWorld", null, "java/lang/Object", null);

        classWriter.visitSource("HelloWorld.compiler", null);

        classWriter.visitInnerClass("java/lang/invoke/MethodHandles$Lookup", "java/lang/invoke/MethodHandles", "Lookup", ACC_PUBLIC | ACC_FINAL | ACC_STATIC);

        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(3, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", "Lcom/tabjy/snippets/asm/HelloWorld;", null, label0, label1, 0);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[] { "java/lang/Exception" });
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(6, label0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/tabjy/snippets/asm/HelloWorld", "dangerousPrint", "()V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(7, label1);
            methodVisitor.visitInsn(RETURN);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLocalVariable("args", "[Ljava/lang/String;", null, label0, label2, 0);
            methodVisitor.visitMaxs(0, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "dangerousPrint", "()V", null, new String[] { "java/lang/Exception" });
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(12, label0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/tabjy/snippets/asm/HelloWorld", "print", "()V", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(15, label1);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(13, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
            methodVisitor.visitVarInsn(ASTORE, 0);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(14, label4);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("caught by inner try");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(16, label3);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label4, label3, 0);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "dangerousPrint2", "()V", null, new String[] { "java/lang/Exception" });
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            Label label3 = new Label();
            Label label4 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label3, label4, "java/lang/Exception");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(22, label0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/tabjy/snippets/asm/HelloWorld", "print", "()V", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(25, label1);
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(23, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
            methodVisitor.visitVarInsn(ASTORE, 0);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(24, label5);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("caught by inner try");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(28, label3);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            Label label6 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label6);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(26, label4);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
            methodVisitor.visitVarInsn(ASTORE, 0);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLineNumber(27, label7);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("caught by outer try");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(29, label6);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label5, label3, 0);
            methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label7, label6, 0);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "safePrint", "()V", null, new String[] { "java/lang/Exception" });
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(33, label0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/tabjy/snippets/asm/HelloWorld", "print", "()V", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(37, label1);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(34, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Exception"});
            methodVisitor.visitVarInsn(ASTORE, 0);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(35, label4);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;", false);
            methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"Error: \u0001"});
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(36, label5);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(38, label3);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label4, label3, 0);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "print", "()V", null, new String[] { "java/lang/Exception" });
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(41, label0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/tabjy/snippets/asm/HelloWorld", "doPrint", "()V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(42, label1);
            methodVisitor.visitTypeInsn(NEW, "java/lang/Exception");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitLdcInsn("oops!");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Exception", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitMaxs(3, 0);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC, "doPrint", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(46, label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitLdcInsn("Hello World!");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(47, label1);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}
