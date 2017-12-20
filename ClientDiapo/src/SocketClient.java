//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public SocketClient() {
    }

    public static void main(String[] args) {
        String hostName = "35.176.120.18";
        short portNumber = 8000;
        boolean isFullScreen = false;
        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            Throwable var4 = null;

            try {
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                Throwable var6 = null;

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                    Throwable var8 = null;

                    try {
                        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                        Throwable var10 = null;

                        try {
                            try {
                                while(true) {
                                    String message = in.readLine();
                                    if(message != null){
                                        System.out.println(message);
                                        if(message.contains("LONG")){
                                            System.out.println("long click");
                                            if(!isFullScreen) {
                                                Runtime.getRuntime().exec("notify-send \'Mise en plein écran\'\n");
                                                Runtime.getRuntime().exec("xdotool key Ctrl+F5\n");
                                                try{
                                                    Thread.sleep(500);
                                                } catch(Exception e){}
                                                Runtime.getRuntime().exec("xdotool key F11\n");
                                                isFullScreen = true;
                                            }else {
                                                Runtime.getRuntime().exec("notify-send \'Fin du plein écran\'\n");
                                                Runtime.getRuntime().exec("xdotool key Escape\n");
                                                isFullScreen = false;
                                            }
                                        }
                                        else if(message.contains("DOUBLE")){
                                            System.out.println("Double click");
                                            Runtime.getRuntime().exec("notify-send \'Diapo précédente\'\n");
                                            Runtime.getRuntime().exec("xdotool key Left");
                                        }

                                        else if(message.contains("SINGLE")){
                                            System.out.println("single click");
                                            Runtime.getRuntime().exec("notify-send \'Diapo suivante\'\n");
                                            Runtime.getRuntime().exec("xdotool key Right");
                                        }

                                    }
                                }
                            } catch (Throwable var90) {
                                var10 = var90;
                                throw var90;
                            }
                        } finally {
                            if(stdIn != null) {
                                if(var10 != null) {
                                    try {
                                        stdIn.close();
                                    } catch (Throwable var89) {
                                        var10.addSuppressed(var89);
                                    }
                                } else {
                                    stdIn.close();
                                }
                            }

                        }
                    } catch (Throwable var92) {
                        var8 = var92;
                        throw var92;
                    } finally {
                        if(in != null) {
                            if(var8 != null) {
                                try {
                                    in.close();
                                } catch (Throwable var88) {
                                    var8.addSuppressed(var88);
                                }
                            } else {
                                in.close();
                            }
                        }

                    }
                } catch (Throwable var94) {
                    var6 = var94;
                    throw var94;
                } finally {
                    if(out != null) {
                        if(var6 != null) {
                            try {
                                out.close();
                            } catch (Throwable var87) {
                                var6.addSuppressed(var87);
                            }
                        } else {
                            out.close();
                        }
                    }

                }
            } catch (Throwable var96) {
                var4 = var96;
                throw var96;
            } finally {
                if(echoSocket != null) {
                    if(var4 != null) {
                        try {
                            echoSocket.close();
                        } catch (Throwable var86) {
                            var4.addSuppressed(var86);
                        }
                    } else {
                        echoSocket.close();
                    }
                }

            }
        } catch (UnknownHostException var98) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException var99) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }

    }
}
