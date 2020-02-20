package com.tabjy.snippets.asm.path_syntax_evaluation;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TargetDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V11, ACC_PUBLIC | ACC_SUPER, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", null, "java/lang/Object", null);

        classWriter.visitSource("Target.java", null);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_STATIC, "STATIC_FIELD", "I", null, null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE, "dynamicField", "I", null, null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_STATIC, "STATIC_OBJECT_FIELD", "Lcom/tabjy/snippets/asm/path_syntax_evaluation/Pojo;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(3, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(5, label1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitIntInsn(BIPUSH, 42);
            methodVisitor.visitFieldInsn(PUTFIELD, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "dynamicField", "I");
            methodVisitor.visitInsn(RETURN);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLocalVariable("this", "Lcom/tabjy/snippets/asm/path_syntax_evaluation/Target;", null, label0, label2, 0);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "dynamicTargetFunction", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(11, label0);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", "Lcom/tabjy/snippets/asm/path_syntax_evaluation/Target;", null, label0, label1, 0);
            methodVisitor.visitMaxs(0, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "staticTargetFunction", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(15, label0);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 0);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "staticSampleFunction", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(18, label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitFieldInsn(GETSTATIC, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "STATIC_FIELD", "I");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(20, label1);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "STATIC_OBJECT_FIELD", "Lcom/tabjy/snippets/asm/path_syntax_evaluation/Pojo;");
            Label label2 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label2);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(21, label3);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitFieldInsn(GETSTATIC, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "STATIC_OBJECT_FIELD", "Lcom/tabjy/snippets/asm/path_syntax_evaluation/Pojo;");
            methodVisitor.visitFieldInsn(GETFIELD, "com/tabjy/snippets/asm/path_syntax_evaluation/Pojo", "dynamicField", "I");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(23, label2);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(4, label0);
            methodVisitor.visitIntInsn(BIPUSH, 42);
            methodVisitor.visitFieldInsn(PUTSTATIC, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "STATIC_FIELD", "I");
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(7, label1);
            methodVisitor.visitTypeInsn(NEW, "com/tabjy/snippets/asm/path_syntax_evaluation/Pojo");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/tabjy/snippets/asm/path_syntax_evaluation/Pojo", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "com/tabjy/snippets/asm/path_syntax_evaluation/Target", "STATIC_OBJECT_FIELD", "Lcom/tabjy/snippets/asm/path_syntax_evaluation/Pojo;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}
