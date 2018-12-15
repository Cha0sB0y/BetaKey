package de.luuuuuis.http;

import com.google.common.io.ByteStreams;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class AssetsHandler extends LuisHandler {

    @Override
    public void handle(HttpExchange arg0) throws IOException {
        Map<String, String> req = queryToMap(arg0.getRequestURI().getQuery());
        if (req.containsKey("id")) {
            String id = req.get("id");
            if (id.equals("468739.jpg") || id.equals("favicon.ico")) {
                try {
                    String url = "/Website/" + id;

                    InputStream in = HttpHandler.class.getResourceAsStream(url);
                    byte[] bytes = ByteStreams.toByteArray(in);
                    response(200, bytes, arg0);
                } catch (Exception ex) {
                    response(200, "error".getBytes(), arg0);
                    ex.printStackTrace();
                }
            } else {
                response(200, "illegal request".getBytes(), arg0);
            }
        } else {
            response(200, "illegal request".getBytes(), arg0);
        }
    }

}
