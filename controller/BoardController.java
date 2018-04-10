package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class BoardController extends Controller {

    @FXML
    public HBox board;
    @FXML
    public VBox gameState;
    @FXML
    public HBox enemyBoard;
    @FXML
    public VBox message;
    @FXML
    public Button okButton;
}
