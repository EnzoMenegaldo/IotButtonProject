//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import socket.SocketServer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class SimpleHttpServer {
    SocketServer socketServer;
    HttpServer server;
    Map<String, HttpHandler> handlers;

    public SimpleHttpServer(SocketServer socketServer) throws IOException {
        this.socketServer = socketServer;
        this.handlers = new HashMap();
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        this.server.createContext("/", (t) -> {
            this.handleRequest(t);
        });
        this.server.setExecutor((Executor)null);
        this.server.start();
        this.handlers.put("SubscriptionConfirmation", new SimpleHttpServer.HandlerSubscription());
        this.handlers.put("Notification", new SimpleHttpServer.HandlerNotif());
    }

    public void handleRequest(HttpExchange t) throws IOException {
        Headers headers = t.getRequestHeaders();
        if(headers.containsKey("x-amz-sns-message-type")) {
            String messageType = headers.getFirst("x-amz-sns-message-type");
            ((HttpHandler)this.handlers.getOrDefault(messageType, (exchange) -> {
                System.out.println("NO HANDLER FOR MESSAGES OF TYPE \"" + messageType + "\";");
            })).handle(t);
        }

    }

    private static JSONObject getBody(HttpExchange t) {
        InputStream st = t.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(st));
        StringBuilder response = new StringBuilder();

        String line;
        try {
            while((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        String text = response.toString();
        JSONParser parser = new JSONParser();
        Object ret = null;

        try {
            ret = parser.parse(text);
            return ret instanceof JSONObject?(JSONObject)ret:null;
        } catch (ParseException var9) {
            return null;
        }
    }

    //Permet de traiter un message de type notification envoyé par le service AWS SNS
    private class HandlerNotif implements HttpHandler {
        private HandlerNotif() {
        }

        public void handle(HttpExchange httpExchange) throws IOException {
            JSONObject body = SimpleHttpServer.getBody(httpExchange);
            if (body != null) {
                String message = body.get("Message").toString();
                System.out.println(message);

                //Obligatoire
                //Envoie 200 comme réponse à la requête sinon AWS SNS considère que la requête n'a pas réussi.
                //Si pas de réponse, AWS SNS renvoie jusqu'à 3 fois la requête.
                httpExchange.sendResponseHeaders(200,message.length());

                SimpleHttpServer.this.socketServer.broadcast(message);
            }
        }
    }

    //Permet de traiter un message de type souscription envoyé par le service AWS SNS lors de la création du topic
    private class HandlerSubscription implements HttpHandler {
        private HandlerSubscription() {
        }

        public void handle(HttpExchange httpExchange) throws IOException {
            JSONObject body = SimpleHttpServer.getBody(httpExchange);
            //Obligatoire
            //Envoie d'une requête Get à l'adresse définie dans le champs "SubcribeURL" permettant de valider la souscription
            if(body != null) {
                String targetURL = body.get("SubscribeURL").toString();
                System.out.println(targetURL);
                HttpURLConnection connection = null;
                SimpleHttpServer.this.socketServer.broadcast("Sub confirm received. URL=\"" + targetURL + "\"");

                try {
                    URL url = new URL(targetURL);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null){
                        System.out.println(line);
                    }

                    SimpleHttpServer.this.socketServer.broadcast("Sub confirm http request send.");
                } catch (Exception var12) {
                    var12.printStackTrace();
                } finally {
                    if(connection != null) {
                        connection.disconnect();
                    }

                }

            }
        }
    }
}

