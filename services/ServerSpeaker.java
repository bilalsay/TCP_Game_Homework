package sample.services;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.*;
import java.net.*;

public class ServerSpeaker extends Thread implements SpeechMaker {

    private ServerSocket socket = null;
    private Game game;
    private String serverName;
    public Socket clientSocket = null;
    public DataInputStream is = null;
    public PrintStream os = null;

    public ServerSpeaker(Game game, int port) {
        this.game = game;
        try {
            this.socket = new ServerSocket(port);
            clientSocket = socket.accept();
            os = new PrintStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerName(String serverName) {

        this.serverName = serverName;
    }

    public void run() {
        while(true) {
            String message = null;
            try {
                message = new BufferedReader(new InputStreamReader(is)).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client > " + message);
            String[] dt = message.trim().split("/");
            Platform.runLater(() -> {
                try {
                    if (dt[0].equalsIgnoreCase("any")) {
                        sendData(("invit/"+ serverName + "/").getBytes());
                    } else if(dt[0].equalsIgnoreCase("start")) {
                        game.start = true;
                        sendData("moveClient/".getBytes());
                        game.move = false;
                        game.getBoardController().message.getChildren().clear();
                        game.getBoardController().message.getChildren().add(new Label("Sira Karsida \n Rakibini bekle"));
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
                        game.getBoardController().message.getChildren().add(new Label("Sira Karsida \n Rakibini bekle"));
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

    @Override
    public void sendData(byte[] data) {
        os.println(new String(data));
    }
}
