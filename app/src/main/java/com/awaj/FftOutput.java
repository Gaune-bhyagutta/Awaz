package com.awaj;

/**Radix-2 FftOutput implementation
 * Here the input to the fft is a real number and output is complex number
 */

public class FftOutput {

   public static void fft(float[] real, float[] imag)
    {
        int n,fftPoint;
        int length, noOfStages;
        float alpha, cosPart,sinPart,multiplierReal,multiplierImag;

        length= real.length;//length of fft
        noOfStages = (int)(Math.log(length) / Math.log(2));// number of fft noOfStages
        alpha=(float)(-2*Math.PI);// for calculating twiddle factor

        fftReverse(real,length,noOfStages);// function for reversing bit position in the given data

        //FFT calculation part
        fftPoint = 1;
        for (int i=0; i < noOfStages; i++) {// First loop that runs till the fft noOfStages
            n = fftPoint;
            fftPoint += fftPoint;
            for (int j=0; j < n; j++) {// loop to calculate the corresponding twiddle factor values for each noOfStages
                cosPart = (float)Math.cos(alpha/fftPoint*j);
                sinPart = (float)Math.sin(alpha/fftPoint*j);

                for (int k=j; k < length; k=k+fftPoint) {//multiplication of values
                    multiplierReal = cosPart*real[k+n] - sinPart*imag[k+n];
                    multiplierImag= sinPart*real[k+n] + cosPart*imag[k+n];
                    // even part multiplication
                    real[k] = real[k] + multiplierReal;
                    imag[k] = imag[k] + multiplierImag;
                    // odd part multiplication
                    real[k+n] = real[k] - multiplierReal;
                    imag[k+n] = imag[k] - multiplierImag;
                }
            }
        }
    }

    private static void fftReverse(float[] real,int length, int noOfStages){
        float temp=0;
        int i;
        for(i =0;i<length/2;i++){
            int j= reverseBits(i,noOfStages);// reversing bit value of ith index
            temp = real[i];
            real[i] = real[j];
            real[j] = temp;
        }
    }
    private static int reverseBits(int number, int noOfStages)// function that returns the bit reversed value of given number
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
    }

    private static float[] makePowerOf2(float[] input) {
        int length =input.length;
        int N=length;
        // checking if length is a power of 2 or not
        int flag=0;// flag to check if the length of the array is a power of 2 or not, flag = 0 initially
        float stage=(float)(Math.log(length) / Math.log(2));
        if((stage-(int)stage)!=0){
            flag=1;//when the value of length is not a power of 2 set flag =1
        }
        if(flag==1){// if length is not a power of 2
            int newLength = 1;
            while(newLength < length)//calculating the value of new length to a closest value i.e the power of 2
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
    }

    private static void windowing(float[] input){
        float w[]=new float[input.length];
        for(int i=0; i<input.length; i++){
            if(i>=0&&i<800) {
                w[i]=1;
                //w[i] = (float)( 0.54 - 0.46*(Math.cos( 2*Math.PI*i/(input.length) ) ));
                //w[i] = (float)(0.5*(1 - (Math.cos( 2*Math.PI*i/(input.length-1) ) )));
            }
            else{
                w[i]=0;}
            input[i]*=w[i];
        }

    }

    private static float[] absoluteValue(float[] re, float[] im){
        float[] abs = new float[re.length];
        for(int i=0;i<re.length;i++){
            abs[i]=(float)(Math.sqrt(Math.pow(re[i],2)+Math.pow(im[i],2)));
        }return abs;
    }

    private static void normalize(float[] input){//normalizing the amplitude value
        for(int i=0;i<input.length;i++){
            input[i]=(float)(input[i]/32768.0);//since the maximum amplitude value is 32768
        }

    }
    public static float[] callMainFft(float[] input){
        normalize(input);
       float[] fftInputReal = makePowerOf2(input);// for zero padding if it is not a power of 2
        float[] fftInputImag = new float[fftInputReal.length];
        for(int i=0;i<fftInputReal.length;i++){
            fftInputImag[i]=0;
        }
                //float[] window = fft.getWindow();
        fft(fftInputReal, fftInputImag);
        float[] fftOutput= absoluteValue(fftInputReal,fftInputImag);
        windowing(fftOutput);
        return fftOutput;
    }
}