package com.example.experiment1.Controller;

import com.example.experiment1.Class.PCB;
import com.example.experiment1.Class.Progress;
import com.example.experiment1.Class.SystemLog;
import com.example.experiment1.MainApplication;
import com.example.experiment1.Service.ProgressControlService;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketOption;
import java.util.*;

public class MainController {

    public Label WarningLabel;//非法输入警告
    public ImageView NormalPic;//100昏
    public ImageView WarningPic;//0昏

    //以下label显示当前运行进程属性
    public Label runningPID;
    public Label runningStatus;
    public Label runningPriority;
    public Label runningSize;
    public Label runningRemaining;
    public Label runningNext;

    //以下label显示内存队列PID
    public Label restMemory;
    public Label RunningPID;
    public Label nextPID;
    public Label thirdPID;
    public Label fourthPID;
    public Label fifthPID;
    public Label sixthPID;

    //切换模式
    public TextField timeSlice;//设置时间片
    public Label currentSlice;//显示当前时间片
    public Button setRR;//设置时间片模式
    public Button setPS;//设计优先级模式

    //设置新增控件
    public TextField addFormer;//前驱
    public TextField addPID;//设置新进程PID
    public TextField addTime;//设置新进程运行时间
    public TextField addPriority;//设置新进程优先级
    public TextField addSize;//新进程大小
    public Button addProgress;//按设置添加进程
    public Button randomAdd;//随机添加进程
    public ComboBox randomNumber;//设置随机生成进程数量

    //以下为模拟总控
    public Button start;//开始模拟
    public Button pause;//终止模拟
    public Button clearProgress;//清空进程

    //以下为右上角进度条
    public ProgressBar memorySchedule;//内存使用条
    public ProgressBar timeSliceSchedule;//时间片进度条
    public ProgressBar runningProgressSchedule;//当前进程进度条
    public ProgressBar totalSchedule;//总进度条

    //以下为进程总览表格控件
    @FXML
    public TableView progressOverview;//程序总览
    public TableColumn POPID;
    public TableColumn POStatus;
    public TableColumn POFormer;
    public TableColumn POPriority;
    public TableColumn POTime;
    public TableColumn PORemain;
    public TableColumn POSize;
    public TableColumn POHangup;
    public TableColumn POStart;
    public TableColumn PONext;

    //以下为系统日志表格控件
    public TableView systemLog;//系统日志
    public TableColumn SLPID;
    public TableColumn SLTime;
    public TableColumn SLContent;

    private MainApplication mainApplication;//主程序
    private ProgressControlService progressControlService;

    public MainController(){}

    public void initialize(MainApplication mainApplication){
        ObservableList<Integer> randomNumbers = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);
        randomNumber.setItems(randomNumbers);
        WarningPic.setVisible(false);

        this.mainApplication = mainApplication;
        progressControlService = new ProgressControlService(this);

