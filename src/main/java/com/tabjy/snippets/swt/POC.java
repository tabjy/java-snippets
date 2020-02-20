package com.tabjy.snippets.swt;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class POC {

    private class MyContentProvider implements IStructuredContentProvider {
        @Override
        public Object[] getElements(Object inputElement) {
            return (MyModel[]) inputElement;
        }
    }

    public class MyModel {
        public int counter;

        public MyModel(int counter) {
            this.counter = counter;
        }

        @Override
        public String toString() {
            return "Item " + this.counter;
        }
    }

    public POC(Shell shell) {
        final TableViewer v = new TableViewer(shell);
        v.setLabelProvider(new LabelProvider());
        v.setContentProvider(new MyContentProvider());
        createColumn(v.getTable(), "Values");
        v.getTable().setLinesVisible(true);

        v.getTable().addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("listener");
                update(v);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        update(v);
    }

    public void update(TableViewer v) {
        System.out.println("updating");
        MyModel[] model = createModel();
        v.setInput(model);

        System.out.println("setting selection");
        v.getTable().setSelection(5);
        System.out.println("done setting selection");

        System.out.println("done updating");
    }

    public void createColumn(Table tb, String text) {
        TableColumn column = new TableColumn(tb, SWT.NONE);
        column.setWidth(100);
        column.setText(text);
        tb.setHeaderVisible(true);
    }

    private MyModel[] createModel() {
        MyModel[] elements = new MyModel[10];

        for (int i = 0; i < 10; i++) {
            elements[i] = new MyModel(i);
        }

        return elements;
    }

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        new POC(shell);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }
}