package com.example.experiment1.Service;

import com.example.experiment1.Class.MemorySlice;
import com.example.experiment1.Class.PCB;
import com.example.experiment1.Class.Progress;
import com.example.experiment1.Class.SystemLog;
import com.example.experiment1.Controller.MainController;
import com.example.experiment1.MainApplication;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import static java.lang.Math.abs;

public class ProgressControlService extends Thread{
    MainController mainController;
    private ObservableList<PCB> PCBList;
    private ObservableList<Progress> progressesList;
    private ObservableList<MemorySlice> memorySliceList;
    public ProgressControlService(MainController mainController){
        this.mainController=mainController;
    }

    public void setList(ObservableList<PCB> PCBList, ObservableList<Progress> progressesList, ObservableList<MemorySlice> memorySliceList) {
        this.PCBList = PCBList;
        this.progressesList = progressesList;
        this.memorySliceList = memorySliceList;
    }

    @Override
    public void run(){

        while (true) {
            //暂停
            while(mainController.getMainApplication().isPause()){
                try {
                    Thread.sleep(5000);
                    System.out.println("Pauseing, and pause = "+mainController.getMainApplication().isPause());

                    //获得时间
                    Date tmpDate = new Date(System.currentTimeMillis());
                    String tmpDateString = mainController.getMainApplication().getDdf().format(tmpDate);
                    mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,"NULL","System Is Pausing."));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            //查看是否所有Progress已运行完
            while(PCBList.size()==0);

            //寻找PCB队列第一位PCB对应的Progress
            String tmpID = PCBList.get(0).getPID();
            int i;
            for (i = progressesList.size() - 1; i >= 0; i--) {
                if (progressesList.get(i).getPID().equals(tmpID))
                    break;
            }
            Progress tmpProgress = progressesList.get(i);

            //包括更新RunningProgress状态，获得新进程并开始运行
            tmpProgress.setStatus(1);
            tmpProgress.getPcb().setState("running");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mainController.refreshBoard();
                }
            });

            //获得时间
            Date tmpDate = new Date(System.currentTimeMillis());
            String tmpDateString = mainController.getMainApplication().getDdf().format(tmpDate);

            if(tmpProgress.isRunnable()){
                mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,tmpProgress.getPID(),"Progress Is Now Starting."));
                if(!tmpProgress.isInterrupted()) {
                    tmpProgress.start();
                }else{
                    tmpProgress.run();
                }
            }else{
                mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,tmpProgress.getPID(),"Progress Is Now Starting Again."));
                tmpProgress.setRunnable(true);
            }

            //进程忙等当前progress运行结束(相当于监听progress结束了)
            long startTime = System.currentTimeMillis();
            while (tmpProgress.isAlive()&&(System.currentTimeMillis()-startTime < (mainController.getMainApplication().getTimeSlice())*1000));
            Done(tmpProgress);
        }
    }

    //处理progress运行结束
    public void Done(Progress tmpProgress){
        //获得时间
        Date tmpDate = new Date(System.currentTimeMillis());
        String tmpDateString = mainController.getMainApplication().getDdf().format(tmpDate);

        //Ageing:其余没有完成的进程优先级加一
        for(PCB p:PCBList){
            //如果p不处于挂起
            if(!p.getStatus().equals("Hang Up")) {
                int pri = p.getPriorityy();
                for (Progress pro : progressesList)
                    if (pro.getPID() == p.getPID())
                        pro.setPriorityy(pri + 1);
            }
        }

        //进程完成
        if(abs(tmpProgress.getRTime())<=0.001){

            mainController.removeLabel(tmpProgress);
            tmpProgress.setRTime(0);
            tmpProgress.setStatus(4);
            tmpProgress.setRTime(0);
            mainController.releaseMemory(tmpProgress);
            PCBList.remove(0);

            //向PCB队列增加新进PCB
            int reserve = mainController.getMainApplication().getReserveProgress();
            while(reserve<progressesList.size()&&PCBList.size()<6) {
                Progress newProgress = progressesList.get(reserve);
                PCB tmpPCB = newProgress.createPCB();
                mainController.setMemorySliceLocation(newProgress);

                //查看是否有前驱并置为wait
                for(Progress pro:progressesList){
                    if(pro.getPID().equals(newProgress.getFormer())&&!(pro.getStatus().equals("Done"))){
                        newProgress.setStatus(2);
                        newProgress.getPcb().setState("waiting");
                        mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,newProgress.getPID(),"Progress is Waiting Cause Progress: "+pro.getPID()+" Hasn't Been Done"));
                        break;
                    }
                    newProgress.setStatus(0);
                }
                tmpPCB.setState("ready");

                //查看是否为前驱并释放所有后继
                for(PCB pcb:PCBList){
                    for(Progress pro:progressesList){
                        if(pro.getPcb()==pcb&&pro.getFormer().equals(tmpProgress.getPID())){
                            pro.setStatus(0);
                            pro.getPcb().setState("ready");
                            mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,pro.getPID(),"Progress Stop Waiting Cause Progress: "+tmpProgress.getPID()+" Has Been Done"));
                        }
                    }
                }

                mainController.getMainApplication().insertPCB(tmpPCB);
                mainController.getMainApplication().setReserveProgress(reserve + 1);
            }
            //系统日志更新
            mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,tmpProgress.getPID(),"Progress Is Now Done."));
        }else if(tmpProgress.isHangUp()){//程序被挂起
            tmpProgress.setStatus(3);
            tmpProgress.getPcb().setState("hang up");
            PCBList.remove(0);
            PCBList.add(tmpProgress.getPcb());
            mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,tmpProgress.getPID(),"Progress Has Been Hung Up."));
        }else {//此else只出现在时间片耗尽且程序未跑完的情况
            tmpProgress.setRunnable(false);
            tmpProgress.setStatus(0);
            tmpProgress.getPcb().setState("ready");
            PCBList.remove(0);
            PCBList.add(tmpProgress.getPcb());
            mainController.getMainApplication().getLogList().add(new SystemLog(tmpDateString,tmpProgress.getPID(),"Time Slice Has Been Ran Out."));
        }

        //优先级排序
        if(MainApplication.isPrivilege()){
            PCBList.sort(new Comparator<PCB>() {
                @Override
                public int compare(PCB o1, PCB o2) {
                    if(o1.getPriorityy()<o2.getPriorityy()) return 1;
                    else if (o1.getPriorityy()==o2.getPriorityy()) return 0;
                    else return -1;
                }
            });
        }

        //Waiting压入PCB底部
        for(int i =PCBList.size();i>0;i--){
            PCB tmpPCB = PCBList.get(i-1);
            //如果p处于挂起
            if(tmpPCB.getStatus().equals("Waiting")) {
                //查找PCB对应进程
                PCBList.remove(i-1);
                PCBList.add(tmpPCB);
            }
        }


        //挂起程序压入PCB队列底部
        for(int i =PCBList.size();i>0;i--){
            PCB tmpPCB = PCBList.get(i-1);
            //如果p处于挂起
           // System.out.println(tmpPCB.getPID()+"--status:"+tmpPCB.getStatus());
            if(tmpPCB.getStatus().equals("Hang Up")) {
                //查找PCB对应进程
                PCBList.remove(i-1);
                PCBList.add(tmpPCB);
            }
        }

        //规范剩余时间
        mainController.getMainApplication().df.format(tmpProgress.getRTime());
        
        mainController.progressOverview.refresh();
    }

    //处理progress轮转
    public void turn(){

    }
}
