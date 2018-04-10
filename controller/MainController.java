package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.*;
import sample.services.Game;

import java.io.IOException;

public class MainController extends Controller {

    @FXML
    private TextField serverName;
    @FXML
    public VBox serverContainer;

    private Game game;

    public void createServer() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("/sample/resources/gameBoard.fxml"));
        main.mainLayout.setCenter(mainLoader.load());
        BoardController boardController = (BoardController) mainLoader.getController();
        boardController.setMain(main);
        game = new Game(boardController, true, 20, 2, 5);
        game.setServerName(serverName.getText());
    }

    public void listInvet() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("/sample/resources/gameBoard.fxml"));
        main.mainLayout.setCenter(mainLoader.load());
        BoardController boardController = (BoardController) mainLoader.getController();
        boardController.setMain(main);
        game = new Game(boardController, false,20, 2, 5);
        game.start();
        game.getBoardController().okButton.setVisible(false);
    }


}
