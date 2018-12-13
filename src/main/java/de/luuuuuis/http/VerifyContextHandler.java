package de.luuuuuis.http;

import com.sun.net.httpserver.HttpExchange;
import de.luuuuuis.MojangUUIDResolve;
import de.luuuuuis.SQL.KeyInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class VerifyContextHandler extends LuisHandler {

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> request = queryToMap(exchange.getRequestURI().toString());

        String response;

        if (request.containsKey("username") && request.containsKey("key")) {
            String username = request.get("username");
            String key = request.get("key");

            System.out.println("login from " + exchange.getRemoteAddress().getAddress().getHostAddress() + " -> " + username + "/" + key);

            /**
             * Login
             */
            KeyInfo keyInfo = new KeyInfo();

            String pUUID = MojangUUIDResolve.getUUIDResult(username).getValue();
            if (!pUUID.equals("null")) {
                if (!keyInfo.alreadyAdded(pUUID)) {
                    if (keyInfo.keyIsValid(key)) {


                        if (!key.endsWith("t-P") && !(key.getBytes().length > 36))
                            keyInfo.deleteKey(key);


                        keyInfo.getAllowedList().add(pUUID);
                        keyInfo.addPlayer(pUUID);

                        response = "success=all right";

                        exchange.getResponseHeaders().add("Location", "/verify?verified");
                        exchange.getResponseHeaders().put("Context-Type", Collections.singletonList("text/plain; charset=UTF-8"));
                        try {
                            response(302, response.getBytes(), exchange);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        return;
                    } else {
                        response = "error=wrong key";

                        exchange.getResponseHeaders().add("Location", "/verify?wrongkey");
                        exchange.getResponseHeaders().put("Context-Type", Collections.singletonList("text/plain; charset=UTF-8"));
                        try {
                            response(302, response.getBytes(), exchange);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        return;
                    }


                } else {
                    response = "error=already verified";

                    exchange.getResponseHeaders().add("Location", "/verify?alreadyverified");
                    exchange.getResponseHeaders().put("Context-Type", Collections.singletonList("text/plain; charset=UTF-8"));
                    try {
                        response(302, response.getBytes(), exchange);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }

            } else {
                response = "error=wrong username";
                exchange.getResponseHeaders().add("Location", "/verify?wrongusername");
                exchange.getResponseHeaders().put("Context-Type", Collections.singletonList("text/plain; charset=UTF-8"));
                try {
                    response(302, response.getBytes(), exchange);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }

        } else {
            response = "error=bad request";

            exchange.getResponseHeaders().add("Location", "/verify?badrequest");
            exchange.getResponseHeaders().put("Context-Type", Collections.singletonList("text/plain; charset=UTF-8"));
            try {
                response(302, response.getBytes(), exchange);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

    }
}
