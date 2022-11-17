package com.example.experiment1.Class;

public class MemorySlice {
    private  String PID;
    private int start;
    private int limit;
    private int end;

    public MemorySlice(String pid, int start, int limit){
        PID = pid;
        this.start = start;
        this.limit = limit;
        this.end = start + limit;
    }

    public MemorySlice(PCB pcb){
        this.PID = pcb.getPID();
        this.start = pcb.getStart();
        this.limit = pcb.getSize();
        this.end = start + limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }
}
