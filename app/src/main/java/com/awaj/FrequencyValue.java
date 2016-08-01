package com.awaj;

public class FrequencyValue{

    public static float getFundamentalFrequency (double[] amplitude){
        float fundamentalFrequency;
        float maxAmplitude=0;
        float[] sum= new float[amplitude.length];
        int i_max=0;
        float x= MainActivity.SAMPLE_RATE_IN_HZ/(amplitude.length*2);
        for(int i=0;i<amplitude.length/8;i++){
            sum[i] = (float) (amplitude[i]*amplitude[2*i]*amplitude[3*i]);
            if(sum[i]>maxAmplitude){
                maxAmplitude=sum[i];
                i_max=i;
            }
        }

        fundamentalFrequency= i_max*x;

        return fundamentalFrequency;
    }


}
