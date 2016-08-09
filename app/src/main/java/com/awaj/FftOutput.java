package com.awaj;

/**Radix-2 FftOutput implementation
 * Here the input to the fft is a real number and output is complex number
 */

public class FftOutput {

    static int length, stages;
    static float[] cos;
    static float[] sin;
    public FftOutput(int length) {
        this.length= length;
        this.stages = (int)(Math.log(length) / Math.log(2));
        // precompute tables
        cos = new float[length/2];
        sin = new float[length/2];
        double alpha=-2*Math.PI/length;
        for(int i=0; i<length/2; i++) {
            cos[i] = (float)Math.cos(alpha*i);
            sin[i] = (float)Math.sin(alpha*i);
        }
    }
    public static void fft(float[] real, float[] imag)
    {
        int n1,n2,a;
        float cosPart,sinPart,t1,t2;
        // Bit-reverse
       ReverseBits(real,imag);
        // FFT
        n2 = 1;
        for (int i=0; i < stages; i++) {
            n1 = n2;
            n2 = n2 + n2;
            a = 0;

            for (int j=0; j < n1; j++) {
                cosPart = cos[a];
                sinPart = sin[a];
                a +=  1 << (stages-i-1);

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

    private static void ReverseBits(float[] real, float[] imag)
    {
        int n1,n2, reversedBit=0;
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
        for(int i=0 ; i<N;i++){
            if (N%2==0){N=N/2;}
            else{flag=1;}// when the value of length is not a power of 2 set flag =1
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
            w[i] =0;}
        for(int i=0; i<input.length; i++){
            if(i>=0&&i<800) {
                w[i]=1;
                //w[i] = (float)( 0.54 - 0.46*(Math.cos( 2*Math.PI*i/(input.length) ) ));
                //w[i] = (float)(0.5*(1 - (Math.cos( 2*Math.PI*i/(input.length-1) ) )));
            }
            else{
                w[i]=0;}
        }

        for(int i=0;i<input.length;i++){
            input[i]*=w[i];
        }
    }

    private float[] absoluteValue(float[] re, float[] im){
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
        FftOutput fft = new FftOutput(fftInputReal.length);
        //float[] window = fft.getWindow();
        fft.fft(fftInputReal, fftInputImag);
        float[] fftOutput= fft.absoluteValue(fftInputReal,fftInputImag);
        windowing(fftOutput);
        return fftOutput;
    }
}