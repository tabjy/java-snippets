package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Polygon {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Polygon window = new Polygon();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
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
		
		shell.setLayout(new FillLayout());
		Canvas cnvs = new Canvas(shell, SWT.NONE);
		
		cnvs.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				int dx = 500;
				int dy = 500;
				GC gc = e.gc;
				gc.drawPolygon(new int[] {
						30 + dx, 00 + dy, //
						60 + dx, 70 + dy, //
						40 + dx, 70 + dy, //
						40 + dx, 90 + dy, //
						80 + dx, 90 + dy, //
						80 + dx, 110 + dy, //
						20 + dx, 110 + dy, //
						20 + dx, 70 + dy, //
						00 + dx, 70 + dy
						});
			}
		});

	}

}
