package com.tabjy.snippets.asm.path_syntax_evaluation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class Generator {
    public static void main(String[] args) throws IOException {
        Printer printer = new ASMifier();

        PrintWriter output = new PrintWriter("/home/kxu/Documents/github.com/tabjy/java-snippets/src/main/java/com/tabjy/snippets/asm/path_syntax_evaluation/TargetDump.java");
        PrintWriter logger = new PrintWriter(System.err, true);

        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, output);

        InputStream inputStream = new FileInputStream("/home/kxu/Documents/github.com/tabjy/java-snippets/target/classes/com/tabjy/snippets/asm/path_syntax_evaluation/Target.class");
        new ClassReader(inputStream).accept(traceClassVisitor, 0);
    }
}
