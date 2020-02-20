package com.tabjy.mymodule;

import java.io.IOException;

public class InstrumentMe {
	public static void main(String[] args) throws IOException {
		new Thread(() -> {
			while(true) {
				workload();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		System.in.read();
		System.exit(0);
	}

	public static void workload() {
		System.out.println("working");
	}
}
