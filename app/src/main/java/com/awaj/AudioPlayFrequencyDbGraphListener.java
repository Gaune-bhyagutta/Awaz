package com.awaj;

/**
 * Created by anup on 8/16/16.
 */
public interface AudioPlayFrequencyDbGraphListener extends AudioPlayFrequencyDbListener {
   void processExecuting(Float frequency, String note,Float db,float[] audioFloatsForFFT,float[] audioFloatsForAmp);
    void processExecuted();
}
