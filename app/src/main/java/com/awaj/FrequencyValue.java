package com.awaj;

public class FrequencyValue{

    public static double[] getFrequency (double[] amplitude){
        double[] frequency = new double[amplitude.length];
        for(int i=0;i<amplitude.length;i++){
            frequency[i]= 44000*i/amplitude.length;
        }
        return frequency;
    }
}