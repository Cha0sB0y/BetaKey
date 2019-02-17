package de.luuuuuis.http;

import com.sun.net.httpserver.HttpExchange;
import de.luuuuuis.MojangUUIDResolve;
import de.luuuuuis.MySQL.KeyInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class VerifyContextHandler extends LuisHandler {

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> request = queryToMap(exchange.getRequestURI().getQuery());

        try {
            String response = readSite("verify.html").replace("%servername%", "BetaKey");

            if (request.containsKey("username") && request.containsKey("key")) {
                String username = request.get("username");
                String key = request.get("key");

                if (username.length() > 16 || key.length() > 36) {
                    return;
                }

                /*
                 * Verify
                 */
                KeyInfo keyInfo = new KeyInfo();

                String uuid = MojangUUIDResolve.getUUIDResult(username).getValue();
                if (!uuid.equals("null")) {
                    if (!keyInfo.alreadyAdded(uuid)) {
                        if (keyInfo.keyIsValid(key)) {

                            if (!key.endsWith("t-P") && !(key.getBytes().length > 36))
                                keyInfo.deleteKey(key);

                            keyInfo.addPlayer(uuid, username);

                            response = response.replace("%script%", "<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>\r\n" +
                                    "\r\n" +
                                    "<script type=\"text/javascript\">\r\n" +
                                    "\r\n" +
                                    "	$(window).ready(function () {\r\n" +
                                    "		swal({\r\n" +
                                    "			  title: \"Welcome, " + username + "!\",\r\n" +
                                    "             text: \"You can now access our Minecraft server.\", \r\n" +
                                    "			  icon: \"success\",\r\n" +
                                    "			});\r\n" +
                                    "	history.pushState({}, \"/verify\", window.location.pathname);\r\n" +
                                    "	});\r\n" +
                                    "\r\n" +
                                    "</script>");


                            response(200, response.getBytes(), exchange);
                            return;
                        } else {
                            response = response.replace("%script%", "<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>\r\n" +
                                    "\r\n" +
                                    "<script type=\"text/javascript\">\r\n" +
                                    "\r\n" +
                                    "	$(window).ready(function () {\r\n" +
                                    "		swal({\r\n" +
                                    "			  title: \"Something went wrong! :(\",\r\n" +
                                    "             text: \"This key is not valid. Try another!\", \r\n" +
                                    "			  icon: \"error\",\r\n" +
                                    "			});\r\n" +
                                    "	history.pushState({}, \"/verify\", window.location.pathname);\r\n" +
                                    "	});\r\n" +
                                    "\r\n" +
                                    "</script>");


                            response(200, response.getBytes(), exchange);
                            return;
                        }


                    } else {
                        response = response.replace("%script%", "<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>\r\n" +
                                "\r\n" +
                                "<script type=\"text/javascript\">\r\n" +
                                "\r\n" +
                                "	$(window).ready(function () {\r\n" +
                                "		swal({\r\n" +
                                "			  title: \"You are already verified! :(\",\r\n" +
                                "             text: \"Please enter another username!\", \r\n" +
                                "			  icon: \"success\",\r\n" +
                                "			});\r\n" +
                                "	history.pushState({}, \"/verify\", window.location.pathname);\r\n" +
                                "	});\r\n" +
                                "\r\n" +
                                "</script>");


                        response(200, response.getBytes(), exchange);
                        return;
                    }

                } else {
                    response = response.replace("%script%", "<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>\r\n" +
                            "\r\n" +
                            "<script type=\"text/javascript\">\r\n" +
                            "\r\n" +
                            "	$(window).ready(function () {\r\n" +
                            "		swal({\r\n" +
                            "			  title: \"Something went wrong! :(\",\r\n" +
                            "             text: \"Try again!\", \r\n" +
                            "			  icon: \"error\",\r\n" +
                            "			});\r\n" +
                            "	history.pushState({}, \"/verify\", window.location.pathname);\r\n" +
                            "	});\r\n" +
                            "\r\n" +
                            "</script>");


                    response(200, response.getBytes(), exchange);
                    return;
                }

            } else {
                response = response.replace("%script%", "");

                exchange.getResponseHeaders().add("Location", "/verify");
                exchange.getResponseHeaders().put("Context-Type", Collections.singletonList("text/plain; charset=UTF-8"));
                response(200, response.getBytes(), exchange);
                return;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
