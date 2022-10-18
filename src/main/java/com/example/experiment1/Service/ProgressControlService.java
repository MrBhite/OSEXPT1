package com.example.experiment1.Service;

import com.example.experiment1.Class.PCB;
import com.example.experiment1.Class.Progress;
import com.example.experiment1.Controller.MainController;
import javafx.application.Platform;
import javafx.collections.ObservableList;

public class ProgressControlService extends Thread{
    MainController mainController;
    private ObservableList<PCB> PCBList;
    public ProgressControlService(MainController mainController){
        this.mainController=mainController;
    }

    public void setPCBList(ObservableList<PCB> PCBList) {
        this.PCBList = PCBList;
    }

    @Override
    public void run(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainController.setRunning();
            }
        });
        Progress tmp = mainController.getMainApplication().getProgressList().get(0);
        tmp.getPcb().setState("running");
        tmp.start();

        //进程忙等当前progress运行结束(相当于监听progress结束了)
        while(tmp.isAlive());
        Done(tmp);
    }

    //处理progress运行结束
    public void Done(Progress tmp){
        if(tmp.getRTime()<=0){
            tmp.setStatus(4);
            tmp.setRTime(0);
        }
        mainController.progressOverview.refresh();
    }

    //处理progress轮转
    public void turn(){

    }
}
