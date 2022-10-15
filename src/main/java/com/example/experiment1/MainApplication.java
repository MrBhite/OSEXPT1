package com.example.experiment1;

import com.example.experiment1.Class.PCB;
import com.example.experiment1.Class.Progress;
import com.example.experiment1.Class.SystemLog;
import com.example.experiment1.Controller.MainController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    private static Stage stage;
    private static MainController mainController;

    private static int PID;
    private static int location;//location指向新进程的第一位
    private static ObservableList<Progress> progressList;
    private static ObservableList<PCB> PCBList;
    private static ObservableList<SystemLog> logList;

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


    public ObservableList<Progress> getProgressList() {
        return progressList;
    }

    public static ObservableList<PCB> getPCBList() {
        return PCBList;
    }

    public ObservableList<SystemLog> getLogList() {
        return logList;
    }

    public static void setLocation(int location) {
        MainApplication.location = location;
    }

    public static int getLocation() {
        return location;
    }

    public static int getPID() {
        return PID;
    }

    public static void setPID(int PID) {
        MainApplication.PID = PID;
    }

    public static void initialize(){

        PID=1;
        location = 0;

        Progress protect= new Progress("0",1,"0",0,1,1,1,1,1);
        progressList = FXCollections.observableArrayList(protect);
        PCBList = FXCollections.observableArrayList(protect.getPcb());

        logList = FXCollections.observableArrayList(
                new SystemLog("0","NULL","System Start!")
        );
    }
}


/*
* 目前的设想是，用progressList接收所有的进程，用PCBList接收在内存内运行的进程
* 正好也符合进了内存才开始创建PCB。
* 对内存溢出的检查则需要较大改动，改为进入PCBList后再进行检查是否溢出，而不是创建进程就检查。
* 暂定为此。
* */