package com.awaj;

/**Radix-2 FftOutput implementation
 * Here the input to the fft is a real number and output is complex number
 */

public class FftOutput {

   public static void fft(float[] real, float[] imag)
    {
        int n1,n2;
        int length, stages;
        float alpha, cosPart,sinPart,t1,t2;

        length= real.length;//length of fft
        stages = (int)(Math.log(length) / Math.log(2));// number of fft stages
        alpha=(float)(-2*Math.PI);// for calculating twiddle factor

        ReverseBits(real);// function for reversing bit position in the given data
        // FFT
        n2 = 1;
        for (int i=0; i < stages; i++) {
            n1 = n2;
            n2 = n2 + n2;
            for (int j=0; j < n1; j++) {
                cosPart = (float)Math.cos(alpha/n2*j);
                sinPart = (float)Math.sin(alpha/n2*j);
                for (int k=j; k < length; k=k+n2) {
                    t1 = cosPart*real[k+n1] - sinPart*imag[k+n1];
                    t2 = sinPart*real[k+n1] + cosPart*imag[k+n1];
                    real[k+n1] = real[k] - t1;
                    imag[k+n1] = imag[k] - t2;
                    real[k] = real[k] + t1;
                    imag[k] = imag[k] + t2;
                }
            }
        }
    }

    private static void ReverseBits(float[] real)
    {
        int n1,n2, reversedBit=0, length =real.length;
        float temp;
        n2 = length/2;
        for (int i=1; i < length - 1; i++) {
            n1 = n2;
            while ( reversedBit >= n1 ) {
                reversedBit = reversedBit - n1;
                n1 = n1/2;
            }
            reversedBit = reversedBit + n1;
            if (i < reversedBit) {
                temp = real[i];
                real[i] = real[reversedBit];
                real[reversedBit] = temp;
            }
        }
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

    private static void normalize(float[] input){
        for(int i=0;i<input.length;i++){
            input[i]=(float)(input[i]/32768.0);
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