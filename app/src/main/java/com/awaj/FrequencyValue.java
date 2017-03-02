package com.awaj;

public class FrequencyValue {

    /**
     * @param: float[]
     * @return: float
     */
    public static float getFundamentalFrequency(float[] amplitude) {
        float fundamentalFrequency;
        double length = amplitude.length;
        double i_max=0;
        int i;
        float max=0;
        for(i=0;i<amplitude.length/2;i++){
            if(max < amplitude[i]){
                max=amplitude[i];
                i_max =i;
            }
        }
        double frequency =((i_max)*((double)MainActivity.SAMPLE_RATE_IN_HZ/length));
        fundamentalFrequency= (float)frequency;
        return fundamentalFrequency;
    }/** END OF getFundamentalFrequency**/

    /**
     * @param: float[]
     * @return: float
     */
    public static float getDownSampledFrequency (float[] amplitude){

        float fundamentalFrequency, maxAmplitude=0;
        double correctionFactor = 1;
        double i_max=0, length =amplitude.length;
        float[] harmonics1= downSample(amplitude,2);
        float[] harmonics2= downSample(amplitude,3);
        float[] harmonics3 = downSample(amplitude, 4);
        float[] harmonics4 = downSample(amplitude, 5);
        float[] sum=new float[amplitude.length];

        for(int i=0;i<length/2;i++){
            sum[i]=amplitude[i]*harmonics1[i]*harmonics2[i]*harmonics3[i]*harmonics4[i];
            if(sum[i]>maxAmplitude){
                maxAmplitude=sum[i];
                i_max=i;
            }
        }
        double frequency =((i_max)*((double)MainActivity.SAMPLE_RATE_IN_HZ/length)*correctionFactor);
        fundamentalFrequency= (float)frequency;
        return fundamentalFrequency;

    } /** End of getDownSampledFrequency */


    /**
     * @param: float[], int
     * @return: float[]
     */
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

    }/** End of downSample */

    /**
     * @param: float[]
     * @return: float
     */
    public static float findMaxValue(float[] amplitude){
        int i;
        float max=0;
        for(i=0;i<amplitude.length/2;i++){
            if(max < amplitude[i]){
                max=amplitude[i];
            }
        }
        return max;
    }/** End of findMaxValue */
}
//    public static int[] findLargest(float[] array, int numberOfMaximumValues){
//        int length= array.length/2;
//        float max=0 , largest=100;
//        int i_max=0;
//        int[] indices= new int[numberOfMaximumValues];
//        for(int i=0;i<indices.length;i++){
//            for(int j=0;j<length;j++){
//                if(array[j]<largest && array[j]>max){
//                    max=array[j];
//                    i_max=j;
//                }
//            }
//            largest=max;
//            max =0;
//            indices[i]=i_max;
//        }
//        return indices;
//    }
