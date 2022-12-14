package com.example.experiment1.Class;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DecimalFormat;

public class Progress extends Thread {
    private PCB pcb;
    private String PID;
    private int status;//0->ready 1->running 2->waiting 3->hangUp 4->done 5->Out
    private int priorityy;
    private double TTime;
    private double RTime;
    private int size;
    private int start;
    private int next;
    private String former;
    private boolean runnable;
    private boolean hangUp;

    public Progress(){;}
    public Progress(String PID, int status, String former, int priorityy, double TTime, double RTime, int size, int start, int next){
        this.PID = PID;
        this.status = status;
        this.former = former;
        this.priorityy = priorityy;
        this.TTime = TTime;
        this.RTime = RTime;
        this.size = size;
        this.start = start;
        this.next = next%8192;
        this.pcb=null;
        this.runnable = true;
        this.hangUp = false;
    }

    public PCB createPCB(){
        this.pcb = new PCB(PID, status, former, priorityy, TTime, RTime, size, start, next%8192);
        return this.pcb;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPID(){
        return this.PID;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus() {
        switch (status){
            case 0:return "Ready";
            case 1:return "Running";
            case 2:return "Waiting";
            case 3:return "Hang Up";
            case 4:return "Done";
            case 5:return "Out";
            default:return "Error!";
        }
    }

    public PCB getPcb() {
        return pcb;
    }

    public void setPriorityy(int priorityy) {
        this.priorityy = priorityy;
        this.pcb.setPriorityy(priorityy);
    }

    public int getPriorityy() {
        return priorityy;
    }

    public void setTTime(double TTime) {
        this.TTime = TTime;
    }

    public double getTTime() {
        return TTime;
    }

    public void setRTime(double RTime) {
        this.RTime = RTime;
    }

    public double getRTime() {
        return RTime;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setStart(int start) {
        this.start = start%8192;
    }

    public int getStart() {
        return start;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public void setFormer(String former) {
        this.former = former;
    }

    public String getFormer() {
        return former;
    }

    @Override
    public void run(){
        int i = (int)(this.RTime*10);
        for( ; i>0; i--){
            //???????????????
            if(hangUp)
                break;

            //??????????????????????????????
            while(!runnable)
            {
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            this.RTime = Double.parseDouble(new DecimalFormat( "0.0").format(this.RTime-0.1));
            System.out.println("PID:"+this.PID+"---remain:"+this.RTime+"---"+this.status);
            this.pcb.setRTime(RTime);
        }
        this.interrupt();
    }

    public String ready(){
        this.pcb.setState("ready");
        return this.pcb.getPID();
    }

    public String waite(){
        this.pcb.setState("waiting");
        return this.pcb.getPID();
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
    }

    public boolean isHangUp() {
        return hangUp;
    }

    public void setHangUp(boolean hangUp) {
        this.hangUp = hangUp;
    }
}
