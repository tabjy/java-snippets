package com.tabjy.snippets.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPEcho {
	public static void main(String[] args) {
		// server
		new Thread(() -> {
			try {
				runServer("0.0.0.0", 8080);
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}).start();

		// client
//		new Thread(() -> {
//			try {
//				Thread.sleep(1000);
//				runClient("localhost", 8080, 10);
//			} catch (IOException | InterruptedException e) {
//				e.printStackTrace(System.err);
//			}
//		}).start();
	}

	private static void runServer (String hostname, int port) throws IOException {
		Selector selector = Selector.open();
		InetSocketAddress addr = new InetSocketAddress(hostname, port);
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		serverChannel.socket().bind(addr);
		{
			System.out.println("[server] running");
		}
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		Map<SocketChannel, Queue<ByteBuffer>> request = new HashMap<>();

		while (true) {
			selector.select();
			for (SelectionKey key : selector.selectedKeys()) {
				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
					{
						System.out.println("[server] accepted connection from: " + channel.getRemoteAddress());
					}
					channel.configureBlocking(false);

					request.put(channel, new LinkedBlockingQueue<>());

					channel.register(selector, SelectionKey.OP_READ);
					continue;
				}

				if (key.isReadable()) {
					SocketChannel channel = ((SocketChannel) key.channel());
					ByteBuffer b = ByteBuffer.allocate(1024);

					int n = channel.read(b);
					if (n == -1) {
						request.remove(channel);
						channel.close();
						key.cancel();
						continue;
					}

					b.limit(n);
					{
						ByteBuffer b2 = b.duplicate();
						b2.flip();
						Charset cs = Charset.forName("UTF-8");
						System.out.println("[server] read: " + cs.decode(b2).toString());
					}
					request.get(channel).add(b);

					channel.register(selector, SelectionKey.OP_WRITE);
					continue;
				}

				if (key.isWritable()) {
					SocketChannel channel = ((SocketChannel) key.channel());
					if (request.get(channel).peek() == null) {
						continue;
					}

					ByteBuffer b = request.get(channel).remove();
					b.flip();
					channel.write(b);
					{
						ByteBuffer b2 = b.duplicate();
						b2.flip();
						Charset cs = Charset.forName("UTF-8");
						System.out.println("[client] wrote: " + cs.decode(b2).toString());
					}

					channel.register(selector, SelectionKey.OP_READ);
					continue;
				}
			}
			selector.selectedKeys().clear();
		}
	}

	private static void runClient(String hostname, int port, int num) throws IOException {
		Selector selector = Selector.open();
		InetSocketAddress addr = new InetSocketAddress(hostname, port);

		for (int i = 0; i < num; i++) {
			SocketChannel clientChannel = SocketChannel.open();
			clientChannel.configureBlocking(false);
			clientChannel.register(selector,SelectionKey.OP_CONNECT);
		}

		System.out.println("[client] created " + num + " request connections");

		while (true) {
			selector.select();
			for (SelectionKey key : selector.selectedKeys()) {
				if (!key.isValid()) {
					continue;
				}

				if (key.isConnectable()) {
					SocketChannel channel = ((SocketChannel) key.channel());
					channel.connect(addr);
					channel.finishConnect();
					channel.register(selector, SelectionKey.OP_WRITE);
					continue;
				}

				if (key.isWritable()) {
					SocketChannel channel = ((SocketChannel) key.channel());

					Charset cs = Charset.forName("UTF-8");
					ByteBuffer b = cs.encode(CharBuffer.wrap("hello!".toCharArray()));

					channel.write(b);
					{
						ByteBuffer b2 = b.duplicate();
						b2.flip();
						System.out.println("[client] wrote: " + cs.decode(b2).toString());
					}

					channel.register(selector, SelectionKey.OP_READ);
					continue;
				}

				if (key.isReadable()) {
					SocketChannel channel = ((SocketChannel) key.channel());
					ByteBuffer b = ByteBuffer.allocate(1024);

					int n = channel.read(b);
					if (n == -1) {
						channel.close();
						key.cancel();
						continue;
					}

					b.limit(n);
					{
						ByteBuffer b2 = b.duplicate();
						b2.flip();
						Charset cs = Charset.forName("UTF-8");
						System.out.println("[client] read: " + cs.decode(b2).toString());
					}

					channel.close();
					key.cancel();
					continue;
				}
			}
			selector.selectedKeys().clear();
		}
	}
}
