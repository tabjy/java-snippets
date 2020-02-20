package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Test2 {
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final ScrolledComposite sc = new ScrolledComposite(shell, SWT.VERTICAL | SWT.BORDER);
//		sc.setLayoutData(new RowData(300, 300));
		final Composite c = new Composite(sc, SWT.NONE);
//		c.setLayout(new GridLayout(3, false));
		c.setLayout(new RowLayout());
//		for (int i = 0; i < 10; i++) {
//			Button b = new Button(c, SWT.PUSH);
//			b.setText("Button " + i);
//		}
		sc.setContent(c);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		Button b = new Button(shell, SWT.PUSH);
		b.setText("Add more children");
		b.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Button b = new Button(c, SWT.PUSH);
				b.setText("Another Button " + c.getChildren().length);
				c.layout(false);
				sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
