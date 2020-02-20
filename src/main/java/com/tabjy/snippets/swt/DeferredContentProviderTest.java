package com.tabjy.snippets.swt;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.deferred.DeferredContentProvider;
import org.eclipse.jface.viewers.deferred.SetModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeferredContentProviderTest {
    private static final int NUM_ROWS = 100;

    public static void main(String[] args) {
        // Create concurrent model instance
        final SetModel model = new SetModel();

        // Create UI
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        Table table = new Table(shell, SWT.BORDER | SWT.VIRTUAL | SWT.HIDE_SELECTION );
        //new TableColumn(table, 100, SWT.LEAD);

        // Create viewer using DeferredContentProvider
        TableViewer viewer = new TableViewer(table);
        viewer.setContentProvider(new DeferredContentProvider(Comparator.comparingInt(lhs -> Integer.parseInt(String.valueOf(lhs)))));
        viewer.setInput(model);

        // Fill concurrnt model with content
        Thread modelThread = new Thread(() -> {
            List<String> items = new ArrayList<>(NUM_ROWS);
            for (int i = 0; i < NUM_ROWS; i++) {
                items.add("" + (i));
            }
            model.addAll(items);
        });

        // Show UI (scrolling with scrollbar will show)
        modelThread.start();
        shell.setLayout(new FillLayout());
        shell.setSize(100, 20 * table.getItemHeight()); // first 20 items
        shell.open();

        while (!shell.isDisposed ()) {
            if (!display.readAndDispatch ()) display.sleep ();
        }
        display.dispose ();
    }
}