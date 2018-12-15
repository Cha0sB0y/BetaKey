package de.luuuuuis.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class HttpHandler {

    private HttpServer server;

    String[] rndString = {"/verify", "/assets"};

    public HttpHandler() {

        try {
            server = HttpServer.create(new InetSocketAddress(187), 0);
            System.out.println("BetaKey >> Web server started!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/", new LuisHandler() {

            @Override
            public void handle(HttpExchange arg0) throws IOException {
                if (arg0.getRequestURI() == null || arg0.getRequestURI().toString().isEmpty()
                        || arg0.getRequestURI().toString().equalsIgnoreCase("/")) {
                    arg0.getResponseHeaders().add("Location", "/verify");
                    response(302, "If you see this you are totally wrong here!".getBytes(), arg0);
                } else {
                    String q = arg0.getRequestURI().toString();
                    for (String s : rndString) {
                        if (q.toLowerCase().startsWith(s.toLowerCase())) {
                            return;
                        }
                    }

                    arg0.getResponseHeaders().add("Location", "/overview");
                    response(302, "If you see this you are totally wrong here!".getBytes(), arg0);

                }
            }
        });
        server.createContext("/verify", new VerifyContextHandler());
        server.createContext("/assets", new AssetsHandler());

        server.setExecutor(null);

        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
