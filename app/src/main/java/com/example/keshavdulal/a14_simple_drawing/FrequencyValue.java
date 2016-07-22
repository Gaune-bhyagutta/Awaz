package com.example.keshavdulal.a14_simple_drawing;

/**
 * Created by imas on 7/6/16.
 */
public class FrequencyValue{

//    public static float[] getFrequency (float[] amplitude){
//        float[] frequency = new float[amplitude.length];
//        for(int i=0;i<amplitude.length;i++){
//            frequency[i]= 44100*i/(2*amplitude.length);
//        }
//        return frequency;
//    }
    public static float getFundamentalFrequency (float[] amplitude){
        float fundamentalFrequency;
        float maxAmplitude=0;
        float[] sum= new float[amplitude.length];
        int i_max=0;
        float x= MainActivity.samplingRate/(amplitude.length*2);
        for(int i=0;i<amplitude.length/8;i++){
            sum[i] = amplitude[i]*amplitude[2*i]*amplitude[3*i];
            if(sum[i]>maxAmplitude){
                maxAmplitude=sum[i];
                i_max=i;
            }
        }

        fundamentalFrequency= i_max*x;

        return fundamentalFrequency;
    }
}
