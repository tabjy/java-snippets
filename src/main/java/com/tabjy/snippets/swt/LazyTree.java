package com.tabjy.snippets.swt;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.Random;

public class LazyTree {
    static Shell shell;

    static TreeViewer viewer;

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
        viewer = new TreeViewer(shell, SWT.VIRTUAL);

        viewer.setContentProvider(new ILazyTreeContentProvider() {
            @Override
            public Object getParent(Object element) {
                return ((Node) element).getParent();
            }

            @Override
            public void updateChildCount(Object element, int currentChildCount) {
                viewer.setChildCount(element, ((Node) element).getChildrenCount());
            }

            @Override
            public void updateElement(Object parent, int index) {
                Node element = ((Node) parent).getChildren()[index];
                viewer.replace(parent, index, element);
                updateChildCount(element, -1);
            }
        });
        viewer.setUseHashlookup(true);
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);

        TreeViewerColumn name = new TreeViewerColumn(viewer, SWT.NONE);
        name.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return element.toString();
            }
        });
        name.getColumn().setText("Name");
        name.getColumn().setWidth(200);

        TreeViewerColumn level = new TreeViewerColumn(viewer, SWT.NONE);
        level.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Node) element).getLevel() + "";
            }
        });
        level.getColumn().setText("Level");
        level.getColumn().setWidth(200);

        TreeViewerColumn children = new TreeViewerColumn(viewer, SWT.NONE);
        children.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((Node) element).getChildrenCount() + "";
            }
        });
        children.getColumn().setText("Children Count");
        children.getColumn().setWidth(200);

        TreeViewerColumn hashcode = new TreeViewerColumn(viewer, SWT.NONE);
        hashcode.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return element.hashCode() + "";
            }
        });
        hashcode.getColumn().setText("Hashcode");
        hashcode.getColumn().setWidth(200);


        Node root = new Node();
        viewer.setInput(root);
        viewer.setChildCount(root, root.getChildrenCount());
    }

    static class Node {
        Node parent;
        int level;

        Node[] children;
        int childrenCount = new Random().nextInt(100);

        public Node() {
            this(null);
        }

        public Node(Node parent) {
            this.parent = parent;
            if (parent == null) {
                level = 0;
            } else {
                level = parent.level + 1;
            }
        }

        public Node getParent() {
            return parent;
        }

        public int getChildrenCount() {
            return childrenCount;
        }

        public Node[] getChildren() {
            if (children != null) {
                return children;
            }

            children = new Node[childrenCount];
            for (int i = 0; i < childrenCount; i++) {
                children[i] = new Node(this);
            }

            return children;
        }

        public int getLevel() {
            return level;
        }
    }
}
