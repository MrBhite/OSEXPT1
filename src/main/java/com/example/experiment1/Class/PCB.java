package com.example.experiment1.Class;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PCB {
    private String PID;
    private int status;//0->ready 1->running 2->waiting 3->hangup
    private int priorityy;
    private double TTime;
    private double RTime;

    private DoubleProperty RTimeProperty;
    private int size;
    private int start;
    private int next;
    private String former;

    public PCB(){}
    public PCB(String PID, int status, String former, int priorityy, double TTime, double RTime, int size, int start, int next){
        this.PID = PID;
        this.status=status;
        this.former = former;
        this.TTime = TTime;
        this.RTime = RTime;
        this.RTimeProperty = new SimpleDoubleProperty(RTime);
        this.size=size;
        this.start=start;
        this. priorityy = priorityy;
        this.next = next;
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



    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPID() {
        return PID;
    }

    public void setTTime(double TTime) {
        this.TTime = TTime;
    }

    public double getTTime() {
        return TTime;
    }

    public void setRTime(double RTime) {
        this.RTime = RTime;
        this.RTimeProperty.setValue(RTime);
    }

    public double getRTime() {
        return RTime;
    }

    public DoubleProperty getRTimeProperty() {
        return RTimeProperty;
    }

    public void setPriorityy(int Priorityy) {
        this.priorityy = Priorityy;
    }

    public int getPriorityy() {
        return priorityy;
    }

    public void setState(String state) {
        switch (state){
            case "ready" -> this.status=0;
            case "running" -> this.status=1;
            case "waiting" -> this.status=2;
            case "hangup" -> this.status=3;
            case "done"->this.status=4;
        }
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

    public void setNext(int PNext) {
        this.next = PNext;
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
}
