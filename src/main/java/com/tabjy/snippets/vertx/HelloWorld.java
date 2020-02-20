package com.tabjy.snippets.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.io.IOException;

public class HelloWorld extends AbstractVerticle {

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) throws IOException {
		Vertx vertx = Vertx.vertx();
//		vertx.deployVerticle(new HelloWorld());
//		new HelloWorld().init(vertx, vertx.getOrCreateContext());
		vertx.deployVerticle(new HelloWorld());

		System.out.println("Press enter to exit");
		System.in.read();
		System.out.println("Shutting down...");

		vertx.close();
	}

	@Override
	public void start() {
		vertx.createHttpServer().websocketHandler(ws -> ws.handler(ws::writeBinaryMessage)).requestHandler(req -> {
			if (req.uri().equals("/")) req.response().end("Hello, world!");
		}).listen(8080);
	}
}