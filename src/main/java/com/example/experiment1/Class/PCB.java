package com.example.experiment1.Class;

public class PCB {
    private String PName;
    private String PID;
    private int RTime;
    private int Priority;
    private int state;//0->ready 1->running 2->waiting
    private int PNext;

    public PCB(){}
    public PCB(String PName, String PID, int RTime, int Priority, int state, int PNext){
        this.PName = PName;
        this.PID = PID;
        this.RTime = RTime;
        this. Priority = Priority;
        this.PNext = PNext;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getPName() {
        return PName;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPID() {
        return PID;
    }

    public void setRTime(int RTime) {
        this.RTime = RTime;
    }

    public int getRTime() {
        return RTime;
    }

    public void setPrivilege(int Priority) {
        this.Priority = Priority;
    }

    public int getPrivilege() {
        return Priority;
    }

    public void setState(String state) {
        switch (state){
            case "ready" -> this.state=0;
            case "running" -> this.state=1;
            case "waiting" -> this.state=2;
        }
    }

    public int getState() {
        return state;
    }

    public void setPNext(int PNext) {
        this.PNext = PNext;
    }

    public int getPNext() {
        return PNext;
    }
}
