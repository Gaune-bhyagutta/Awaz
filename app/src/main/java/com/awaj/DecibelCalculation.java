package com.awaj;

import android.util.Log;

/**
 * Created by amitgupta on 8/5/2016.
 */
public class DecibelCalculation {

    private final String TAG = DecibelCalculation.class.getSimpleName();

    float decibel =0;
    float decibelTotal=0;
    int decibelCount=0;
    public float decibelCalculation(short [] audioData){


        for(int i=0;i<audioData.length;i++) {
            if (audioData[i] != 0) {
                decibel = (float) (20 * Math.log10(Math.abs((int) audioData[i]) / 32678.0));
                decibelTotal = decibel + decibelTotal;
                decibelCount++;
            }

        }
        float decibelAverage = decibelTotal / decibelCount;
       //Log.d(TAG,String.valueOf(decibelAverage));
        return decibelAverage;

    }
}
