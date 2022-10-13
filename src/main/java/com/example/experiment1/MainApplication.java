package com.example.experiment1;

import com.example.experiment1.Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {

    private static Stage stage;
    private static MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage PrimaryStage) throws IOException {
        stage = PrimaryStage;
        stage.setTitle("Test");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene (root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        this.initialize();

        mainController = fxmlLoader.getController();
        mainController.initialize(this);
    }

    public static void initialize(){

    }
}
