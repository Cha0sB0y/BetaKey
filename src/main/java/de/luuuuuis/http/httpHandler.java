package de.luuuuuis.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class httpHandler {

	private HttpServer server;
	
	public httpHandler() {
		
		try {
			server = HttpServer.create(new InetSocketAddress(187), 0);
			System.out.println("BetaKey >> WebInterface started!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.createContext("/", new LuisHandler() {
			
			@Override
			public void handle(HttpExchange arg0) throws IOException {
				if(arg0.getRequestURI() == null || arg0.getRequestURI().toString().isEmpty() || arg0.getRequestURI().toString().equalsIgnoreCase("/")) {
					arg0.getResponseHeaders().add("Location", "/betakey");
					response(302, "If you see this you are totaly wrong here!".getBytes(), arg0);
				}
			}
		});
		server.createContext("/verify", new VerifyContextHandler());
		
		server.setExecutor(null);
		
		server.start();
	}
}
