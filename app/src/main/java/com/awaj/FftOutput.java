package com.awaj;

/**Radix-2 FftOutput implementation
 * Here the input to the fft is a real number and output is complex number
 */

public class FftOutput {

    final static float MAX_AMPLITUDE_VALUE = 32768.0f;
    /**
     * @param: float[], float[]
     * @return: void
     * @exception: **/
    public static void fft(float[] real, float[] imag)
    {
        int n,fftPoint;
        int length, noOfStages;
        float alpha, cosPart,sinPart,multiplierReal,multiplierImag;

        length= real.length;//length of fft
        noOfStages = (int)(Math.log(length) / Math.log(2));// number of fft noOfStages
        alpha=(float)(-2*Math.PI);// for calculating twiddle factor

        fftReverse(real,length,noOfStages);// function for reversing bit position in the given data

        /** FFT calculation part */
        n=0;
        fftPoint = 1;

        /** First loop that runs till the fft noOfStages */
        for (int i=0; i < noOfStages; i++) {
            n = fftPoint;
            fftPoint += fftPoint;

            /** loop to calculate the corresponding twiddle factor values for each noOfStages */
            for (int j=0; j < n; j++) {
                cosPart = (float)Math.cos(alpha*j/fftPoint);
                sinPart = (float)Math.sin(alpha*j/fftPoint);
                /** multiplication of values */
                for (int k=j; k < length; k=k+fftPoint) {
                    multiplierReal = cosPart*real[k+n] - sinPart*imag[k+n];
                    multiplierImag= sinPart*real[k+n] + cosPart*imag[k+n];
                    /** odd part multiplication */
                    real[k+n] = real[k] - multiplierReal;
                    imag[k+n] = imag[k] - multiplierImag;
                    /** even part multiplication */
                    real[k] = real[k] + multiplierReal;
                    imag[k] = imag[k] + multiplierImag;
                }
            }
        }
    } /** End of fft */

    /**
     *
     * @param: float[], int, int
     * @return: void
     */
    /** for reversing bit values of the input */
    private static void fftReverse(float[] real,int length, int noOfStages){
        float temp=0;
        int i;
        for(i =0;i<length/2;i++){
            int j= reverseBits(i,noOfStages);
            temp = real[i];
            real[i] = real[j];
            real[j] = temp;
        }
    } /** End of fftReverse */

    /**
     *
     * @param: int, int
     * @return: int
     */
    /** function that returns the bit reversed value of given number*/
    private static int reverseBits(int number, int noOfStages)
    {
        int reversedBit = 0,i;
        for (i = 0; i < noOfStages; i++)
        {
            int nextBit = number & 1;
            number >>= 1;
            reversedBit <<= 1;
            reversedBit |= nextBit;
        }
        return reversedBit;
    } /** End of reverseBits */

    /**
     *
     * @param : float[]
     * @return: float[]
     */
    /** Function to check if the given fft input array length is a power of 2 *
     *  If not the input is zero padded to make the length a power of 2  */
    private static float[] makePowerOf2(float[] input) {
        int length =input.length;

        /** flag to check if the length of the array is a power of 2 or not, flag = 0 initially */
        int flag=0;
        float stage=(float)(Math.log(length) / Math.log(2));
        /** when the value of length is not a power of 2 set flag =1*/
        if((stage-(int)stage)!=0){
            flag=1;
        }
        /** if length is not a power of 2 *
         * calculating the value of new length to a closest value i.e the power of 2*/
        if(flag==1){
            int newLength = 1;
            while(newLength < length)
            {newLength*=2;}

            float[] x = new float[newLength];// making an array of newLength
            for (int i = 0; i < newLength; i++) {
                if (i<length){
                    x[i] =input[i];} // till the value of previous length the input is converted to complex number
                else { x[i] =0;} // zero padding till the number is power of 2 (from previous length to new length
            }
            return x;
        }
        else{// when length is already a power of 2 where flag =0
            return input;
        }
    } /** End of makePowerOf2 */

    /**
     * @param: float[]
     * @return:  void
     */
    /** Low pass filter for frequency */

    private static void windowing(float[] input){
        float w[]=new float[input.length];
        for(int i=0; i<input.length; i++){
            w[i] = (float)(0.42 - 0.5 * Math.cos(2* Math.PI * i / (w.length - 1)) + 0.08 * Math.cos(4 * Math.PI * i / (w.length - 1)));
            input[i]*=w[i];
        }
    } /** End of Windowing */

    /**
     * @param: float[], float[]
     * @return: float[]
     */
    private static float[] absoluteValue(float[] re, float[] im){
        float[] abs = new float[re.length];
        for(int i=0;i<re.length;i++){
            abs[i]=(float)(Math.sqrt(Math.pow(re[i],2)+Math.pow(im[i],2)));
        }return abs;
    } /** End of absoluteValue */

    /**
     * @param: float[]
     * @return: void
     */
    /** normalizing the amplitude value -
     **  dividing each amplitude value by the Maximum amplitude value*/
    private static void normalize(float[] input){
        for(int i=0;i<input.length;i++){
            input[i]=(input[i]/MAX_AMPLITUDE_VALUE);
        }

    } /** End of normalize */

    /**
     * @param: float[]
     * @return: float[]
     */
    public static float[] callMainFft(float[] input){
        normalize(input);
        windowing(input);
        float[] fftInputReal = makePowerOf2(input);
        float[] fftInputImag = new float[fftInputReal.length];
        for(int i=0;i<fftInputReal.length;i++){
            fftInputImag[i]=0;
        }
        fft(fftInputReal, fftInputImag);
        float[] fftOutput= absoluteValue(fftInputReal,fftInputImag);
        return fftOutput;
    }/** End of callMainFft */
}