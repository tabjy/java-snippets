package com.tabjy.snippets.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class WebSocket extends AbstractVerticle {
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new WebSocket());
	}
	
	public void start() {
		vertx.createHttpServer().websocketHandler(ws -> {
			ws.accept();
			ws.textMessageHandler(msg -> {
				ws.writeTextMessage(msg, res -> {
					ws.close(res2 -> {
//						System.exit(0);
						ws.writeTextMessage("test", res3 -> {
							res3.cause().printStackTrace();
						});
					});	
				});
				
			});
		}).listen(1234);
	}
}
