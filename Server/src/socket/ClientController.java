//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ClientController implements Runnable {
    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private SocketServer server;

    public ClientController(SocketServer server, Socket pSock) {
        this.server = server;
        this.sock = pSock;
    }

    public void run() {
        System.err.println("Lancement du traitement de la connexion cliente");
        boolean closeConnexion = false;

        try {
            this.writer = new PrintWriter(this.sock.getOutputStream());
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        while(!this.sock.isClosed()) {
            try {
                this.reader = new BufferedInputStream(this.sock.getInputStream());
                String response = this.read();
                InetSocketAddress remote = (InetSocketAddress)this.sock.getRemoteSocketAddress();
                String toSend = response;
                if(response.equalsIgnoreCase("CLOSE")) {
                    toSend = "Communication terminée";
                    closeConnexion = true;
                }

                this.write(toSend);
                if(closeConnexion) {
                    System.err.println("COMMANDE CLOSE DETECTEE ! ");
                    this.writer = null;
                    this.reader = null;
                    this.sock.close();
                    break;
                }
            } catch (SocketException var6) {
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                this.closeConnexion();
                break;
            } catch (IOException var7) {
                this.closeConnexion();
            }
        }

    }

    public void write(String toWrite) {
        this.writer.write(toWrite);
        this.writer.flush();
    }

    private void closeConnexion() {
        try {
            this.sock.close();
            this.writer.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            this.server.connexionClosed(this);
        }

    }

    private String read() throws IOException {
        String response = "";
        byte[] b = new byte[4096];
        int stream = this.reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}
