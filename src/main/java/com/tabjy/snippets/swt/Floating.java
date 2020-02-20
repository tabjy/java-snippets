package com.tabjy.snippets.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;

public class Floating {

	protected Shell shell;
	protected Display display;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Floating window = new Floating();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayout(new FormLayout());
		
		Composite floating = new Composite(parent, SWT.NONE);
		floating.setBackground(new Color(display, 0, 0, 0, 128));
		floating.setLayout(null);
		FormData fd_floating = new FormData();
		fd_floating.height = 100;
		fd_floating.width = 100;
		fd_floating.bottom = new FormAttachment(100, -10);
		fd_floating.right = new FormAttachment(100, -10);
		floating.setLayoutData(fd_floating);
		
		Label label = new Label(floating, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setBounds(0, 0, 68, 17);
		label.setText("floating");
		
		Composite canvas = new Composite(parent, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		FormData fd_canvas = new FormData();
		fd_canvas.right = new FormAttachment(100, -5);
		fd_canvas.top = new FormAttachment(0, 5);
		fd_canvas.left = new FormAttachment(0, 5);
		fd_canvas.bottom = new FormAttachment(100, -5);
		canvas.setLayoutData(fd_canvas);

	}
}
