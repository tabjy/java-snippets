package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;

@SuppressWarnings("Duplicates")
public class SashForm4 {

	protected Shell shell;
	private Table objectSelectionTable;
	private Table referrerTable;
	private Table classTable;
	private Table ancestorReferrerTable;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SashForm4 window = new SashForm4();
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
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm hSash = new SashForm(shell, SWT.NONE);
		
		SashForm vSashLeft = new SashForm(hSash, SWT.VERTICAL);
		
		Group topLeftContainer = new Group(vSashLeft, SWT.NONE);
		topLeftContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
//		TableViewer objectSelectionTableViewer = new TableViewer(topLeftContainer, SWT.BORDER | SWT.FULL_SELECTION);
//		objectSelectionTable = objectSelectionTableViewer.getTable();
		TableViewerFactory.makeTableViewer(topLeftContainer, TableViewerFactory.makeModels((int) (Math.random() * 100)));
		
		Group buttomLeftContainer = new Group(vSashLeft, SWT.NONE);
		buttomLeftContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group classPieChartContainer = new Group(buttomLeftContainer, SWT.NONE);
		classPieChartContainer.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label clasLabel = new Label(classPieChartContainer, SWT.NONE);
		clasLabel.setText("Class");
		
		Button classPieChart = new Button(classPieChartContainer, SWT.NONE);
		classPieChart.setText("[Pie Chart]");
		
		Group classTableContainer = new Group(buttomLeftContainer, SWT.NONE);
		classTableContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TableViewer classTableViewer = new TableViewer(classTableContainer, SWT.BORDER | SWT.FULL_SELECTION);
		classTable = classTableViewer.getTable();
		vSashLeft.setWeights(new int[] {1, 1});
		
		SashForm vSashRight = new SashForm(hSash, SWT.VERTICAL);
		
		Group topRightContainer = new Group(vSashRight, SWT.NONE);
		topRightContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TableViewer referrerTableViewer = new TableViewer(topRightContainer, SWT.BORDER | SWT.FULL_SELECTION);
		referrerTable = referrerTableViewer.getTable();
		
		Group buttomRIghtContainer = new Group(vSashRight, SWT.NONE);
		buttomRIghtContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group ancestorReferrerPieChartContainer = new Group(buttomRIghtContainer, SWT.NONE);
		ancestorReferrerPieChartContainer.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label ancestoreReferrerLabel = new Label(ancestorReferrerPieChartContainer, SWT.NONE);
		ancestoreReferrerLabel.setText("Ancestor referrer");
		
		Button ancestoreReferrerPieChart = new Button(ancestorReferrerPieChartContainer, SWT.NONE);
		ancestoreReferrerPieChart.setText("[Pie Chart]");
		
		Group ancestorReferrerTableContainer = new Group(buttomRIghtContainer, SWT.NONE);
		ancestorReferrerTableContainer.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TableViewer ancestorReferrerTableViewer = new TableViewer(ancestorReferrerTableContainer, SWT.BORDER | SWT.FULL_SELECTION);
		ancestorReferrerTable = ancestorReferrerTableViewer.getTable();
		vSashRight.setWeights(new int[] {1, 1});
		hSash.setWeights(new int[] {1, 1});
	}
}
