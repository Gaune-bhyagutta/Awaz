package com.example.keshavdulal.a14_simple_drawing;

/**
 * Created by imas on 7/6/16.
 */
public class FrequencyValue {
    public static float getFundamentalFrequency (float[] amplitude){
        float fundamentalFrequency;
        double correctFactor = 0.986;
        float maxAmplitude=0;int i_max=0;
        float[] harmonics1= downSample(amplitude,2);
        float[] harmonics2= downSample(amplitude,3);
        float[] harmonics3= downSample(amplitude,4);
        float[] harmonics4= downSample(amplitude,5);
        for(int i=0;i<amplitude.length;i++){
            amplitude[i]= amplitude[i]*harmonics1[i]*harmonics2[i]*harmonics3[i]*harmonics4[i];
        }

        for(int i=0;i<amplitude.length;i++){
            if(amplitude[i]>maxAmplitude){
                maxAmplitude=amplitude[i];
                i_max=i;
            }
        }

        fundamentalFrequency= (float)(i_max*MainActivity.resolution*correctFactor);
        return fundamentalFrequency;
    }

    public static float[] downSample(float[] input, int downSamplingRate){
        int N= input.length;
        float[] downSampledSignal = new float[N];
        for(int i=0;i<N;i++){
            if((i)*downSamplingRate<N) {
                downSampledSignal[i] = input[(i)*downSamplingRate];
            }
            else{
                downSampledSignal[i]=0;
            }
        }
        return downSampledSignal;
    }

//    public static float getFundamentalFrequency (float[] amplitude){
//        float fundamentalFrequency;
//        float maxAmplitude=0;
//        float[] sum= new float[amplitude.length];
//        int i_max=0;
//        float x= MainActivity.samplingRate/(amplitude.length*2);
//        for(int i=0;i<amplitude.length/8;i++){
//            sum[i] = amplitude[i]*amplitude[2*i]*amplitude[3*i];
//            if(sum[i]>maxAmplitude){
//                maxAmplitude=sum[i];
//                i_max=i;
//            }
//        }
//        fundamentalFrequency= i_max*MainActivity.resolution;
//
//        return fundamentalFrequency;
//    }

}