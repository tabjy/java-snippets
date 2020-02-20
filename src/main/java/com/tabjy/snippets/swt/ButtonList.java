package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ButtonList {
	public static void main(String[] a) {
        final Display d = new Display();
        final Shell shell = new Shell(d);
        shell.setSize(300, 300);
        shell.setLayout(new FillLayout());
        
        Composite container = new Composite(shell, SWT.NONE);
        container.setBackground(d.getSystemColor(SWT.COLOR_BLUE));
        
        RowLayout layout = new RowLayout(SWT.VERTICAL); 
        layout.fill = true;
        layout.center = true;
        container.setLayout(layout);
        
        for (int i = 0; i < 4; i++) {
        	Button button = new Button(container, SWT.NONE);
        	button.setText("Button " + i);
        }
        
        shell.open();
        while (!shell.isDisposed()) {
            if (!d.readAndDispatch())
                d.sleep();
        }
        d.dispose();
	}
}
