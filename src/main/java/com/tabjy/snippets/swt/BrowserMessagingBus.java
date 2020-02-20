package com.tabjy.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BrowserMessagingBus {
	private static String PAGE_HTML;
	private static String IN_PAGE_CONSOLE_SCRIPT;

	static {
		try {
			PAGE_HTML = getResourceAsString("index.html", StandardCharsets.UTF_8);
			IN_PAGE_CONSOLE_SCRIPT = getResourceAsString("in-page-console.js", StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Browser browser = new Browser(shell, SWT.NONE);
		browser.setText(PAGE_HTML);

		MessagingBus ms = new MessagingBus(browser);

		browser.addProgressListener(new ProgressAdapter() {
			@Override
			public void completed(ProgressEvent event) {
				browser.removeProgressListener(this);
				browser.execute(IN_PAGE_CONSOLE_SCRIPT);
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static String getResourceAsString(String name, Charset charset) throws IOException {
		InputStream in = BrowserMessagingBus.class.getResourceAsStream(name);
		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			buf.write((byte) result);
			result = bis.read();
		}
		return buf.toString(charset);
	}

	public static class MessagingBus extends BrowserFunction {
		private int messageId = 0;

		public MessagingBus(Browser browser) {
			super(browser, "__messageBusPostRaw");
		}

		public MessagingBus(Browser browser, boolean top, String[] frameNames) {
			super(browser, "__messageBusPostRaw", top, frameNames);
		}

		@Override
		public Object function(Object[] arguments) {
			String channel = (String) arguments[0];
			return arguments;
		}

		private int generateMessageId() {
			return messageId++;
		}

		public void addMessageListener(String channel, MessageListener listner) {
			
		}

		public void removeMessageListener(String channel, MessageListener listener) {
			
		}

		public void message(String channel, Object... payload) {
			
		}

		public void setInvocationHandler(String channel) {
			
		}

		public void removeInvocationHandler(String channel) {
			
		}

		public Object involk(String channel, Object... arguments) {
			return null;
		}
	}
	
	public interface MessageListener {
		void onMessage(String channel, Object... payload);
	}
	
	public interface InvocationHandler {
		Object onInvoke(String channel, Object... payload);
	}
}
