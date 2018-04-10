package sample.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import sample.controller.BoardController;

import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {

    private List<Rectangle> gameGrid;
    private List<Rectangle> enemyGrid;
    private int shipSize;
    private int shipNumber;
    private int gridLength;
    public ServerSpeaker socketServer;
    public ClientSpeaker socketClient;
    public boolean isServer;
    public ObservableList<Button> observableList;
    private String serverName;
    public boolean move;
    public boolean start;
    private BoardController boardController;
    public int hitShipNumber = 0;

    public Game(BoardController boardController, boolean isServer, int gridLength, int shipSize, int shipNumber) {
        this.boardController = boardController;
        this.isServer = isServer;
        gameGrid = new ArrayList<>(gridLength);
        this.gridLength = gridLength;
        this.shipSize = shipSize;
        this.shipNumber = shipNumber;
        observableList = gridInitialize(false);
        boardController.board.getChildren().addAll(getObservableList());
        boardController.okButton.setOnMouseClicked(event -> {
            this.start();
            boardController.gameState.setVisible(true);
            boardController.enemyBoard.getChildren().addAll(gridInitialize(true));
            boardController.okButton.setVisible(false);
        });
    }

    public ServerSpeaker getSocketServer() {
        return socketServer;
    }

    public ClientSpeaker getSocketClient() {
        return socketClient;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public ObservableList<Button> gridInitialize(boolean isEnemy) {
        List<Rectangle> grid = new ArrayList<>(gridLength);
        for (int i = 0; i < gridLength; i++) {
            Rectangle rectangleButton = new Rectangle(this,false);
            grid.add(rectangleButton);
        }

        if (isEnemy) {
            enemyGrid = grid;
        } else {
            gameGrid = grid;
        }

        ObservableList<Button> observerList = FXCollections.observableArrayList();
        for(Button rectangleButton: grid) {
            try {
                observerList.add(rectangleButton);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return observerList;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public ObservableList<Button> getObservableList() {
        return observableList;
    }

    public List<Rectangle> getGameGrid() {
        return gameGrid;
    }

    public List<Rectangle> getEnemyGrid() {
        return enemyGrid;
    }

    public synchronized void start() {
        if (getIsServer()) {
            socketServer = new ServerSpeaker(this, 1234);
            socketServer.setServerName(serverName);
            socketServer.start();
        } else {
            socketClient = new ClientSpeaker(this,"localhost", 1234);
            socketClient.start();
            socketClient.sendData("any/".getBytes());
        }
    }

    public boolean getIsServer() {
        return isServer;
    }

    @Override
    public void run() {

    }

    public int getShipSize() {
        return shipSize;
    }

    public int getShipNumber() {
        return shipNumber;
    }

    public int getGridLength() {
        return gridLength;
    }
}
