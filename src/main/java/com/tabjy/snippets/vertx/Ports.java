package com.tabjy.snippets.vertx;

import io.vertx.core.Vertx;

class Ports {
	public static void main(String[] args) {
		System.out.println(Vertx.vertx().createHttpServer().actualPort());
	}
}