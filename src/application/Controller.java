package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static javafx.application.Platform.exit;

public class Controller implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = -1;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    static int numCircle = 0;
    static int numLine = 0;
    static int player = 1;
    static int winner = 0;

//    @FXML
//    private DialogPane win_square;

    @FXML
    Pane base_square;


    @FXML
    Rectangle game_panel;

    int[][] chessBoard = new int[3][3];
    static boolean[][] flag = new boolean[3][3];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize...");
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            player = GameClient.player;
            drawChessBoard(x,y,player);
            GameClient.waiting = false;
        });
    }

    void drawChessBoard(int x ,int y,int player){
        if (chessBoard[x][y]==EMPTY) {
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[i][j] == 1) {
                        numCircle++;
                    } else if (chessBoard[i][j] == -1) {
                        numLine++;
                    }
                }
            }
//            System.out.println("numLine: "+numLine+" , numCircle: "+numCircle);
            if (numCircle > numLine &&player == -1) {
                GameClient.selectX = x;
                GameClient.selectY = y;
                drawLine(x, y);
                chessBoard[x][y]=-1;
                checkWin(chessBoard);
                winShow();
                checkTie();
            } else if (numCircle <= numLine &&player == 1){
                GameClient.selectX = x;
                GameClient.selectY = y;
                drawCircle(x, y);
                chessBoard[x][y]=1;
                checkWin(chessBoard);
                winShow();
                checkTie();
            }else {
                System.out.println("Invalid click!");
            }
            numCircle = 0;
            numLine = 0;
//            System.out.println("winner is : "+winner);
        }else {
            System.out.println("Invalid click!");
        }
    }


    void updateChess (int x, int y, int player) {
//        System.out.println("receive :"+x+" , "+y);
        if (chessBoard[x][y]==EMPTY) {
            switch (player) {
                case PLAY_1:
                    chessBoard[x][y] = 1;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            drawCircle(x, y);
                        }
                    });
                    break;
                case PLAY_2:
                    chessBoard[x][y] = -1;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            drawLine(x, y);
                        }
                    });
                    break;
                default:
                    System.err.println("Invalid value!");
            }
        }
        checkWin(chessBoard);
//        System.out.println(winner);
    }

    void checkTie(){
        int k = 0;
        if (player==-1){
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[i][j]!=0){
                        k++;
                    }
                }
            }
            if (k==8){
//                System.out.println("Sure");
                int i = 0;
                int j = 0;
                for (i = 0; i < chessBoard.length; i++) {
                    for (j = 0; j < chessBoard.length; j++) {
                        if (chessBoard[i][j]==0){
                            chessBoard[i][j]=1;
                            break;
                        }
                    }
                }
                checkWin(chessBoard);
                if (winner == 0){
                    winner = 2;
                }
//                System.out.println(i+ " + "+j);
                chessBoard[i-1][j-1] = 0;
            }
        }else if (player==1||player==-1){
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[i][j]!=0){
                        k++;
                    }
                }
            }
            if (k==9){
                checkWin(chessBoard);
                if (winner == 0){
                    winner = 2;
                }
            }
        }
        if (winner == 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("It's Tie!");
            alert.showAndWait();
            exit();
        }
    }

    void checkWin(int[][] chessBoard){
        if (winner==0) {
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[i][j] != 1) break;
                    if (j == 2) {
                        winner = 1;
                        break;
                    }
                }
            }
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[j][i] != 1) break;
                    if (j == 2) {
                        winner = 1;
                        break;
                    }
                }
            }
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[i][j] != -1) break;
                    if (j == 2) {
                        winner = -1;
                        break;
                    }
                }
            }
            for (int i = 0; i < chessBoard.length; i++) {
                for (int j = 0; j < chessBoard.length; j++) {
                    if (chessBoard[j][i] != -1) break;
                    if (j == 2) {
                        winner = -1;
                        break;
                    }
                }
            }
            if (chessBoard[0][0] == 1 && chessBoard[1][1] == 1 && chessBoard[2][2] == 1) {
                winner = 1;
            }
            if (chessBoard[0][0] == -1 && chessBoard[1][1] == -1 && chessBoard[2][2] == -1) {
                winner = -1;
            }
            if (chessBoard[0][2] == 1 && chessBoard[1][1] == 1 && chessBoard[2][0] == 1) {
                winner = 1;
            }
            if (chessBoard[0][2] == -1 && chessBoard[1][1] == -1 && chessBoard[2][0] == -1) {
                winner = -1;
            }
        }
    }

    void winShow(){
        if (winner == player){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("You are win!");
            alert.showAndWait();
            exit();
        }
        if (winner == player*(-1)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("You are lose!");
            alert.showAndWait();
            exit();
        }
    }


    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                base_square.getChildren().add(circle);
            }
        });
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;

    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
//
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }
}
