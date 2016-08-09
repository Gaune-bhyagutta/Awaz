package com.awaj;

import java.util.Arrays;

public class FrequencyValue {
    public static float getFundamentalFrequency (float[] amplitude){
//        int[]  peakIndices = FrequencyValue.findLargest(amplitude,5);
//        System.out.println("Amplitude peak Values");
//        for(int i=0;i<peakIndices.length;i++) {
//            System.out.println(peakIndices[i] + " amplitude = "+ amplitude[peakIndices[i]] + " frequency "+ (peakIndices[i]*MainActivity.resolution));
//        }
        float fundamentalFrequency, maxAmplitude=0;
        float correctFactor = 1.082f;
        //float correctFactor=1;
        int i_max=0, length =amplitude.length/2;

        float[] harmonics1= downSample(amplitude,2);
        float[] harmonics2= downSample(amplitude,3);
        float[] sum=new float[amplitude.length];
        for(int i=0;i<length;i++){
            sum[i]=amplitude[i]*harmonics1[i]*harmonics2[i];

            if(sum[i]>maxAmplitude){
                maxAmplitude=sum[i];
                i_max=i;
            }

        }
        fundamentalFrequency= (i_max*(MainActivity.SAMPLE_RATE_IN_HZ/amplitude.length))*correctFactor;
        return fundamentalFrequency;
    }

    public static float[] downSample(float[] input, int downSamplingRate){
        int N= input.length/2;
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

    public static int[] findLargest(float[] array, int numberOfMaximumValues){
        int length= array.length/2;
        float max=0 , largest=100;
        int i_max=0;
        int[] indices= new int[numberOfMaximumValues];
        for(int i=0;i<indices.length;i++){
            for(int j=0;j<length;j++){
                if(array[j]<largest && array[j]>max){
                    max=array[j];
                    i_max=j;
                }
            }
            largest=max;
            max =0;
            indices[i]=i_max;
        }
        return indices;
    }
//    public static float getFundamentalFrequency(float[] amplitude) {
//        float fundamentalFrequency=0;
//        double correction_factor=1.082;
//        int i_max=0;
//        float max=0;
//        for(int i=0;i<amplitude.length/2;i++){
//            if(max < amplitude[i]){
//                max=amplitude[i];
//                i_max =i;
//            }
//        }
//        fundamentalFrequency= ((i_max)*(MainActivity.SAMPLE_RATE_IN_HZ/amplitude.length)*(float)correction_factor);
//
//        return fundamentalFrequency;
//    }
}
