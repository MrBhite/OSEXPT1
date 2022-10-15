package com.example.experiment1.Class;

public class SystemLog {
    private String time;
    private String PID;
    private String content;

    public SystemLog(String time, String PID, String content){
        this.time=time;
        this.PID=PID;
        this.content=content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPID() {
        return PID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
