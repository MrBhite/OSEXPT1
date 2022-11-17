package com.example.experiment1.Controller;

import com.example.experiment1.Class.MemorySlice;
import com.example.experiment1.Class.Progress;
import com.example.experiment1.Class.SystemLog;
import com.example.experiment1.MainApplication;
import com.example.experiment1.Service.ProgressControlService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    public TextField releasePID;
    public Button releaseButton;
    public Label memerySlice;
    public Button hangUp;

    //以下为内存日志表格控件
    public TableView memeryLog;
    public TableColumn MLPID;
    public TableColumn MLSTART;
    public TableColumn MLEND;

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
        initMLTable();
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
        systemLog.setItems(mainApplication.getLogList());
    }

    public void initMLTable(){
        MLPID.setCellValueFactory(new PropertyValueFactory<>("PID"));
        MLSTART.setCellValueFactory(new PropertyValueFactory<>("start"));
        MLEND.setCellValueFactory(new PropertyValueFactory<>("end"));

        MLPID.setStyle( "-fx-alignment: CENTER;");
        MLSTART.setStyle( "-fx-alignment: CENTER;");
        MLEND.setStyle( "-fx-alignment: CENTER;");
        memeryLog.setItems(mainApplication.getMemorySlicesList());
    }

    //添加进程
    public void addProgress(ActionEvent actionEvent) {
        String PID;
        String former = addFormer.getText();
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
        int status = 5;
        if (mainApplication.getProgressList().size() < 6) {
            status = 0;
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

        //新进程入PCB队列
        if(MainApplication.getPCBList().size()<6) {
            tmp.setStatus(0);
            tmp.createPCB();
            setMemorySliceLocation(tmp);
            this.mainApplication.insertPCB(tmp.getPcb());
            //查看是否有前驱并置为wait
            for(Progress pro:mainApplication.getProgressList()){
                if(pro.getPID().equals(tmp.getFormer())&&!(pro.getStatus().equals("Done"))){
                    tmp.setStatus(2);
                    tmp.getPcb().setState("waiting");
                }
            }
            MainApplication.setReserveProgress(MainApplication.getReserveProgress()+1);
        }

        //更新系统日志
        Date tmpDate = new Date(System.currentTimeMillis());
        String tmpDateString = mainApplication.getDdf().format(tmpDate);
        mainApplication.getLogList().add(new SystemLog(tmpDateString,tmp.getPID(),"New Progress Added and Now is "+tmp.getStatus()));

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
            double time = Double.parseDouble(mainApplication.df.format(rd.nextInt(40)*0.1+2));
            int size = (rd.nextInt(7)+3)*128;
            int location;
            int pid = MainApplication.getPID();
            location = MainApplication.getLocation();
            /*
            if((location+size)>this.mainApplication.getProgressList().get(0).getStart()+8192){
                error(1);
            }
             */

            int status = 5;
            if (mainApplication.getProgressList().size() < 6) {
                status = 0;
            }

            Progress tmp = new Progress(String.valueOf(pid+1),
                    status,
                    "Null",
                    rd.nextInt(7)+1,
                    time,
                    time,
                    size,
                    0,
                    0);

            //加入新进程
            this.mainApplication.getProgressList().add(tmp);

            //新进程入PCB队列
            if(MainApplication.getPCBList().size()<6) {
                tmp.setStatus(0);
                tmp.createPCB();
                setMemorySliceLocation(tmp);
                this.mainApplication.insertPCB(tmp.getPcb());
                MainApplication.setReserveProgress(MainApplication.getReserveProgress()+1);
            }

            //更新系统日志
            Date tmpDate = new Date(System.currentTimeMillis());
            String tmpDateString = mainApplication.getDdf().format(tmpDate);
            mainApplication.getLogList().add(new SystemLog(tmpDateString,tmp.getPID(),"New Progress Added and Now is "+tmp.getStatus()));

            //更新全局变量
            mainApplication.setLocation((location + size)%8192);
            MainApplication.setPID(pid + 1);
        }
    }

    //开始模拟
    public void start(ActionEvent actionEvent) {
        if(MainApplication.isPause() == true){
            MainApplication.setPause(false);
            return;
        }
        progressControlService.setList(MainApplication.getPCBList(), mainApplication.getProgressList(), mainApplication.getMemorySlicesList());
        progressControlService.start();
        //setRunning();
        /*
        Progress tmp = mainApplication.getProgressList().get(i);
        tmp.getPcb().setState("running");
        tmp.start();
         */
    }

    //将第i个PCBList中的元素放入中间的Running栏
    public void refreshBoard(){
        //runningProgress栏
        runningPID.setText(MainApplication.getPCBList().get(0).getPID());
        runningStatus.setText(MainApplication.getPCBList().get(0).getStatus());
        runningPriority.setText(String.valueOf(MainApplication.getPCBList().get(0).getPriorityy()));
        runningNext.setText(String.valueOf(MainApplication.getPCBList().get(0).getNext()));
        runningRemaining.setText(String.valueOf(mainApplication.df.format(MainApplication.getPCBList().get(0).getRTime())));
        runningSize.setText(String.valueOf(MainApplication.getPCBList().get(0).getSize()));
        //绑定RTime
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

        //nextProgress栏
        switch (mainApplication.getPCBList().size()){
            case 6:sixthPID.setText(mainApplication.getPCBList().get(5).getPID());
            case 5:fifthPID.setText(mainApplication.getPCBList().get(4).getPID());
            case 4:fourthPID.setText(mainApplication.getPCBList().get(3).getPID());
            case 3:thirdPID.setText(mainApplication.getPCBList().get(2).getPID());
            case 2:nextPID.setText(mainApplication.getPCBList().get(1).getPID());
            case 1:RunningPID.setText(mainApplication.getPCBList().get(0).getPID());
        }
    }

    //没办法人家就是小笨猪人家就是不会自己写exception嘛。
    public void error(int e){
        //获得时间
        Date tmpDate = new Date(System.currentTimeMillis());
        String tmpDateString = getMainApplication().getDdf().format(tmpDate);
        switch (e){
            case 0-> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        WarningLabel.setText("Invalid input detected!");//错误输入(包括PID冲突，输入栏格式错误)
                    }
                });
                getMainApplication().getLogList().add(new SystemLog(tmpDateString,"NULL","Invalid input detected! The system is paused automatically."));
            }
            case 1-> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        WarningLabel.setText("Memory overflow!");//内存溢出
                    }
                });
                getMainApplication().getLogList().add(new SystemLog(tmpDateString,"NULL","Memory overflow! Please restart the System."));
            }
        }
        WarningPic.setVisible(true);
        NormalPic.setVisible(false);
        MainApplication.setPause(true);
    }

    public void resetPic(){
        WarningPic.setVisible(false);
        NormalPic.setVisible(true);
        WarningLabel.setText("");
    }

    public void pause(ActionEvent actionEvent) {
        MainApplication.setPause(true);
    }

    public void setRR(ActionEvent actionEvent) {
        //获得时间
        Date tmpDate = new Date(System.currentTimeMillis());
        String tmpDateString = getMainApplication().getDdf().format(tmpDate);
        getMainApplication().getLogList().add(new SystemLog(tmpDateString,"NULL","System Mod Changed To RR Mod"));

        mainApplication.setPrivilege(false);
        mainApplication.setTimeSlice(Double.parseDouble(timeSlice.getText()));
        currentSlice.setText(timeSlice.getText());
    }

    public void setPriority(ActionEvent actionEvent) {
        //获得时间
        Date tmpDate = new Date(System.currentTimeMillis());
        String tmpDateString = getMainApplication().getDdf().format(tmpDate);
        getMainApplication().getLogList().add(new SystemLog(tmpDateString,"NULL","System Mod Changed To Priority Mod"));

        mainApplication.setPrivilege(true);
        currentSlice.setText("");
        mainApplication.setTimeSlice(60);
    }

    public void hangUpRunning(ActionEvent actionEvent) {
        String tmpID = mainApplication.getPCBList().get(0).getPID();
        int i;
        for (i = mainApplication.getProgressList().size() - 1; i >= 0; i--) {
            if (mainApplication.getProgressList().get(i).getPID().equals(tmpID))
                break;
        }
        Progress tmpProgress = mainApplication.getProgressList().get(i);
        tmpProgress.setHangUp(true);
        tmpProgress.setStatus(3);
        tmpProgress.getPcb().setState("Hang Up");
        System.out.println("test"+tmpProgress.getPcb().getStatus());
    }

    public void release(ActionEvent actionEvent) {
        String releasePID = this.releasePID.getText();
        for(Progress tmpProgress:mainApplication.getProgressList()){
            if (tmpProgress.getPID().equals(releasePID)){
                //解挂操作
                tmpProgress.setHangUp(false);

                //修改属性
                tmpProgress.setStatus(0);
                tmpProgress.getPcb().setState("ready");

                //更新日志
                Date tmpDate = new Date(System.currentTimeMillis());
                String tmpDateString = getMainApplication().getDdf().format(tmpDate);
                getMainApplication().getLogList().add(new SystemLog(tmpDateString,releasePID,"Progress Released."));
                break;
            }
        }
    }

    public void setMemorySliceLocation(Progress progress){
        Vector<Integer> memorySliceLocation = MainApplication.getMemorySliceLocation();
        int lastEnd = memorySliceLocation.size()-1;
        int size = progress.getSize();
        //0位插入
        if(memorySliceLocation.get(0)>size){
            progress.setStart(0);
            progress.setNext(size);
            progress.getPcb().setStart(0);
            progress.getPcb().setNext(size);
            memorySliceLocation.insertElementAt(0,0);
            memorySliceLocation.insertElementAt(size,1);
            addLabel(progress);
            return;
        }

        //间隙插入
        for(int i = 2; i <lastEnd ; i += 2){
            int end = memorySliceLocation.get(i-1);
            int start = memorySliceLocation.get(i);
            int space = start - end;
            if(space>progress.getSize()){
                progress.setStart(end);
                progress.setNext(end + size);
                progress.getPcb().setStart(end);
                progress.getPcb().setNext(end + size);
                memorySliceLocation.insertElementAt(end,i);//本质上是插入到上一个结束加一的位置，即(i-1)+1
                memorySliceLocation.insertElementAt(end+size,i+1);
                addLabel(progress);
                return;
            }
        }

        //最后插入
        int lastValue = memorySliceLocation.get(lastEnd);
        progress.setStart(lastValue);
        progress.setNext(lastValue + size);
        progress.getPcb().setStart(lastValue);
        progress.getPcb().setNext(lastValue + size);
        memorySliceLocation.addElement(lastValue);
        memorySliceLocation.addElement(lastValue + size);
        addLabel(progress);
    }

    public void releaseMemory(Progress progress){
        Vector<Integer> memorySliceLocation = MainApplication.getMemorySliceLocation();
        int lastEnd = memorySliceLocation.size()-1;
        int size = progress.getSize();
        int pStart = progress.getStart();
        ObservableList<MemorySlice> memorySliceList = MainApplication.getMemorySlicesList();
        for(int i = 0; i < lastEnd; i +=2){
            if(pStart==memorySliceLocation.get(i)){
                //删除vector中的内容
                memorySliceLocation.remove(i);
                memorySliceLocation.remove(i);
                break;
            }
        }
        //删除tableView中内容
        for(MemorySlice memorySlice:memorySliceList){
            if (memorySlice.getStart()==progress.getStart()){
                memorySliceList.remove(memorySlice);
                return;
            }
        }
    }

    public void addLabel(Progress progress){

        Label tmp = new Label(progress.getPID());

        //初始化样式
        StringBuffer style = new StringBuffer();
        style.append("-fx-alignment: center;");
        style.append("-fx-background-color: #5b6bb2;");;
        style.append("-fx-border-color: #b8afff;");
        style.append("-fx-border-width: 2;");
        style.append("-fx-pref-height: 46;");
        style.append("-fx-font-size:  18;");
        style.append("-fx-text-fill:  #bbbfc9;");

        //如果从0开始设置圆角
        if(progress.getStart()==0){
            style.append("-fx-border-radius: 13 0 0 13;");
            style.append("-fx-background-radius: 13 0 0 13;");
        }

        //设置位置
        int x = 375+progress.getStart()*716/8192-1;
        int y = 112;
        tmp.setLayoutX(x);
        tmp.setLayoutY(y);

        //设置长度
        int width = progress.getSize()*716/8192+2;
        tmp.setPrefWidth(width);

        //添加到面板上
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tmp.setStyle(style.toString());
                AnchorPane root = (AnchorPane) mainApplication.getRoot();
                root.getChildren().add(tmp);
            }
        });
        MainApplication.getPLMap().put(progress,tmp);
    }

    public void removeLabel(Progress progress) throws RuntimeException{
        HashMap<Progress, Label> PLMap = MainApplication.getPLMap();
        Label label = PLMap.get(progress);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                AnchorPane root = (AnchorPane) mainApplication.getRoot();
                root.getChildren().remove(label);
            }
        });
        PLMap.remove(progress);
    }
}