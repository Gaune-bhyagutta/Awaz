package com.example.keshavdulal.a14_simple_drawing;

/**
 * Created by imas on 7/6/16.
 */
public class FrequencyValue{

    public static double[] getFrequency (double[] amplitude){
        double[] frequency = new double[amplitude.length];
        for(int i=0;i<amplitude.length;i++){
            frequency[i]= 44100*i/(2*amplitude.length);
        }
        return frequency;
    }

    public static double getFundamentalFrequency (double[] amplitude){
        double fundamentalFrequency;
        double maxAmplitude=0;
        for(int i=0;i<amplitude.length;i++){
            if(maxAmplitude<amplitude[i]){
                maxAmplitude=amplitude[i];
            }
        }
        fundamentalFrequency= maxAmplitude* (44100/(2*amplitude.length)-(44100/2));
        return fundamentalFrequency;
    }
}