        initPOTable();
        initSLTable();
    }

    public MainApplication getMainApplication(){
        return this.mainApplication;
    }

    public void initPOTable(){
        POPID.setCellValueFactory(new PropertyValueFactory<>("PID"));
        POStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        POFormer.setCellValueFactory(new PropertyValueFactory<>("former"));
        POPriority.setCellValueFactory(new PropertyValueFactory<>("priorityy"));
        POTime.setCellValueFactory(new PropertyValueFactory<>("TTime"));
        PORemain.setCellValueFactory(new PropertyValueFactory<>("RTime"));
        POSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        POStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        PONext.setCellValueFactory(new PropertyValueFactory<>("next"));
        POHangup.setCellValueFactory(new PropertyValueFactory<>("PID"));

        POPID.setStyle( "-fx-alignment: CENTER;");
        POStatus.setStyle( "-fx-alignment: CENTER;");
        POFormer.setStyle( "-fx-alignment: CENTER;");
        POPriority.setStyle( "-fx-alignment: CENTER;");
        POTime.setStyle( "-fx-alignment: CENTER;");
        PORemain.setStyle( "-fx-alignment: CENTER;");
        POSize.setStyle( "-fx-alignment: CENTER;");
        POStart.setStyle( "-fx-alignment: CENTER;");
        PONext.setStyle( "-fx-alignment: CENTER;");
        POHangup.setStyle( "-fx-alignment: CENTER;");
        progressOverview.setItems(mainApplication.getProgressList());
    }

    public void initSLTable(){
        SLTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        SLPID.setCellValueFactory(new PropertyValueFactory<>("PID"));
        SLContent.setCellValueFactory(new PropertyValueFactory<>("content"));

        SLTime.setStyle( "-fx-alignment: CENTER;");
        SLPID.setStyle( "-fx-alignment: CENTER;");
        systemLog.setItems(this.mainApplication.getLogList());
    }

    //添加进程
    public void addProgress(ActionEvent actionEvent) {
        String PID;
        String former = "0";
        int priority;
        double time;
        int size;
        int location = MainApplication.getLocation();

        //检查输入是否合法
        try{
            Integer.parseInt(addPID.getText());
            Integer.parseInt(addFormer.getText());
            PID = addPID.getText();
            priority = Integer.parseInt(addPriority.getText());
            time = Double.parseDouble(addTime.getText());
            size = Integer.parseInt(addSize.getText());
        }catch(Exception e){
            error(0);
            return;
        }

        //检查输入是否有意义
        if(priority<0||time<=0||size<=0){
            error(0);
        }

        //检查PID是否重复
        for(Progress p:mainApplication.getProgressList()){
            if(p.getPID().equals(PID)){
                error(0);
                return;
            }
        }

        /*
        //检查是否内存溢出
        location = MainApplication.getLocation();
        if((location+size)>this.mainApplication.getProgressList().get(1).getStart()+8192){
            error(1);
        }
         */

        //
        int status = 0;
        if (mainApplication.getProgressList().size() < 6) {
            status = 1;
        }

        Progress tmp = new Progress(PID,
                status,
                former,
                priority,
                time,
                time,
                size,
                location,
                location + size);

        //加入新进程
        this.mainApplication.getProgressList().add(tmp);

        if(MainApplication.getPCBList().size()<6) {
            tmp.createPCB();
            if(!this.mainApplication.insertPCB(tmp.getPcb()))
                return;
            MainApplication.setReserveProgress(MainApplication.getReserveProgress()+1);
            System.out.println(MainApplication.getReserveProgress());
        }

        //更新全局变量
        mainApplication.setLocation((location + size)%8192);
        int pid = Integer.parseInt(PID);
        if(pid>MainApplication.getPID()){
            MainApplication.setPID(pid);
        }
        resetPic();
    }

    //随机添加新进程
    public void randomAdd(ActionEvent actionEvent) {
        long  t = System.currentTimeMillis(); //获得当前时间的毫秒数
        for(int i = Integer.parseInt(randomNumber.getValue().toString()); i > 0 ; i--){
            Random rd =  new  Random(t+i); //作为种子数传入到Random的构造器中
            int time = rd.nextInt(4)+1;
            int size = (rd.nextInt(10)+1)*128;
            int location;
            int pid = MainApplication.getPID();
            location = MainApplication.getLocation();

            /*
            if((location+size)>this.mainApplication.getProgressList().get(0).getStart()+8192){
                error(1);
            }
             */

            int status = 0;
            if (mainApplication.getProgressList().size() < 6) {
                status = 1;
            }

            Progress tmp = new Progress(String.valueOf(pid+1),
                    status,
                    "0",
                    rd.nextInt(7)+1,
                    time,
                    time,
                    size,
                    location,
                    location+ size);

            //加入新进程
            this.mainApplication.getProgressList().add(tmp);

            if(MainApplication.getPCBList().size()<6) {
                tmp.createPCB();
                this.mainApplication.insertPCB(tmp.getPcb());
                MainApplication.setReserveProgress(MainApplication.getReserveProgress()+1);
            }

            //更新全局变量
            mainApplication.setLocation((location + size)%8192);
            MainApplication.setPID(pid + 1);
        }
    }

    //开始模拟
    public void start(ActionEvent actionEvent) {
        progressControlService.setPCBList(MainApplication.getPCBList());
        progressControlService.start();
        //setRunning();
        /*
        Progress tmp = mainApplication.getProgressList().get(i);
        tmp.getPcb().setState("running");
        tmp.start();
         */
    }

    //将第i个PCBList中的元素放入中间的Running栏
    public void setRunning(){
        runningPID.setText(MainApplication.getPCBList().get(0).getPID());
        runningStatus.setText(MainApplication.getPCBList().get(0).getStatus());
        runningPriority.setText(String.valueOf(MainApplication.getPCBList().get(0).getPriorityy()));
        runningNext.setText(String.valueOf(MainApplication.getPCBList().get(0).getNext()));
        runningRemaining.setText(String.valueOf(mainApplication.df.format(MainApplication.getPCBList().get(0).getRTime())));
        runningSize.setText(String.valueOf(MainApplication.getPCBList().get(0).getSize()));

        //绑定
        MainApplication.getPCBList().get(0).getRTimeProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        runningRemaining.setText(MainApplication.df.format(t1.doubleValue()));
                    }
                });
            }
        });
    }

    //没办法人家就是小笨猪人家就是不会自己写exception嘛。
    public void error(int e){
        System.out.println(e);
        switch (e){
            case 0-> WarningLabel.setText("Invalid input detected!");//错误输入(包括PID冲突，输入栏格式错误)
            case 1-> WarningLabel.setText("Memory overflow!");//内存溢出
        }
        WarningPic.setVisible(true);
        NormalPic.setVisible(false);
    }

    public void resetPic(){
        WarningPic.setVisible(false);
        NormalPic.setVisible(true);
        WarningLabel.setText("");
    }

    public void pause(ActionEvent actionEvent) {
        Progress tmp = mainApplication.getProgressList().get(0);
        if (tmp.isAlive())
            System.out.println("Alive");
        else
            System.out.println("wait");
    }
}