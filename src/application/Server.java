package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static javafx.application.Platform.exit;

public class Server{

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(12344);
        System.out.println("Wait connect...");
        while (true) {
            Socket s1 = server.accept();
            PrintWriter out1 = new PrintWriter(s1.getOutputStream());
            System.out.println("One player is coming...");
            out1.println(1);
            out1.flush();
            out1.println("Waiting another Player...");
            out1.flush();
            Socket s2 = server.accept();
            PrintWriter out2 = new PrintWriter(s2.getOutputStream());
            System.out.println("The game is start.");
            out1.println("The game is start.");
            out1.flush();
            out2.println(-1);
            out2.flush();
            out2.println("The game is start.");
            out2.flush();
            new Thread(new SocketThread(s1, s2)).start();
            break;
        }
    }
}
