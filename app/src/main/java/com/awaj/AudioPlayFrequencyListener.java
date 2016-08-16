package com.awaj;

/**
 * Created by anup on 8/12/16.
 */
public interface AudioPlayFrequencyListener extends AudioPlayMainListener{

    void processExecuting(Float frequency, String note);
    void processExecuted();
}
