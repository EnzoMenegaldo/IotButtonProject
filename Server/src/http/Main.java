package http;

import socket.SocketServer;

import java.io.IOException;

public class Main {
        public Main() {
        }

        public static void main(String[] args) {
            SocketServer socketServer = new SocketServer("35.176.120.18", 8000);
            socketServer.open();

            try {
                new SimpleHttpServer(socketServer);
            } catch (IOException var3) {
                var3.printStackTrace();
            }

        }
}
