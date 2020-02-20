package com.tabjy.snippets.nio;

import com.tabjy.snippets.common.Util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public class Server {
	public enum State {
		RUNNING, STOPPED
	}

	private State mState = State.STOPPED;
	private InetSocketAddress mAddress;
	private Selector mSelector;
	private ServerSocketChannel mServerSocketChannel;
	private Thread mThread;

	private Set<ServerListener> mListeners = new HashSet<>();

	public Server(String hostname, int port) {
		mAddress = new InetSocketAddress(hostname, port);
	}

	public Server(int port) {
		this("0.0.0.0", port);
	}

	public void start() throws IOException {
		if (mState == State.RUNNING) {
			throw new RuntimeException("server is started"); // TODO: better exception type
		}

		mSelector = Selector.open();
		mServerSocketChannel = ServerSocketChannel.open();
		mServerSocketChannel.configureBlocking(false);
		mServerSocketChannel.socket().bind(mAddress);
		mServerSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
		mState = State.RUNNING;

		mThread = new Thread(this::serve);
		mThread.start();
	}

	public void stop() throws IOException {
		if (mState == State.STOPPED) {
			throw new RuntimeException("server is started"); // TODO: better exception type
		}

		mState = State.STOPPED;
		mServerSocketChannel.close();
		mSelector.close();
	}

	private void serve() {
		try {
			while (mState == State.RUNNING) {
				mSelector.select();
				for (SelectionKey key : mSelector.selectedKeys()) {
					if (!key.isValid()) {
						continue;
					}

					if (key.isAcceptable()) {
						SocketChannel channel = null;
						try {
							channel = ((ServerSocketChannel) key.channel()).accept();
							channel.configureBlocking(false);

							channel.register(mSelector, SelectionKey.OP_READ);
						} catch (IOException e) {
							notifyException(e);
							try {
								if (channel != null) {
									channel.close();
								}
							} catch (IOException ex) {
								notifyException(ex);
							}
						}
						continue;
					}

					if (key.isReadable()) {
						SocketChannel channel = ((SocketChannel) key.channel());

						channel.register(mSelector, SelectionKey.OP_WRITE);
						continue;
					}

					if (key.isWritable()) {
						SocketChannel channel = ((SocketChannel) key.channel());

						new Handler(key);

						key.cancel();
						continue;
					}
				}
				mSelector.selectedKeys().clear();
			}
		} catch (IOException e) {
			notifyException(e);
		}
	}

	private void notifyException(Exception e) {
		for (ServerListener listener : mListeners) {
			listener.onException(e);
		}

		try {
			stop();
		} catch (IOException ex) {
			Util.getLogger().severe(e.getLocalizedMessage());
		}
	}

	public InetSocketAddress getAddress() {
		return mAddress;
	}

	public State getState() {
		return mState;
	}

	public boolean addListener(ServerListener listener) {
		return mListeners.add(listener);
	}

	public static class Handler {
		private SelectionKey mKey;
		private SocketChannel mChannel;

		public Handler(SelectionKey key) {
			mKey = key;
			mChannel = (SocketChannel) mKey.channel();
		}

		void begin() {

		}

		void resume() {

		}

		void stop() {

		}
	}

	public abstract static class ServerListener {
		public void onException(Exception e) {
			Util.getLogger().severe(e.getLocalizedMessage());
		}
	}
}
