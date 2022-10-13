package com.example.experiment1.Controller;

import com.example.experiment1.Class.Progress;
import com.example.experiment1.MainApplication;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.SocketOption;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainController {

    public Label WarningLabel;//非法输入警告
    public ImageView NormalPic;//100昏
    public ImageView WarningPic;//0昏

    //以下label显示当前运行进程属性
    public Label runningPID;
    public Label runningStatus;
    public Label runningPriority;
    public Label runningTime;
    public Label runningRemaining;
    public Label runningNext;

    //以下label显示内存队列PID
    public Label RunningPID;
    public Label nextPID;
    public Label thirdPID;
    public Label fourthPID;
    public Label fifthPID;
    public Label sixthPID;

    public TextField timeSlice;//设置时间片
    public Label currentSlice;//显示当前时间片
    public Button setRR;//设置时间片模式
    public Button setPS;//设计优先级模式
    public TextField addPID;//设置新进程PID
    public TextField addTime;//设置新进程运行时间
    public TextField addPriority;//设置新进程优先级
    public Button addProgress;//按设置添加进程
    public Button randomAdd;//随机添加进程
    public ComboBox randomNumber;//设置随机生成进程数量
    @FXML
    public TableView progressOverview;//程序总览
    public TableView systemLog;//系统日志
    public Button start;//开始模拟
    public Button pause;//终止模拟
    public Button clearProgress;//清空进程
    public ProgressBar timeSliceSchedule;//时间片进度条
    public ProgressBar runningProgressSchedule;//当前进程进度条
    public ProgressBar totalSchedule;//总进度条
    @FXML
    private TableColumn POPID;
    private MainApplication mainApplication;//主程序

    private ObservableList<Progress> progressList;

    public MainController(){}

    public void initialize(MainApplication mainApplication){
        ObservableList<Integer> randomNumbers = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
        randomNumber.setItems(randomNumbers);


        progressList = FXCollections.observableArrayList(
                new Progress("1","1",1,1,1,1),
                new Progress("1","2",1,1,1,1),
                new Progress("1","3",1,1,1,1)
        );
        initPOTable();

        WarningPic.setVisible(false);
        this.mainApplication = mainApplication;
    }

    public void initPOTable(){
        //POPID.setCellValueFactory(cellData -> cellData.getValue);
        progressOverview.setItems(progressList);
    }

    public void addProgress(ActionEvent actionEvent) {
        int state = 1;
        if (progressList.size() <= 6) {
            state = 0;
        }
        progressList.add(new Progress("default",
                addPID.getText(),
                Integer.parseInt(addTime.getText()),
                Integer.parseInt(addPriority.getText()),
                state,
                Integer.parseInt(addPID.getText()) + 1));
    }

    public void initLogTable(){

    }
}