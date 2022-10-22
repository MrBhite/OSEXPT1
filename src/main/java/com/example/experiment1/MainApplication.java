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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainApplication extends Application {
    private static Stage stage;
    private static MainController mainController;

    public static DecimalFormat df = new DecimalFormat( "0.0");
    public static SimpleDateFormat ddf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");

    private static boolean pause;
    private static boolean privilege;
    private static int PID;//下一个随机进程的PID
    private static int location;//location指向新进程的第一位储存位置
    private static int reserveProgress;//后背队列第一个Progress的PID
    private static double timeSlice;//时间片
    private static ObservableList<Progress> progressList;
    private static ObservableList<PCB> PCBList;
    private static ObservableList<SystemLog> logList;

    public static void main(String[] args) {
        launch(args);
    }

    public static SimpleDateFormat getDdf(){
        return ddf;
    }

    public static int getReserveProgress() {
        return reserveProgress;
    }

    public static void setReserveProgress(int reserveProgress) {
        MainApplication.reserveProgress = reserveProgress;
    }

    public static boolean isPause() {
        return pause;
    }

    public static void setPause(boolean pause) {
        MainApplication.pause = pause;
    }

    public static boolean isPrivilege() {
        return privilege;
    }

    public static void setPrivilege(boolean privilege) {
        MainApplication.privilege = privilege;
    }

    public static double getTimeSlice() {
        return timeSlice;
    }

    public static void setTimeSlice(double timeSlice) {
        MainApplication.timeSlice = timeSlice;
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
        PID = 0;
        location = 0;
        reserveProgress = 1;
        privilege=true;//默认为优先级
        timeSlice=60;//默认优先级，时间片为60(设置为一个极大值，在可预见的模拟中不会跑完)
        pause = false;//默认为未停止

        Progress protect= new Progress("Protected",1,"Null",0,2,2,0,0,0);
        protect.createPCB();
        progressList = FXCollections.observableArrayList(protect);
        PCBList = FXCollections.observableArrayList(protect.getPcb());

        logList = FXCollections.observableArrayList(
                new SystemLog("0","Null","System Start!")
        );
    }

    //加入新的PCB
    public static boolean insertPCB(PCB pcb){
        //检查内存溢出
        if((location+pcb.getSize())>progressList.get(1).getStart()+8192){
            mainController.error(1);
            return false;
        }
        PCBList.add(pcb);
        return true;
    }

    //删除第i个PCb
    public static void deletePCB(PCB pcb){
        PCBList.remove(pcb);
    }
}




/*
* 目前的设想是，用progressList接收所有的进程，用PCBList接收在内存内运行的进程
* 正好也符合进了内存才开始创建PCB。
* 对内存溢出的检查则需要较大改动，改为进入PCBList后再进行检查是否溢出，而不是创建进程就检查。
* 暂定为此。
* */

/*
* 只有要进PCBList的Progress才会分配调用createPCB函数
* 进而创建PCB
* */