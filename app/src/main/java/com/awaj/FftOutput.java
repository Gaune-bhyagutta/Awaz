package com.awaj;

/**Radix-2 FftOutput implementation
 * Here the input to the fft is a real number and output is complex number
 */

public class FftOutput {

    int n, m;
    // Lookup tables.  Only need to recompute when size of FFT changes.
    float[] cos;
    float[] sin;
    public FftOutput(int n) {
        this.n = n;
        this.m = (int)(Math.log(n) / Math.log(2));
        // precompute tables
        cos = new float[n/2];
        sin = new float[n/2];
        for(int i=0; i<n/2; i++) {
            cos[i] = (float)Math.cos(-2*Math.PI*i/n);
            sin[i] = (float)Math.sin(-2*Math.PI*i/n);
        }
    }
    public void fft(float[] x, float[] y)
    {
        int i,j,k,n1,n2,a;
        float c,s,e,t1,t2;

        // Bit-reverse
        j = 0;
        n2 = n/2;
        for (i=1; i < n - 1; i++) {
            n1 = n2;
            while ( j >= n1 ) {
                j = j - n1;
                n1 = n1/2;
            }
            j = j + n1;
            if (i < j) {
                t1 = x[i];
                x[i] = x[j];
                x[j] = t1;
                t1 = y[i];
                y[i] = y[j];
                y[j] = t1;
            }
        }
        // FFT
        n1 = 0;
        n2 = 1;
        for (i=0; i < m; i++) {
            n1 = n2;
            n2 = n2 + n2;
            a = 0;

            for (j=0; j < n1; j++) {
                c = cos[a];
                s = sin[a];
                a +=  1 << (m-i-1);

                for (k=j; k < n; k=k+n2) {
                    t1 = c*x[k+n1] - s*y[k+n1];
                    t2 = s*x[k+n1] + c*y[k+n1];
                    x[k+n1] = x[k] - t1;
                    y[k+n1] = y[k] - t2;
                    x[k] = x[k] + t1;
                    y[k] = y[k] + t2;
                }
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
            if(i>=5&&i<800) {
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