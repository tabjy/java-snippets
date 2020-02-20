package com.tabjy.snippets.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StaticHandlerExample {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		
		vertx.deployVerticle(new StaticServerVerticle());
	}
	
	public static class StaticServerVerticle extends AbstractVerticle {
		@Override
		public void start() throws Exception {
			super.start();

			Router router = Router.router(vertx);
			router.route("/*").handler(StaticHandler.create());
			
			vertx.createHttpServer().requestHandler(router).listen(8080);
		}
	}
}
