package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameClient extends Application {
    Scanner in;
    PrintWriter out;
    static int player;
    static int selectX;
    static int selectY;
    static boolean waiting = true;
    private Controller controller;
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
            Pane root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();

            Socket s = new Socket("localhost",  12344);
            in = new Scanner(s.getInputStream());
            out = new PrintWriter(s.getOutputStream());
            new Thread(() -> {
                player = Integer.parseInt(in.nextLine());
                if (player == 1) {
                    System.out.println(in.nextLine());
                }
                System.out.println(in.nextLine());
                while (true) {
//                    System.out.println(Controller.winner);
                    if (player == 1) {
                        try {
                            waitCt();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        sendMsg();
                        doMsg();
                    } else if (player == -1) {
                        doMsg();
                        try {
                            waitCt();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        sendMsg();
                    }
                    if (Controller.winner==1){
                        out.println(101);
                        out.flush();
                        break;
                    }else if (Controller.winner==-1){
                        out.println(102);
                        out.flush();
                        break;
                    }else if (Controller.winner==2){
                        out.println(103);
                        out.flush();
                        break;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
//        System.out.println("send : "+selectX+" , "+selectY);
        out.println(selectX);
        out.flush();
        out.println(selectY);
        out.flush();
    }
    public void waitCt() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }
        waiting = true;
    }
    public void doMsg() {
        try {
            int x = in.nextInt();
            int y = in.nextInt();
            controller.updateChess(x,y,player*-1);
            Controller.flag[x][y] = true;
        }catch (NoSuchElementException e){
            System.out.println("It's a Tie, no winner !");
        }
    }

}




