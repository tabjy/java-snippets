package com.tabjy.snippets.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;

public class NonRectangular {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NonRectangular window = new NonRectangular();
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
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);

		// make the region for the main window
		// circle is a method that returns a list of points defining a circle
//		Region region = new Region();
//		region.add(350, 0, 981, 51);
//		region.add(circle(380,51,30));
//		region.add(circle(951,51,30));
//		region.add(380, 51, 571, 30);
//		shell.setRegion(region);
//		Rectangle rsize = region.getBounds();
//		shell.setSize(rsize.width, rsize.height);
		shell.setSize(300, 300);

		shell.setLayout(new FillLayout());
		
		Composite main = new Composite(shell, SWT.NULL);
		
		
		main.setLayout(new FillLayout());
		
		// make the label
		Composite cpyLbl = new Composite(main, SWT.NONE);
		
		main.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("outter clicked");
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		cpyLbl.setBackground(SWTResourceManager.getColor(38,255,23));
//
		Region cpyRegion = new Region();
		cpyRegion.add(new Rectangle(0, 0, 100, 100));
		cpyLbl.setRegion(cpyRegion);
		
		cpyLbl.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("inner clicked");
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		// the top left of the bounds is the 0,0 of the region coordinates
		// bounds are in screen coordinates (maybe shell coordinates?)
//		cpyLbl.setBounds(10, 10, 10, 10);
		cpyLbl.setVisible(true);
	}
	
	static int[] circle(int r, int offsetX, int offsetY) {
	    int[] polygon = new int[8 * r + 4];
	    // x^2 + y^2 = r^2
	    for (int i = 0; i < 2 * r + 1; i++) {
	      int x = i - r;
	      int y = (int) Math.sqrt(r * r - x * x);
	      polygon[2 * i] = offsetX + x;
	      polygon[2 * i + 1] = offsetY + y;
	      polygon[8 * r - 2 * i - 2] = offsetX + x;
	      polygon[8 * r - 2 * i - 1] = offsetY - y;
	    }
	    return polygon;
	  }

}
