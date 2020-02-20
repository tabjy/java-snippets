package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This class demonstrates ScrolledComposite
 */
public class ScrolledCompositeTest {
	public static void main(String[] args) {
		Display display = new Display();
		Image image1 = display.getSystemImage(SWT.ICON_WORKING);
		Image image2 = display.getSystemImage(SWT.ICON_QUESTION);
		Image image3 = display.getSystemImage(SWT.ICON_ERROR);

		Shell shell = new Shell(display);
		shell.setText("Snippet 166");
		shell.setLayout(new FillLayout());

		final ScrolledComposite scrollComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);

		final Composite parent = new Composite(scrollComposite, SWT.NONE);
//		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
//		layout.wrap = true;
		parent.setLayout(new ColumnLayout());

		scrollComposite.setContent(parent);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setExpandHorizontal(true);
//		scrollComposite.addControlListener(ControlListener.controlResizedAdapter(e -> {
//			Rectangle r = scrollComposite.getClientArea();
//			scrollComposite.setMinSize(parent.computeSize(r.width, SWT.DEFAULT));
//		}));
		
		for(int i = 0; i <= 0; i++) {
//			Label label = new Label(parent, SWT.NONE);
//			if (i % 3 == 0) label.setImage(image1);
//			if (i % 3 == 1) label.setImage(image2);
//			if (i % 3 == 2) label.setImage(image3);
			Button btn = new Button(parent, SWT.NONE);
			btn.setText(btn.toString());
		}
		
		Button add = new Button(shell, SWT.NONE);
		add.setText("add");
		add.addListener(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				System.out.println("test");
				
				Button btn = new Button(parent, SWT.NONE);
				btn.setText(btn.toString());
//				parent.layout();
//				scrollComposite.update();
				
				scrollComposite.layout(true, true);
				
				Rectangle r = scrollComposite.getClientArea();
				scrollComposite.setMinSize(parent.computeSize(r.width, SWT.DEFAULT));
			}
		});
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
