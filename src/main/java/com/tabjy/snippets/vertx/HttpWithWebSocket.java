package com.tabjy.snippets.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HttpWithWebSocket {
	public static void main(String[] args) throws IOException {
		Vertx vertx = Vertx.vertx();

		HttpServiceVerticle httpService = new HttpServiceVerticle(8080);

		CompletableFuture<Void> future = new CompletableFuture<>();

		vertx.deployVerticle(httpService, res -> {
			if (res.failed()) {
				res.cause().printStackTrace(System.err);
				return;
			}

			future.completeExceptionally(new Exception("some exception"));

			System.out.println("HttpService deployed");
			future.complete(null);

			httpService.requestHandler(provideRequestHandler(httpService));
			System.out.println("WebService deployed");

//			WebService webService = new WebService(httpService.getHttpServer());
//			vertx.deployVerticle(webService);

//
//			WebSocketService webSocketService = new WebSocketService(httpService.getHttpServer());
//			vertx.deployVerticle(webSocketService);
//			System.out.println("WebSocketService deployed");
		});

		future.join();
		System.out.println("test");

//		WebService webService = new WebService(httpService.getHttpServer());
//		vertx.deployVerticle(webService);
//		System.out.println("WebService deployed");
//
//		WebSocketService webSocketService = new WebSocketService(httpService.getHttpServer());
//		vertx.deployVerticle(webSocketService);
//		System.out.println("WebSocketService deployed");

//		System.out.println("Press enter to exit");
		System.in.read();
		System.out.println("Shutting down...");

		vertx.close();
	}

	public static Handler<HttpServerRequest> provideRequestHandler(HttpServiceVerticle hsv) {
		Router router = Router.router(hsv.getVertx());

		router.route().handler(BodyHandler.create());

		router.get("/").handler(ctx -> ctx.response().end("index page"));

		router.get("/people/:name")
				.handler(ctx -> ctx.response().end("people page: " + ctx.request().getParam("name")));

		return router;
	}

	public static Handler<ServerWebSocket> provideWebSocketHandler() {
		return null;
	}

	public static class HandlerDelegate<T> implements Handler<T> {

		private Handler<T> mHandler;

		@Override
		public final void handle(T event) {
			if (mHandler != null) {
				mHandler.handle(event);
			}
		}

		public HandlerDelegate<T> handler(Handler<T> handler) {
			mHandler = handler;

			return this;
		}
	}

	private static class HttpServiceVerticle extends AbstractVerticle {
		private int mPort;
		private HttpServer mHttpServer;
		private HandlerDelegate<HttpServerRequest> mRequestHandlerDelegate = new HandlerDelegate<>();
		private HandlerDelegate<ServerWebSocket> mWebsocketHandlerDelegate = new HandlerDelegate<>();

		HttpServiceVerticle(int port) {
			mPort = port;
		}

		@Override
		public void start(Future<Void> startFuture) throws Exception {
			System.out.println("HttpService:start()");
			super.start();

			mHttpServer = vertx.createHttpServer().requestHandler(mRequestHandlerDelegate)
					.websocketHandler(mWebsocketHandlerDelegate).listen(mPort, res -> {
						if (res.failed()) {
							res.cause().printStackTrace(System.err);
							return;
						}

						startFuture.complete();
					});
		}

		@Override
		public void stop(Future<Void> stopFuture) throws Exception {
			System.out.println("HttpService:stop()");
			if (mHttpServer != null) {
				mHttpServer.close(res -> {
					if (res.failed()) {
						res.cause().printStackTrace(System.err);
						return;
					}

					stopFuture.complete();
				});
			}

			super.stop();
		}

		public HttpServiceVerticle requestHandler(Handler<HttpServerRequest> handler) {
			mRequestHandlerDelegate.handler(handler);
			return this;
		}

		public HttpServiceVerticle websocketHandler(Handler<ServerWebSocket> handler) {
			mWebsocketHandlerDelegate.handler(handler);
			return this;
		}
	}

//	private static class WebService extends AbstractVerticle {
//		HttpServer mHttpServer;
//
//		WebService(HttpServer server) {
//			mHttpServer = server;
//		}
//
//		@Override
//		public void start() throws Exception {
//			System.out.println("WebService:start()");
//			super.start();
//
//			mHttpServer.requestHandler(req -> req.response().end("Hello, world!"));
//		}
//
//		@Override
//		public void stop() throws Exception {
//			System.out.println("WebService:stop()");
//			mHttpServer.requestHandler(null);
//
//			super.stop();
//		}
//	}
//
//	private static class WebSocketService extends AbstractVerticle {
//		HttpServer mHttpServer;
//
//		WebSocketService(HttpServer server) {
//			mHttpServer = server;
//		}
//
//		@Override
//		public void start() throws Exception {
//			System.out.println("WebSocketService:start()");
//
//			super.start();
//
//			mHttpServer.websocketHandler(ws -> ws.handler(event -> {
//				ws.writeBinaryMessage(Buffer.buffer("Hello, world!"));
//			}));
//		}
//
//		@Override
//		public void stop() throws Exception {
//			System.out.println("WebSocketService:stop()");
//
//			mHttpServer.websocketHandler(null);
//
//			super.stop();
//		}
//	}
}
