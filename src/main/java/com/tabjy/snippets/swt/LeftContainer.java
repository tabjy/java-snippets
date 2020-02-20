package com.tabjy.snippets.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

public class LeftContainer {

	protected Shell shell;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LeftContainer window = new LeftContainer();
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
		shell.setLayout(new FormLayout());
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("New Label");
		
		SashForm sashForm = new SashForm(shell, SWT.VERTICAL);
		
		
		Button btnNewButton = new Button(sashForm, SWT.NONE);
		btnNewButton.setText("New Button");
		
		Button btnNewButton_1 = new Button(sashForm, SWT.NONE);
		btnNewButton_1.setText("New Button");
		sashForm.setWeights(new int[] {1, 1});
		
		text = new Text(shell, SWT.BORDER);
		text.setToolTipText("");
		
		
		
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		FormData fd_btnNewButton_2 = new FormData();
		fd_btnNewButton_2.bottom = new FormAttachment(100, -10);
		fd_btnNewButton_2.right = new FormAttachment(100, -10);
		btnNewButton_2.setLayoutData(fd_btnNewButton_2);
		btnNewButton_2.setText("New Button");
		
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(100, -112);
		fd_text.bottom = new FormAttachment(btnNewButton_2, 0, SWT.CENTER);
		fd_text.left = new FormAttachment(0, 10);
		text.setLayoutData(fd_text);
		text.setMessage("Ancestor prefix");
		
		
		FormData fd_sashForm = new FormData();
		fd_sashForm.bottom = new FormAttachment(btnNewButton_2, -10);
		fd_sashForm.top = new FormAttachment(lblNewLabel, 10);
		fd_sashForm.right = new FormAttachment(100, -10);
		fd_sashForm.left = new FormAttachment(0, 10);
		sashForm.setLayoutData(fd_sashForm);
		
		text.addListener(SWT.Traverse, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	            if(event.detail == SWT.TRAVERSE_RETURN){
	                System.out.println("prefix: " + text.getText());
	            }
	        }
	    });

	}
}
