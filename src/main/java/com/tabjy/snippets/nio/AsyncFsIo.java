package com.tabjy.snippets.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;

public class AsyncFsIo {
	public static final int N = 10;

	public static void main(String[] args) throws IOException {
		List<Handler> handlers = new LinkedList<>();

		for (int i = 0; i < N; i++) {
			Handler handler = new ReadFileHandler("/dev/random");
			handler.handle();
			handlers.add(handler);
		}

		while (true) {
			for (Handler handler : handlers) {
				handler.resume();
			}
		}
	}

	interface Handler {
		void handle();
		void resume();
	}

	static class ReadFileHandler implements Handler {
		private static int COUNTER = 0;

		private int id;
		private String filePath;
		private ReadableByteChannel rbc;

		public ReadFileHandler(String filePath) throws FileNotFoundException {
			id = COUNTER++;
			this.filePath = filePath;
		}

		@Override
		public void handle() {
			try {
				rbc = Channels.newChannel(new FileInputStream(filePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override public void resume() {
			ByteBuffer bb = ByteBuffer.allocate(1024);
			int n = 0;
			try {
				n = rbc.read(bb);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("reader " + id + " reads " + n + "B");
		}

	}
}
