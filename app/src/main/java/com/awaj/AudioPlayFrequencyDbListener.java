package com.awaj;

/**
 * Created by anup on 8/12/16.
 */
public interface AudioPlayFrequencyDbListener extends AudioPlayFrequencyListener{
    void processExecuting(Float frequency, String note,Float db);
    void processExecuted();

}
