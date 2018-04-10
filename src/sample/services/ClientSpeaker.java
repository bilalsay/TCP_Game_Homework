package sample.services;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.controller.BoardController;

import java.io.*;
import java.net.*;

public class ClientSpeaker extends Thread implements SpeechMaker {

   public Socket socket = null;
   public DataInputStream is = null;
   public PrintStream os = null;
   private Game game;

   public ClientSpeaker(Game game, String ipAddress, int port) {
      this.game = game;
      try {
         this.socket = new Socket(ipAddress, port);
         os = new PrintStream(socket.getOutputStream());
         is = new DataInputStream(socket.getInputStream());
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void run() {
      while(true) {
         String message = null;
         try {
            message = new BufferedReader(new InputStreamReader(is)).readLine();
         } catch (IOException e) {
            e.printStackTrace();
         }
         System.out.println("Server > " + message);
         String[] dt = message.trim().split("/");
         Platform.runLater(() -> {
            try {
               if (dt[0].equalsIgnoreCase("invit")) {
                  Button button = new Button(dt[1]);
                  button.setOnMouseClicked(event -> {
                     game.getBoardController().message.getChildren().clear();
                     sendData("start/".getBytes());
                     game.start = true;
                     game.getBoardController().gameState.setVisible(true);
                     game.getBoardController().enemyBoard.getChildren().clear();
                     game.getBoardController().enemyBoard.getChildren().addAll(game.gridInitialize(true));
                  });
                  game.getBoardController().message.getChildren().add(button);
               } else if (dt[0].equalsIgnoreCase("moveClient")) {
                  game.move = true;
                  game.getBoardController().message.getChildren().clear();
                  game.getBoardController().message.getChildren().add(new Label("Sira Sende. \n Vurmak icin tikla."));
               } else if(dt[0].equalsIgnoreCase("point")) {
                  game.move = true;
                  if (game.getGameGrid().get(Integer.parseInt(dt[1])).getText().equalsIgnoreCase("S")) {
                     game.getGameGrid().get(Integer.parseInt(dt[1])).setText("X");
                     game.getGameGrid().get(Integer.parseInt(dt[1])).setStyle("-fx-background-color: blue");
                     game.hitShipNumber++;
                     if (game.hitShipNumber == (game.getShipNumber() * game.getShipSize())) {
                        sendData(("gumAndWin/" + dt[1] + "/").getBytes());
                        game.getBoardController().message.getChildren().clear();
                        game.getBoardController().message.getChildren().add(new Label("Oyunu kaybettin dostum."));
                     } else {
                        sendData(("gum/" + dt[1] + "/").getBytes());
                        game.getBoardController().message.getChildren().clear();
                        game.getBoardController().message.getChildren().add(new Label("Sira Sende. \n Vurmak icin tikla."));
                     }
                  } else {
                     sendData(("empty/" + dt[1] + "/").getBytes());
                     game.getBoardController().message.getChildren().clear();
                     game.getBoardController().message.getChildren().add(new Label("Sira Sende. \n Vurmak icin tikla."));
                  }

               } else if(dt[0].equalsIgnoreCase("gum")) {
                  game.move = false;
                  game.getEnemyGrid().get(Integer.parseInt(dt[1])).setText("X");
                  game.getEnemyGrid().get(Integer.parseInt(dt[1])).setStyle("-fx-background-color: red");
                  game.getBoardController().message.getChildren().clear();
                  game.getBoardController().message.getChildren().add(new Label("Sira Karsida. \n Rakibini bekle."));
               } else if(dt[0].equalsIgnoreCase("gumAndWin")) {
                  game.move = false;
                  game.getEnemyGrid().get(Integer.parseInt(dt[1])).setText("X");
                  game.getEnemyGrid().get(Integer.parseInt(dt[1])).setStyle("-fx-background-color: red");
                  game.getBoardController().message.getChildren().clear();
                  game.getBoardController().message.getChildren().add(new Label("Oyun Bitti, Kazandin."));
               } else if(dt[0].equalsIgnoreCase("empty")) {
                  game.move = false;
                  game.getBoardController().message.getChildren().clear();
                  game.getBoardController().message.getChildren().add(new Label("Sira Karsida \n Rakibini bekle"));
               }
            } catch (Exception e) {
            }
         });
      }
   }

   public void sendData(byte[] data) {
         os.println(new String(data));
   }
}
