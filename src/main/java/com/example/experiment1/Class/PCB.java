package com.example.experiment1.Class;

public class PCB {
    private String PID;
    private int status;//0->ready 1->running 2->waiting 3->hangup
    private int priority;
    private double TTime;
    private double RTime;
    private int size;
    private int start;
    private int next;
    private String former;

    public PCB(){}
    public PCB(String PID, int status, String former, int priority, double TTime, double RTime, int size, int start, int next){
        this.PID = PID;
        this.status=status;
        this.former = former;
        this.TTime = TTime;
        this.RTime = RTime;
        this.size=size;
        this.start=start;
        this. priority = priority;
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

    public void setTTime(int TTime) {
        this.TTime = TTime;
    }

    public double getTTime() {
        return TTime;
    }

    public void setRTime(int RTime) {
        this.RTime = RTime;
    }

    public double getRTime() {
        return RTime;
    }

    public void setPriority(int Priority) {
        this.priority = Priority;
    }

    public int getPriority() {
        return priority;
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
