package com.tabjy.snippets.swt;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class TableViewerFactory {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(400, 150);
        shell.setLayout(new FillLayout());
        shell.setText("Object Selection");

        TableViewer viewer = makeTableViewer(shell, makeModels(10));

        viewer.getTable().addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {

            }

            @Override
            public void mouseDown(MouseEvent e) {

            }

            @Override
            public void mouseUp(MouseEvent e) {
                if (viewer.getSelection().isEmpty()) {
                    return;
                }
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                ObjectSelectionModel model = (ObjectSelectionModel) selection.getFirstElement();
                if (e.button == 1) {
                    System.out.println("clicked primary button on: " + model.getName());
                } else if (e.button == 3) {
                    System.out.println("clicked tertiary button on: " + model.getName());
                }
            }
        });

        (new Thread(() -> {
            while (true) {
                Scanner kbd = new Scanner(System.in);
                System.out.println("press enter to update");
                kbd.nextLine();

                shell.getDisplay().asyncExec(() -> viewer.setInput(makeModels(10)));
            }
        })).start();

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        display.dispose();
    }

    public static ObjectSelectionModel[] makeModels(int len) {
        long memorySum = 0;
        long[] memories = new long[len];

        int objectSum = 0;
        int[] objects = new int[len];

        for (int i = 0; i < len; i++) {
            memories[i] = (long) (Math.random() * 100000);
            memorySum += memories[i];

            objects[i] = (int) (Math.random() * 100000);
            objectSum += objects[i];
        }

        ObjectSelectionModel[] models = new ObjectSelectionModel[len + 1];
        models[0] = new ObjectSelectionModel("All Objects", memorySum, 100, 0, 0, objectSum);
        for (int i = 0; i < len; i++) {
            long overhead = (long) (Math.random() * memories[i]);

            models[i + 1] = new ObjectSelectionModel(
                    "Selection " + i,
                    memories[i], (int) (memories[i] * 100 / memorySum),
                    overhead, (int) (overhead * 100 / memorySum), objects[i]
            );
        }

        return models;
    }

    public static TableViewer makeTableViewer(Composite parent, ObjectSelectionModel[] models) {
        final TableViewer tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);

        tableViewer.setContentProvider(ArrayContentProvider.getInstance());

        createTableColumnViewer(tableViewer, "Object Selection",
                ObjectSelectionModel::getName,
                (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));

        createTableColumnViewer(tableViewer, "Memory KB",
                (Function<ObjectSelectionModel, String>) model -> String.format("%d (%d%%)", model.getMemory(),
                        model.getMemoryPercentage()), (lhs, rhs) -> (int) (lhs.getMemory() - rhs.getMemory()), ColumnViewerComparator.Direction.Desc);

        createTableColumnViewer(tableViewer, "Overhead KB",
                (Function<ObjectSelectionModel, String>) model -> String.format("%d (%d%%)",
                        model.getOverhead(), model.getOverheadPercentage()), (lhs, rhs) -> (int) (lhs.getOverhead() - rhs.getOverhead()));

        createTableColumnViewer(tableViewer, "Objects",
                (Function<ObjectSelectionModel, String>) model ->
                        String.valueOf(model.getObjects()), (lhs, rhs) -> lhs.getObjects() - rhs.getObjects());

        tableViewer.setInput(models);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(true);

        return tableViewer;
    }

    private static <T> TableViewerColumn createTableColumnViewer(TableViewer tableViewer, String label, Function<T, String> labelProvider, BiFunction<T, T, Integer> comparator) {
        return createTableColumnViewer(tableViewer, label, labelProvider, comparator, null);
    }

    private static <T> TableViewerColumn createTableColumnViewer(TableViewer tableViewer, String label, Function<T, String> labelProvider, BiFunction<T, T, Integer> comparator, ColumnViewerComparator.Direction sortDirection) {
        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
        column.getColumn().setWidth(200);
        column.getColumn().setText(label);
        column.getColumn().setMoveable(true);

        column.setLabelProvider(new OwnerDrawLabelProvider() {
            @Override
            protected void measure(Event event, Object element) {
                // no op
            }

            @Override
            protected void paint(Event event, Object element) {
                Widget item = event.item;
                Rectangle bounds = ((TableItem) item).getBounds(event.index);
                Color bg = event.gc.getBackground();
                Color fg = event.gc.getForeground();

                Point p = event.gc.stringExtent(labelProvider.apply((T) element));

                int margin = (bounds.height - p.y) / 2;
                int dx = bounds.x + margin;

                if (label.equals("Object Selection")) {
                    event.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                    event.gc.fillArc(dx, bounds.y + margin + margin, p.y - margin - margin, p.y - margin - margin, 0, 360);

                    dx += p.y + margin;
                }

                event.gc.setBackground(bg);
                event.gc.setForeground(fg);

                event.gc.drawString(labelProvider.apply((T) element), dx, bounds.y + margin, true);
            }

            @Override
            protected void erase(Event event, Object element) {
                // no op
            }
        });

        ColumnViewerComparator cmp = new ColumnViewerComparator(tableViewer, column) {
            @Override
            protected int doCompare(Object e1, Object e2) {
                return comparator.apply((T) e1, (T) e2);
            }
        };

        if (sortDirection != null) {
            cmp.setSorter(sortDirection);
        }

        return column;
    }

    @SuppressWarnings("Duplicates")
    private static abstract class ColumnViewerComparator extends ViewerComparator {

        public enum Direction {
            Asc,
            Desc
        }

        private Direction direction = null;
        private TableViewerColumn column;
        private ColumnViewer viewer;

        public ColumnViewerComparator(ColumnViewer viewer, TableViewerColumn column) {
            this.column = column;
            this.viewer = viewer;

            ColumnViewerComparator that = this;

            this.column.getColumn().addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (viewer.getComparator() == null) {
                        that.setSorter(Direction.Asc);
                        return;
                    }

                    if (viewer.getComparator() != that) {
                        that.setSorter(Direction.Asc);
                        return;
                    }

                    switch (that.direction) {
                        case Asc:
                            that.setSorter(Direction.Desc);
                            break;
                        case Desc:
                            that.setSorter(null);
                            break;
                    }
                }
            });
        }

        public void setSorter(Direction direction) {
            this.direction = direction;

            Table columnParent = column.getColumn().getParent();
            if (direction == null) {
                columnParent.setSortColumn(null);
                columnParent.setSortDirection(SWT.NONE);
                viewer.setComparator(null);
                return;
            }

            columnParent.setSortColumn(column.getColumn());
            columnParent.setSortDirection(direction == Direction.Asc ? SWT.UP : SWT.DOWN);

            if (viewer.getComparator() == this) {
                viewer.refresh();
            } else {
                viewer.setComparator(this);
            }
        }

        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            int multiplier = 0;
            if (direction == Direction.Asc) {
                multiplier = 1;
            } else if (direction == Direction.Desc) {
                multiplier = -1;
            }
            return multiplier * doCompare(e1, e2);
        }

        protected abstract int doCompare(Object e1, Object e2);
    }

