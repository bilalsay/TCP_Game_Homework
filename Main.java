package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.controller.MainController;

import java.awt.*;

public class Main extends Application {

    public BorderPane mainLayout;
    private Stage primaryStage;

    @FXML
    public Label l;

    @Override
    public void start(Stage pStage) throws Exception {
        primaryStage = pStage;
        primaryStage.setTitle("TCPSocket");
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.centerOnScreen();
    }

    @Override
    public void init() throws Exception {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("/sample/resources/main.fxml"));
        mainLayout = mainLoader.load();
        MainController mainController = (MainController) mainLoader.getController();
        mainController.setMain(this);
        mainLayout.autosize();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(Main.class, args);

    }
}