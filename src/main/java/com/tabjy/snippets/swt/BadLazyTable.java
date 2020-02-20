package com.tabjy.snippets.swt;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openjdk.jmc.ui.misc.DisplayToolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class BadLazyTable {
	Display display = new Display();
	Shell shell = new Shell(display);
	TableViewer tableViewer = new TableViewer(shell, SWT.VIRTUAL);
	MyContentProvider contentProvider = new MyContentProvider();

	Comparator<ObjectModel> sortingComparator = Comparator.comparingInt(o -> o.id);
	int sortingDirection = -1;

	static class ObjectModel {
		static int COUNTER = 0;

		int id = COUNTER++;
		String name = toString();
		int hash = hashCode();
		int rand = (int) (Math.random() * 100000f);
	}

	class MyContentProvider implements ILazyContentProvider {

		ObjectModel[] models = new ObjectModel[0];
		ObjectModel[] rawInput;

		@Override
		public void updateElement(int index) {
			if (index >= models.length) {
				return;
			}
			tableViewer.replace(models[index], index);
		}

		@Override
		public void dispose() {
			// no op
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			models = (ObjectModel[]) newInput;
		}

		void sort() {
			if (sortingComparator != null) {
				Arrays.sort(models, (o1, o2) -> sortingComparator.compare(o1, o2) * sortingDirection);
			}
			tableViewer.setInput(models);
		}

		void setInput(ObjectModel[] input) {
			rawInput = input;

			ObjectModel selected = null;
			if (tableViewer.getTable().getSelection().length > 0) {
				selected = (ObjectModel) tableViewer.getTable().getSelection()[0].getData();
			}

			models = Arrays.copyOf(input, input.length);
			sort();
			tableViewer.setItemCount(models.length);

			if (selected == null) {
				return;
			}

			int index = Arrays.asList(models).indexOf(selected);
			if (index == -1) {
				tableViewer.getTable().deselectAll();
				return;
			}
			tableViewer.getTable().setSelection(index);
		}

		ObjectModel[] getInput() {
			return rawInput;
		}
	}

	public static void main(String[] args) {
		new BadLazyTable().run();
	}

	ObjectModel[] makeModels(int count) {
		ObjectModel[] models = new ObjectModel[count];

		for (int i = 0; i < count; i++) {
			models[i] = new ObjectModel();
		}

		return models;
	}

	void refreshData(int size, boolean preserve) {
		List<ObjectModel> data = new ArrayList<>();
		if (preserve && contentProvider.getInput() != null) {
			data.addAll(Arrays.asList(contentProvider.getInput()));
		}

		if (size < data.size()) {
			data = data.subList(0, size);
		} else if (size > data.size()) {
			data.addAll(Arrays.asList(makeModels(size - data.size())));
		}

		contentProvider.setInput(data.toArray(new ObjectModel[0]));
	}

	TableViewerColumn makeColumn(
			String label, Function<ObjectModel, String> labelProvider, Comparator<ObjectModel> comparator) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.BORDER);
		tableViewerColumn.getColumn().setWidth(200);
		tableViewerColumn.getColumn().setText(label);

		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return labelProvider.apply((ObjectModel) element);
			}
		});

		tableViewerColumn.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (sortingComparator == comparator) {
					sortingDirection *= -1;
				} else {
					sortingComparator = comparator;
					sortingDirection = 1;
				}

				tableViewer.getTable().setSortColumn(tableViewerColumn.getColumn());
				tableViewer.getTable().setSortDirection(sortingDirection == 1 ? SWT.UP : SWT.DOWN);
				contentProvider.sort();
			}
		});

		return tableViewerColumn;
	}

	void run() {
		shell.setLayout(new FillLayout());
		shell.setText(BadLazyTable.class.getSimpleName());

		tableViewer.setContentProvider(contentProvider);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		TableViewerColumn sortingColumn = makeColumn("id", objectModel -> objectModel.id + "", sortingComparator);
		makeColumn("name", objectModel -> objectModel.name, Comparator.comparing(o -> o.name));
		makeColumn("hash", objectModel -> objectModel.hash + "", Comparator.comparingInt(o -> o.hash));
		makeColumn("rand", objectModel -> objectModel.rand + "", Comparator.comparingInt(o -> o.rand));

		tableViewer.getTable().setSortColumn(sortingColumn.getColumn());
		tableViewer.getTable().setSortDirection(SWT.DOWN);

		tableViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ObjectModel model = (ObjectModel) e.item.getData();
				System.err.printf("Selected: %d, %s, %d, %d\n", model.id, model.name, model.hash, model.rand);
			}
		});

		new Thread(() -> {
			Scanner kbd = new Scanner(System.in);
			while (true) {
				System.out.print("number of rows: ");
				int size = kbd.nextInt();

				DisplayToolkit.inDisplayThread().execute(() -> refreshData(size, true));
			}
		}).start();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
