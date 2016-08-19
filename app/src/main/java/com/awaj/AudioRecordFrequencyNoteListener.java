package com.awaj;

/**
 * Created by amitgupta on 8/15/2016.
 */
public interface AudioRecordFrequencyNoteListener {

    void processExecuting(String frequency,String note, String nearestNote, String nearestNoteFrequency);
    void processExecuted();
}
