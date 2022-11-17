package com.example.experiment1;

import com.example.experiment1.Class.MemorySlice;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainApplication extends Application {
    private static Stage stage;
    private static MainController mainController;

    private Parent root;

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
    private static ObservableList<MemorySlice> memorySlicesList;
    private static HashMap<Progress,Label> PLMap;
    private static Vector<Integer> memorySliceLocation;

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

    public static HashMap<Progress, Label> getPLMap() {
        return PLMap;
    }

    public static void setPLMap(HashMap<Progress, Label> PLMap) {
        MainApplication.PLMap = PLMap;
    }

    public static Vector<Integer> getMemorySliceLocation() {
        return memorySliceLocation;
    }

    public static void setMemorySliceLocation(Vector<Integer> memorySliceLocation) {
        MainApplication.memorySliceLocation = memorySliceLocation;
    }

    @Override
    public void start(Stage PrimaryStage) throws IOException {
        stage = PrimaryStage;
        stage.setTitle("Test");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        root = fxmlLoader.load();
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
    public static ObservableList<MemorySlice> getMemorySlicesList() { return  memorySlicesList;}

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

        memorySliceLocation = new Vector<>();
        memorySliceLocation.addElement(protect.getStart());
        memorySliceLocation.addElement(protect.getSize());

        PLMap = new HashMap<>();

        memorySlicesList = FXCollections.observableArrayList(new MemorySlice(protect.getPcb()));
    }

    //加入新的PCB
    public static boolean insertPCB(PCB pcb){
        //检查内存溢出

        if(pcb.getStart()+pcb.getSize()>8192){
            mainController.error(1);
            return false;
        }

        memorySlicesList.add(new MemorySlice(pcb));
        PCBList.add(pcb);
        return true;

    }

    //删除第i个PCb
    public static void deletePCB(PCB pcb){
        PCBList.remove(pcb);
    }

    public Parent getRoot(){
        return root;
    }
}