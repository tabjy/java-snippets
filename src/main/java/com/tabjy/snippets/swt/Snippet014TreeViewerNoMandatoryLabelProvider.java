package com.tabjy.snippets.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * A simple TreeViewer to demonstrate usage
 *
 */
public class Snippet014TreeViewerNoMandatoryLabelProvider {
    private class MyContentProvider implements ITreeContentProvider {

        @Override
        public Object[] getElements(Object inputElement) {
            return ((MyModel) inputElement).child.toArray();
        }

        @Override
        public void dispose() {

        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

        @Override
        public Object[] getChildren(Object parentElement) {
            return getElements(parentElement);
        }

        @Override
        public Object getParent(Object element) {
            if (element == null) {
                return null;
            }

            return ((MyModel) element).parent;
        }

        @Override
        public boolean hasChildren(Object element) {
            return ((MyModel) element).child.size() > 0;
        }

    }

    public class MyModel {
        public MyModel parent;

        public List<MyModel> child = new ArrayList<>();

        public int counter;

        public MyModel(int counter, MyModel parent) {
            this.parent = parent;
            this.counter = counter;
        }

        @Override
        public String toString() {
            String rv = "Item ";
            if (parent != null) {
                rv = parent + ".";
            }

            rv += counter;

            return rv;
        }
    }

    public class MyLabelProvider extends LabelProvider implements ITableLabelProvider{
        FontRegistry registry = new FontRegistry();

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            return "Column " + columnIndex + " => " + element;
        }
    }

    public Snippet014TreeViewerNoMandatoryLabelProvider(Shell shell) {
        final TreeViewer v = new TreeViewer(shell) {
            @Override
            protected void handleTreeCollapse(TreeEvent event) {
                // super.handleTreeCollapse(event);
                System.out.println("handleTreeCollapse");
            }
        };

        TreeColumn column = new TreeColumn(v.getTree(),SWT.NONE);
        column.setWidth(200);
        column.setText("Column 1");

        column = new TreeColumn(v.getTree(),SWT.NONE);
        column.setWidth(200);
        column.setText("Column 2");

        v.setLabelProvider(new MyLabelProvider());
        v.setContentProvider(new MyContentProvider());
        v.setInput(createModel());
        v.getTree().setHeaderVisible(true);
        v.expandAll();
    }

    private MyModel createModel() {

        MyModel root = new MyModel(0, null);
        root.counter = 0;

        MyModel tmp;
        MyModel tmp2;
        for (int i = 1; i < 10; i++) {
            tmp = new MyModel(i, root);
            root.child.add(tmp);
            for (int j = 1; j < i; j++) {
                tmp2 = new MyModel(j, tmp);
                tmp.child.add(tmp2);
                for (int k = 1; k < j; k++) {
                    tmp2.child.add(new MyModel(k, tmp2));
                }
            }
        }

        return root;
    }

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        new Snippet014TreeViewerNoMandatoryLabelProvider(shell);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }
}
