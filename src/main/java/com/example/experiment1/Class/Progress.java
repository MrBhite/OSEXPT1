package com.example.experiment1.Class;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Progress {
    private PCB pcb;
    public String PID;

    public Progress(){;}
    public Progress(String PName, String PID, int RTime, int priority, int state, int PNext){
        this.pcb = new PCB(PName, PID, RTime, priority, state, PNext);
        this.PID = PID;
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
