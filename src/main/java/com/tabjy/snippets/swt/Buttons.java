package com.tabjy.snippets.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class Buttons {

	 public static void main (String [] args) {
	      Display display = new Display ();
	      Shell shell = new Shell (display);
	      shell.setLayout(new FillLayout());
	      
	      final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);
	     
	      shell.open ();
	      while (!shell.isDisposed ()) {
	          if (!display.readAndDispatch ()) display.sleep ();
	      }
	      display.dispose ();
	 }
	 

}
