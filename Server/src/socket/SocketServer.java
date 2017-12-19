//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SocketServer {
    private int port = 2345;
    private String host = "127.0.0.1";
    private ServerSocket server = null;
    private boolean isRunning;
    private Object lock;
    private List<ClientController> clientList;

    public SocketServer() {
        this.init();
    }

    public SocketServer(String pHost, int pPort) {
        this.host = pHost;
        this.port = pPort;
        this.init();
    }

    private void init() {
        this.clientList = new ArrayList();
        this.lock = new Object();

        try {
            this.server = new ServerSocket(this.port);
        } catch (UnknownHostException var2) {
            var2.printStackTrace();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    private boolean isRunning() {
        Object var1 = this.lock;
        synchronized(this.lock) {
            return this.isRunning;
        }
    }

    private void setRunning(boolean value) {
        Object var2 = this.lock;
        synchronized(this.lock) {
            this.isRunning = value;
        }
    }

    public void open() {
        this.setRunning(true);
        Thread t = new Thread(new Runnable() {
            public void run() {
                while(SocketServer.this.isRunning()) {
                    try {
                        Socket clientSocket = SocketServer.this.server.accept();
                        System.out.println("Connexion cliente reçue.");
                        ClientController c = new ClientController(SocketServer.this, clientSocket);
                        SocketServer.this.clientList.add(c);
                        Thread t = new Thread(c);
                        t.start();
                    } catch (IOException var4) {
                        var4.printStackTrace();
                    }
                }

                try {
                    SocketServer.this.server.close();
                } catch (IOException var5) {
                    var5.printStackTrace();
                    SocketServer.this.server = null;
                }

            }
        });
        t.start();
    }

    public void close() {
        this.setRunning(false);
    }

    public void broadcast(String message) {
        Iterator var2 = this.clientList.iterator();

        while(var2.hasNext()) {
            ClientController controller = (ClientController)var2.next();
            (new Thread(() -> {
                controller.write(message);
                System.out.println("message envoyé");
            })).start();
        }

    }

    public void connexionClosed(ClientController clientController) {
        this.clientList.remove(clientController);
    }
}
