package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class FileDialogs {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.open();

		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText("Export Flame Graph");
		fd.setFilterNames(new String[] {"JPEG image", "PNG image"});
		fd.setFilterExtensions(new String[] {"*.jpg", "*.png"});
		fd.setFileName("flame_graph");
		fd.setOverwrite(true);
		fd.open();

		System.out.println(fd.getFileName());
		System.out.println(fd.getFilterIndex());
		// FIXME: FileDialog filterIndex returns -1: https://bugs.eclipse.org/bugs/show_bug.cgi?id=546256
		if (fd.getFileName().endsWith(".jpg")) {
			
		} else if (fd.getFileName().endsWith(".png")) {
			
		}

		shell.dispose();
		display.dispose();
	}
}
