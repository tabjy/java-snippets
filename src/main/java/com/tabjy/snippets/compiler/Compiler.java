package com.tabjy.snippets.compiler;

import javax.lang.model.SourceVersion;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.Collections;

public class Compiler {
	public static void main(String[] args) {
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		for (final SourceVersion version : compiler.getSourceVersions()) {
			System.out.println(version);
		}

		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		final StandardJavaFileManager manager = compiler.getStandardFileManager(diagnostics, null, null);

		final File file = new File(
				"/home/kxu/Documents/github.com/tabjy/java-snippets/src/com/tabjy/snippets/compiler/SampleClass.java");

		final Iterable<? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Collections.singletonList(file));

		final JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sources);
		task.call();
	}
}
