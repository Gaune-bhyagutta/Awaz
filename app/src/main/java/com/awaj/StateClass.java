package com.awaj;

/**
 * Created by amitgupta on 8/11/2016.
 */

public class StateClass {

    boolean isRecording=false;
    boolean isPlaying=false;

    static int SingleObjectCount =0;
    static StateClass object;

    private StateClass() {

    }

    public static StateClass getState(){
        if(SingleObjectCount ==0){
            object = new StateClass();
            SingleObjectCount++;
            return  object;
        }
        else
            return object;
    }

    public boolean getRecoderingState(){
        return isRecording;
    }
    public boolean getPlayingState(){
        return isPlaying;
    }

    public void setRecoderingState(boolean isRecording){
        this.isRecording=isRecording;
    }
    public void setPlayingState(boolean isPlaying){
        this.isPlaying=isPlaying;
    }

}
