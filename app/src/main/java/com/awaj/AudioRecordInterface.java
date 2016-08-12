package com.awaj;

/**
 * Created by amitgupta on 8/11/2016.
 */
public interface AudioRecordInterface {
   // void AudioRecordInterface();
    void processExecuting(float decibel, float frequency, String notes);
    void processExecuted();

}

