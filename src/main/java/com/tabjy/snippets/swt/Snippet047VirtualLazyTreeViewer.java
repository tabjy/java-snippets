package com.tabjy.snippets.swt;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Random;


public class Snippet047VirtualLazyTreeViewer {


    private TreeViewer fViewer;


    private class MyContentProvider implements ILazyTreeContentProvider {

        @Override
        public Object getParent(Object element) {
            return ((Node) element).getParent();
        }

        @Override
        public void updateChildCount(Object element, int currentChildCount) {
            fViewer.setChildCount(element, ((Node) element).getChildrenCount());
        }

        @Override
        public void updateElement(Object parent, int index) {
            Node element = ((Node) parent).getChildren()[index];
            fViewer.replace(parent, index, element);
            updateChildCount(element, -1);
        }
    }

    static class Node {
        Node parent;
        int level;

        Node[] children;
        int childrenCount = new Random().nextInt(1000);

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
    }


    public Snippet047VirtualLazyTreeViewer(Shell shell) {
        fViewer = new TreeViewer(shell, SWT.VIRTUAL | SWT.BORDER);
        fViewer.setLabelProvider(new LabelProvider());
        fViewer.setContentProvider(new MyContentProvider());
        fViewer.setUseHashlookup(true);
        Node root = new Node();
        fViewer.setInput(root);
        fViewer.setChildCount(root, root.getChildrenCount());
        // at this point the model only contains what is visible on the screen.
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());
        new Snippet047VirtualLazyTreeViewer(shell);
        shell.setSize(800, 600);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    public static int getRandomChildCount() {
        return new Random().nextInt(1000) + 100;
    }
}