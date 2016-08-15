package com.awaj;

/**
 * Created by amitgupta on 8/15/2016.
 */
public interface AudioRecordFileDecibelFrequencyNoteGraphListener {

    void processExecuting(String decibel,String frequency , String note,float[] ampValues,float[] freqValues);
    void processExecuted();

}
