package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Test {

	public static void main( String[] args ) {
		Display display = new Display();
		Shell shell = new Shell( display );
		shell.setLayout( new FillLayout() );
		Table table = new Table( shell, SWT.NONE );
		TableColumn column = new TableColumn( table, 0 );
		column.setWidth( 100 );
		for( int i = 0; i < 10; i++ ) {
			TableItem item = new TableItem( table, SWT.NONE );
			item.setText( "item " + i );
//			if( i % 2 == 0 ) {
//				item.setBackground( display.getSystemColor( SWT.COLOR_GREEN ) );
//			}
		}
		final Button button = new Button( table, SWT.RADIO );
		button.setText( "radio" );
		table.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent event ) {
				TableItem item = ( TableItem )event.item;
				TableEditor editor = new TableEditor( item.getParent() );
				button.setText( item.getText() );
				button.setBackground( item.getBackground() );
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				editor.setEditor( button, item, 0 );
				editor.layout();
			}
		} );
		shell.pack();
		shell.open();
		while( !shell.isDisposed() ) {
			if( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