//    public static class ObjectSelectionTableLabelProvider extends LabelProvider implements ITableLabelProvider {
//        @Override
//        public String getColumnText(Object element, int columnIndex) {
//            ObjectSelectionModel model = (ObjectSelectionModel) element;
//
//            switch (columnIndex) {
//                case 0:
//                    return model.getName();
//                case 1:
//                    return String.format("%d (%d%%)", model.getMemory(), model.getMemoryPercentage());
//                case 2:
//                    return String.format("%d (%d%%)", model.getOverhead(), model.getOverheadPercentage());
//                case 3:
//                    return model.getObjects() + "";
//                default:
//                    throw new RuntimeException("columnIndex out of bound");
//            }
//        }
//
//        @Override
//        public Image getColumnImage(Object element, int columnIndex) {
//            return null;
//        }
//    }

    public static class ObjectSelectionModel {
        private String name;
        private long memory;
        private int memoryPercentage;
        private long overhead;
        private int overheadPercentage;
        private int objects;

        public ObjectSelectionModel(String name, long memory, int memoryPercentage, long overhead, int overheadPercentage, int objects) {
            this.name = name;
            this.memory = memory;
            this.memoryPercentage = memoryPercentage;
            this.overhead = overhead;
            this.overheadPercentage = overheadPercentage;
            this.objects = objects;
        }

        public String getName() {
            return name;
        }

        public long getMemory() {
            return memory;
        }

        public int getMemoryPercentage() {
            return memoryPercentage;
        }

        public long getOverhead() {
            return overhead;
        }

        public int getOverheadPercentage() {
            return overheadPercentage;
        }

        public int getObjects() {
            return objects;
        }
    }
}
