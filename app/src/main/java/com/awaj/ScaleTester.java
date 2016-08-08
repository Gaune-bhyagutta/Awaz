package com.awaj;

/**
 * Created by imas on 8/8/16.
 */
public class ScaleTester {
    public static float getFundamentalFrequency(float[] amplitude) {
        float fundamentalFrequency=0;
        double correction_factor=1.082;
        int i_max=0;
        float max=0;
        for(int i=0;i<amplitude.length/2;i++){
            if(max < amplitude[i]){
                max=amplitude[i];
                i_max =i;
            }
        }
        fundamentalFrequency= ((i_max)*MainActivity.resolution*(float)correction_factor);

        return fundamentalFrequency;
    }
}
