package com.example.experiment1.Class;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Progress {
    private PCB pcb;
    private String PID;
    private int status;//0->ready 1->running 2->waiting 3->hangup 4->done
    private int priority;
    private double TTime;
    private double RTime;
    private int size;
    private int start;
    private int next;
    private String former;

    public Progress(){;}
    public Progress(String PID, int status, String former, int priority, double TTime, double RTime, int size, int start, int next){
        this.pcb = new PCB(PID, status, former, priority, TTime, RTime, size, start, next);
        this.PID = PID;
        this.status = status;
        this.former = former;
        this.priority = priority;
        this.TTime = TTime;
        this.RTime = RTime;
        this.size = size;
        this.next = next;
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
            default:return "Error!";
        }
    }

    public PCB getPcb() {
        return pcb;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
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
        this.start = start;
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

    public void run(){
        this.pcb.setState("running");
    }

    public String ready(){
        this.pcb.setState("ready");
        return this.pcb.getPID();
    }

    public String waite(){
        this.pcb.setState("waiting");
        return this.pcb.getPID();
    }

}
