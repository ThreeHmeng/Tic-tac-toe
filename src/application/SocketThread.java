package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static javafx.application.Platform.exit;

public class SocketThread implements Runnable {
    Socket s1;
    Socket s2;
    Scanner in1;
    PrintWriter out1;
    Scanner in2;
    PrintWriter out2;
    public SocketThread(Socket S1, Socket S2) throws IOException {
        s1 = S1;
        s2 = S2;
    }
    @Override
    public void run() {
        try {
            in1 = new Scanner(s1.getInputStream());
            in2 = new Scanner(s2.getInputStream());
            out1 = new PrintWriter(s1.getOutputStream());
            out2 = new PrintWriter(s2.getOutputStream());
            play();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public void play() {
        int player = 1;
        int x;
        int y;
        int z;
        while (true) {
            if (player == 1) {
                x = Integer.parseInt(in1.nextLine());
                if (x==101){
                    System.out.println("Winner is Player1!");
                    break;
                }else if (x == 102){
                    System.out.println("Winner is Player2!");
                    break;
                }else if (x == 103){
                    System.out.println("It's a Tie, no winner !");
                    break;
                }
                y = Integer.parseInt(in1.nextLine());
                out2.println(x);
                out2.flush();
                out2.println(y);
                out2.flush();
                player = player * (-1);
            } else {
                try {
                    x = Integer.parseInt(in2.nextLine());
                    if (x==101){
                        System.out.println("Winner is Player1!");
                        break;
                    }else if (x == 102){
                        System.out.println("Winner is Player2!");
                        break;
                    }else if (x == 103){
                        System.out.println("It's a Tie, no winner !");
                        break;
                    }
                    y = Integer.parseInt(in2.nextLine());
//                System.out.println("what :"+x+" , "+y);
                    out1.println(x);
                    out1.flush();
                    out1.println(y);
                    out1.flush();
                    player = player * (-1);
                }catch (NoSuchElementException e){
                    x = Integer.parseInt(in2.nextLine());
                    if (x==101){
                        System.out.println("Winner is Player1!");
                        break;
                    }else if (x == 102){
                        System.out.println("Winner is Player2!");
                        break;
                    }else if (x == 103){
                        System.out.println("It's a Tie, no winner !");
                        break;
                    }
                }
            }
        }
    }
}


