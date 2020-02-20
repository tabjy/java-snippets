package com.tabjy.snippets.swt;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arcs {

    public static void main(String[] a) {

        final Display d = new Display();
        final Shell shell = new Shell(d);
        shell.setSize(300, 300);
        shell.setLayout(new FillLayout());

//        PieChart pie = new PieChart(shell, SWT.BORDER);
//
//        ArcItem arc30 = new ArcItem(pie, SWT.BORDER);
//        arc30.setAngle(30);
//        arc30.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
//
//        ArcItem arc60 = new ArcItem(pie, SWT.NONE);
//        arc60.setAngle(60);
//        arc60.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
//
//        ArcItem arc45 = new ArcItem(pie, SWT.NONE);
//        arc45.setAngle(45);
//        arc45.setColor(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
//
//        ArcItem arc90 = new ArcItem(pie, SWT.BORDER);
//        arc90.setAngle(90);
//        arc90.setColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
//
//        pie.setZoomRatio(1.25);

        PieChartViewer viewer = new PieChartViewer(shell, SWT.BORDER);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setArcAttributeProvider(new ArcAttributeProvider(){
            @Override
            public int getWeight(Object element) {
                return ((Model) element).weight;
            }

            @Override
            public Color getColor(Object element) {
                return ((Model) element).color;
            }
        });
        viewer.getPieChart().setZoomRatio(1.2);

        viewer.setComparator(new ViewerComparator() {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                return ((Model) e2).weight - ((Model) e1).weight;
            }
        });
        viewer.getPieChart().addMouseListener(new MouseListener() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {

            }

            @Override
            public void mouseDown(MouseEvent e) {

            }

            @Override
            public void mouseUp(MouseEvent e) {
                viewer.setInput(generateModels());
            }
        });
        viewer.setMinimumArcAngle(90);

        viewer.setInput(generateModels());

        shell.open();
        while (!shell.isDisposed()) {
            if (!d.readAndDispatch())
                d.sleep();
        }
        d.dispose();
    }

    static class Model {
        int weight;
        Color color;

        Model(int weight, Color color) {
            this.weight = weight;
            this.color = color;
        }
    }

    private static Object[] generateModels() {
        Object[] res = new Model[10];
        for (int i = 0; i < res.length; i++) {
            int r = (int) (Math.random() * 256);
            res[i] = new Model(256 - r, new Color(Display.getDefault(), r, r, r));
        }

        return res;
    }

    static class PieChart extends Canvas {
        static int MARGIN = 5;
        static double zoomRatio = 1;

        List<ArcItem> arcs = new ArrayList<>();

        private ArcItem highlightedItem;

        PieChart(Composite parent, int style) {
            super(parent, style);

            setLayout(new FillLayout());

            addPaintListener((e) -> {
                int x = this.getClientArea().width / 2;
                int y = this.getClientArea().height / 2;

                Point center = new Point(x, y);

                int radius = Math.min(x, y) - MARGIN;
                radius = (int) (radius / zoomRatio);
                if (radius < 0) {
                    radius = 0;
                }

                int startAngle = 0;
                Color background = e.gc.getBackground();
                for (ArcItem item : arcs) {
                    e.gc.setBackground(background);
                    if (highlightedItem == item) {
                        item.paintArc(e.gc, center, radius, startAngle, zoomRatio, (style & SWT.BORDER) == SWT.BORDER);
                    } else {
                        item.paintArc(e.gc, center, radius, startAngle, 1, (style & SWT.BORDER) == SWT.BORDER);
                    }
                    startAngle += item.getAngle();
                }

                if (startAngle < 360 && (style & SWT.BORDER) == SWT.BORDER) {
                    e.gc.drawArc(center.x - radius, center.y - radius, radius * 2, radius * 2, startAngle, 360 - startAngle);
                }
            });

            addListener(SWT.Resize, (Event e) -> redraw());

            addMouseMoveListener(e -> {
                highlightedItem = getItem(new Point(e.x, e.y));
                redraw();
            });
        }

        void createItem(ArcItem arc, int i) {
            if (i < 0 || i > arcs.size()) {
                SWT.error(SWT.ERROR_INVALID_RANGE);
                return;
            }

            if (i == arcs.size()) {
                arcs.add(arc);
                return;
            }

            arcs.get(i).dispose();
            arcs.set(i, arc);
        }

        int getItemCount() {
            return arcs.size();
        }

        ArcItem[] getItems() {
            return arcs.toArray(new ArcItem[0]);
        }

        ArcItem getItemByAngle(int angle) {
            angle %= 360;

            if (angle < 0) {
                angle += 360;
            }

            int startAngle = 0;
            for (ArcItem item : arcs) {
                if (angle > startAngle && angle < startAngle + item.angle) {
                    return item;
                }

                startAngle += item.getAngle();
            }

            return null;
        }

        ArcItem getItem(Point point) {
            int x = this.getClientArea().width / 2;
            int y = this.getClientArea().height / 2;

            int radius = Math.min(x, y) - MARGIN;
            radius = (int) (radius / zoomRatio);

            if (radius < 0) {
                radius = 0;
            }

            x = point.x - x;
            y = y - point.y;

            ArcItem item = getItemByAngle((int) Math.toDegrees(Math.atan2(y, x)));
            if (item == null) {
                return null;
            }

            if (item == highlightedItem && Math.sqrt(x * x + y * y) < (radius * zoomRatio)) {
                return item;
            }

            if (Math.sqrt(x * x + y * y) < radius) {
                return item;
            }

            return null;
        }

        ArcItem getItem(int index) {
            return arcs.get(index);
        }

        int indexOf(ArcItem item) {
            return arcs.indexOf(item);
        }

        void removeItem(int index) {
            arcs.get(index).dispose();
            arcs.remove(index);
        }

        void setZoomRatio(double ratio) {
            zoomRatio = ratio;
        }
    }

    static class ArcItem extends Item {
        PieChart parent;
        int style;
        int index;

        int angle = 0;
        Color color;

        ArcItem(PieChart parent, int style) {
            this(parent, style, parent.getItemCount());
        }

        ArcItem(PieChart parent, int style, int index) {
            super(parent, style, index);
            this.parent = parent;
            this.style = style;
            this.index = index;

            parent.createItem(this, index);
        }

        void setAngle(int angle) {
            if (angle < 0) {
                SWT.error(SWT.ERROR_INVALID_RANGE);
                return;
            }

            if (this.angle > 360) {
                this.angle = angle % 360;
            } else {
                this.angle = angle;
            }
            parent.redraw();
        }

        int getAngle() {
            return angle;
        }

        void setColor(Color color) {
            this.color = color;
            parent.redraw();
        }

        Color getColor() {
            return color;
        }

        void paintArc(GC gc, Point center, int radius, int startAngle, double zoomRatio, boolean paintArcBorder) {
            if (angle < 0) {
                SWT.error(SWT.ERROR_INVALID_RANGE);
            }

            if (color != null) {
                gc.setBackground(this.color);
            }

            int outerRadius = (int) (radius * zoomRatio);

            gc.fillArc(center.x - outerRadius, center.y - outerRadius, outerRadius * 2, outerRadius * 2, startAngle, angle);

            if (paintArcBorder) {
                gc.drawArc(center.x - outerRadius, center.y - outerRadius, outerRadius * 2, outerRadius * 2, startAngle, angle);
                if (zoomRatio != 1 && angle < 360) {
                    gc.drawLine(
                            (int) (center.x + Math.cos(Math.toRadians(startAngle)) * radius),
                            (int) (center.y - Math.sin(Math.toRadians(startAngle)) * radius),
                            (int) (center.x + Math.cos(Math.toRadians(startAngle)) * outerRadius),
                            (int) (center.y - Math.sin(Math.toRadians(startAngle)) * outerRadius));
                    gc.drawLine(
                            (int) (center.x + Math.cos(Math.toRadians(startAngle + angle)) * radius),
                            (int) (center.y - Math.sin(Math.toRadians(startAngle + angle)) * radius),
                            (int) (center.x + Math.cos(Math.toRadians(startAngle + angle)) * outerRadius),
                            (int) (center.y - Math.sin(Math.toRadians(startAngle + angle)) * outerRadius));
                }
            }

            if ((style & SWT.BORDER) == SWT.BORDER && angle < 360) {
                gc.drawLine(
                        center.x,
                        center.y,
                        (int) (center.x + Math.cos(Math.toRadians(startAngle)) * radius),
                        (int) (center.y - Math.sin(Math.toRadians(startAngle)) * radius));
                gc.drawLine(
                        center.x,
                        center.y,
                        (int) (center.x + Math.cos(Math.toRadians(startAngle + angle)) * radius),
                        (int) (center.y - Math.sin(Math.toRadians(startAngle + angle)) * radius));
            }
        }
    }

    static class PieChartViewer extends StructuredViewer {

        private PieChart pieChart;
        private IArcAttributeProvider arcAttributeProvider = new ArcAttributeProvider();
        private int minimumArcAngle = 0;
        private ArcItem otherArc;

        private List<?> inputs = new ArrayList<>();

        public PieChartViewer(Composite parent) {
            this(parent, SWT.BORDER);
        }

        public PieChartViewer(Composite parent, int style) {
            this(new PieChart(parent, style));
        }

        public PieChartViewer(PieChart pieChart) {
            this.pieChart = pieChart;
        }

        public PieChart getPieChart() {
            return pieChart;
        }

        @Override
        protected Widget doFindInputItem(Object element) {
            if (equals(element, getRoot())) {
                return getControl();
            }

            return null;
        }

        @Override
        protected Widget doFindItem(Object element) {
            if (inputs.contains(element)) {
                return pieChart.getItem(inputs.indexOf(element));
            }
            return null;
        }

        @Override
        protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
            updateItems();
        }

        @Override
        protected List getSelectionFromWidget() {
            List<Object> res = new ArrayList<>();
            if (pieChart.highlightedItem == null) {
                return res;
            }

            int i = pieChart.indexOf(pieChart.highlightedItem);
            if (i == -1) {
                return res;
            }

            res.add(inputs.get(i));
            return res;
        }

        @Override
        protected void internalRefresh(Object element) {
            updateItems();
        }

        @Override
        protected void inputChanged(Object input, Object oldInput) {
            inputs = Arrays.asList(getSortedChildren(getRoot()));
            pieChart.highlightedItem = null;
            updateItems();
        }

        @Override
        public void reveal(Object element) {
            // intentionally empty
        }

        @Override
        protected void setSelectionToWidget(List l, boolean reveal) {
            if (l == null) {
                pieChart.highlightedItem = null;
                return;
            }

            if (l.size() == 0) {
                return;
            }

            pieChart.highlightedItem = (ArcItem) doFindItem(l.get(0));
            pieChart.redraw();
        }

        @Override
        public Control getControl() {
            return pieChart;
        }

        public void setArcAttributeProvider(IArcAttributeProvider provider) {
            if (arcAttributeProvider == null) {
                arcAttributeProvider = new ArcAttributeProvider();
            } else {
                arcAttributeProvider = provider;
            }

        }

        private void updateItems() {
            int weightSum = 0;
            for (Object input : inputs) {
                weightSum += arcAttributeProvider.getWeight(input);
            }

            double otherAngle = 0;
            List<Object> inputs = new ArrayList<>();
            List<Double> angles = new ArrayList<>();
            for (Object input: this.inputs) {
                double angle = 360f * (double) arcAttributeProvider.getWeight(input) / (double) weightSum;
                if (angle >= minimumArcAngle) {
                    inputs.add(input);
                    angles.add(angle);
                } else {
                    otherAngle += angle;
                }
            }

            while (pieChart.getItemCount() < inputs.size()) {
                new ArcItem(pieChart, SWT.BORDER);
            }

            while (inputs.size() < pieChart.getItemCount()) {
                pieChart.removeItem(pieChart.getItemCount() - 1);
            }

            if (otherAngle != 0) {
                otherArc = new ArcItem(pieChart, SWT.BORDER);
            }

            int angleSum = 0;
            for (int i = 0; i < inputs.size(); i++) {
                Object input = inputs.get(i);
                ArcItem item = pieChart.getItem(i);

                int w = (int) Math.round(angles.get(i));
                angleSum += w;
                item.setAngle(w);
                item.setColor(arcAttributeProvider.getColor(input));
            }

            if (otherAngle != 0) {
                int w = 360 - angleSum;
                if (w > 0) {
                    otherArc.setAngle(w);
                    otherArc.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                    return;
                }
            } else {
                otherArc = null;
            }

            // fix rounding error
            if (angleSum != 0 && angleSum != 360 && inputs.size() != 0) {
                for (int i = inputs.size() - 1; i >= 0; i--) {
                    Object input = inputs.get(i);
                    ArcItem item = pieChart.getItem(i);

                    int w = 360 - angleSum + (int) Math.round(360 * (double) arcAttributeProvider.getWeight(input) / weightSum);
                    if (w < 0) {
                        continue;
                    }
                    item.setAngle(w);
                    break;
                }
            }
        }

        void setMinimumArcAngle (int angle) {
            minimumArcAngle = angle;
        }
    }

    interface IArcAttributeProvider {
        int getWeight(Object element);

        Color getColor(Object element);
    }

    static class ArcAttributeProvider implements IArcAttributeProvider {
        public static final Color[] COLORS = {new Color(Display.getDefault(), 250, 206, 210), // red
                new Color(Display.getDefault(), 185, 214, 255), // blue
                new Color(Display.getDefault(), 229, 229, 229), // grey
                new Color(Display.getDefault(), 255, 231, 199), // orange
                new Color(Display.getDefault(), 171, 235, 238), // aqua
                new Color(Display.getDefault(), 228, 209, 252), // purple
                new Color(Display.getDefault(), 255, 255, 255), // white
                new Color(Display.getDefault(), 205, 249, 212), // green
        };

        int i = 0;

        @Override
        public int getWeight(Object element) {
            return 1;
        }

        @Override
        public Color getColor(Object element) {
            return COLORS[i++ % COLORS.length];
        }
    }
}

