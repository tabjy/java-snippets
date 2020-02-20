package com.tabjy.snippets.swt;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.deferred.DeferredContentProvider;
import org.eclipse.jface.viewers.deferred.IConcurrentModel;
import org.eclipse.jface.viewers.deferred.IConcurrentModelListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.util.Comparator;

public class LongTable {
    static Shell shell;

    static TableViewer viewer;
    static ConcurrentModelInputWrapper input;

    public static void main(String[] args) {
        Display display = new Display();
        shell = new Shell(display);
        shell.setSize(400, 150);
        shell.setLayout(new FillLayout());

        createView();

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }

    private static void createView() {
        TableViewer tb = new TableViewer(shell, SWT.VIRTUAL);
        viewer = tb;

        tb.setContentProvider(new DeferredContentProvider(Comparator.comparingInt(Object::hashCode)));

        TableViewerColumn col = new TableViewerColumn(tb, SWT.VIRTUAL);

        col.getColumn().setWidth(200);
        col.getColumn().setText("column");
        col.getColumn().setMoveable(true);

        col.setLabelProvider(new OwnerDrawLabelProvider() {
            @Override
            protected void measure(Event event, Object element) {

            }

            @Override
            protected void paint(Event event, Object element) {
                String text = "not loaded";
                if (element != null) {
                    text = element.toString();
                } else {
                    return;
                }

                Widget item = event.item;
                Rectangle bounds = ((TableItem) item).getBounds(event.index);
                event.gc.drawString(text, bounds.x, bounds.y, true);
            }
        });

        col.setLabelProvider(new ColumnLabelProvider());

        tb.getTable().setLinesVisible(true);
        tb.getTable().setHeaderVisible(true);

        input = new ConcurrentModelInputWrapper();
        tb.setInput(input);

        {
            input.setInput(makeModels(50));
        }
    }

    private static Object[] makeModels(int size) {
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = new Object();
        }

        return res;
    }

    public static class ConcurrentModelInputWrapper implements IConcurrentModel {
        ListenerList<IConcurrentModelListener> mModelListeners = new ListenerList<>();
        Object[] mElements;

        public void setInput(Object input) {
            mElements = (Object[]) input;

            for (Object obj : mElements) {
                if (obj == null) {
                    System.err.print("wtf???");
                    System.exit(1);
                }
            }

            for (IConcurrentModelListener l : mModelListeners) {
                l.setContents(mElements);
            }
        }

        @Override
        public void requestUpdate(IConcurrentModelListener listener) {
            listener.setContents(mElements);
        }

        @Override
        public void addListener(IConcurrentModelListener listener) {
            mModelListeners.add(listener);
            requestUpdate(listener);
        }

        @Override
        public void removeListener(IConcurrentModelListener listener) {
            mModelListeners.remove(listener);
        }
    }
}
