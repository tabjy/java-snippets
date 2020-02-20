package com.tabjy.snippets.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.layout.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class ColumnLayoutTest {
	public static void main (String[] args) {
		final Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setText("Snippet 296");
		shell.setBounds (10, 10, 300, 300);
		final ScrolledComposite sc = new ScrolledComposite (shell, SWT.VERTICAL);
		sc.setBounds (10, 10, 280, 200);
		final int clientWidth = sc.getClientArea ().width;

		final Tree tree = new Tree (sc, SWT.NONE);
		for (int i = 0; i < 99; i++) {
			TreeItem item = new TreeItem (tree, SWT.NONE);
			item.setText ("item " + i);
			new TreeItem (item, SWT.NONE).setText ("child");
		}
		sc.setContent (tree);
		int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
		tree.setSize (clientWidth, prefHeight);
		/*
		 * The following listener ensures that the Tree is always large
		 * enough to not need to show its own vertical scrollbar.
		 */
		tree.addTreeListener (new TreeListener () {
			@Override
			public void treeExpanded (TreeEvent e) {
				int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
				tree.setSize (clientWidth, prefHeight);
			}
			@Override
			public void treeCollapsed (TreeEvent e) {
				int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
				tree.setSize (clientWidth, prefHeight);
			}
		});
		/*
		 * The following listener ensures that a newly-selected item
		 * in the Tree is always visible.
		 *
		 * The following listener scrolls the Tree one item at a time
		 * in response to MouseWheel events.
		 */
		tree.addListener(SWT.MouseWheel, event -> {
			Point origin = sc.getOrigin();
			if (event.count < 0) {
				origin.y = Math.min(origin.y + tree.getItemHeight(), tree.getSize().y);
			} else {
				origin.y = Math.max(origin.y - tree.getItemHeight(), 0);
			}
			sc.setOrigin(origin);
		});

		Button downButton = new Button (shell, SWT.PUSH);
		downButton.setBounds (10, 220, 120, 30);
		downButton.setText ("Down 10px");
		downButton.addListener (SWT.Selection, event -> sc.setOrigin (0, sc.getOrigin ().y + 10));
		Button upButton = new Button (shell, SWT.PUSH);
		upButton.setBounds (140, 220, 120, 30);
		upButton.setText ("Up 10px");
		upButton.addListener (SWT.Selection, event -> sc.setOrigin (0, sc.getOrigin ().y - 10));
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
