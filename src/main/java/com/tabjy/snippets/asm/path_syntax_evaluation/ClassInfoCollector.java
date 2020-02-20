package com.tabjy.snippets.asm.path_syntax_evaluation;

import org.objectweb.asm.Type;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ClassInfoCollector extends ClassVisitor {

    public static void main(String[] args) throws IOException {
        ClassLoader cl = ClassInfoCollector.class.getClassLoader();
        InputStream classInfoCollectorBytes = cl.getResourceAsStream("com/tabjy/snippets/asm/path_syntax_evaluation/ClassInfoCollector.class");

        ClassReader cr = new ClassReader(classInfoCollectorBytes);
        ClassInfoCollector collector = new ClassInfoCollector(Opcodes.ASM7);
        cr.accept(collector, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        System.out.println(collector);
    }

    private int access = -1;
    private String name = null;
    private String superName = null;
    private String[] interfaceNames = null;

    private Map<String, Field> fields = new HashMap<>();

    public ClassInfoCollector(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, java.lang.String name, java.lang.String signature, java.lang.String superName, java.lang.String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.access = access;
        this.name = name;
        this.superName = superName;
        this.interfaceNames = interfaces;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        fields.put(name, new Field(access, name, descriptor));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (Modifier.isPublic(access)) {
            sb.append("public ");
        } else if (Modifier.isProtected(access)) {
            sb.append("protected ");
        } else if (Modifier.isPrivate(access)) {
            sb.append("private ");
        }

        if (Modifier.isFinal(access)) {
            sb.append("final ");
        }

        if (Modifier.isStatic(access)) {
            sb.append("static ");
        }

        if (Modifier.isInterface(access)) {
            sb.append("interface ");
        } else if (Modifier.isAbstract(access)) {
            sb.append("abstract class ");
        } else {
            sb.append("class ");
        }

        sb.append(name).append(" ");

        if (superName != null) {
            sb.append("extends ").append(superName);
        }

        if (interfaceNames != null && interfaceNames.length > 0) {
            sb.append("implements ").append(String.join(", ", interfaceNames));
        }
        
        sb.append('\n');
        for (Field f : fields.values()) {
            sb.append("    ");
            if (Modifier.isPublic(f.access)) {
                sb.append("public ");
            } else if (Modifier.isProtected(f.access)) {
                sb.append("protected ");
            } else if (Modifier.isPrivate(f.access)) {
                sb.append("private ");
            }

            if (Modifier.isFinal(f.access)) {
                sb.append("final ");
            }

            if (Modifier.isStatic(f.access)) {
                sb.append("static ");
            }
            
            sb.append(Type.getType(f.descriptor).getClassName()).append(" ").append(f.name).append('\n');
        }
        
        return sb.toString();
    }

    public static class Field {
        public int access;
        public String name;
        public String descriptor;

        public Field(int modifiers, String name, String descriptor) {
            this.access = modifiers;
            this.name = name;
            this.descriptor = descriptor;
        }
    }
}
