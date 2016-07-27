package com.example.keshavdulal.a14_simple_drawing;

/**
 * Created by imas on 7/6/16.
 */
public class FrequencyValue {
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
        fundamentalFrequency= i_max*MainActivity.resolution;

        return fundamentalFrequency;
    }

//    public static float getFundamentalFrequency (float[] amplitude){
//        float fundamentalFrequency;
//        float maxAmplitude=0;
//        int i_max=0;
//        for(int i=0;i<amplitude.length;i++){
//            if(amplitude[i]>maxAmplitude){
//                maxAmplitude=amplitude[i];
//                i_max=i;
//            }
//        }
//        fundamentalFrequency= MainActivity.resolution*i_max;
//        return fundamentalFrequency;
//    }
}