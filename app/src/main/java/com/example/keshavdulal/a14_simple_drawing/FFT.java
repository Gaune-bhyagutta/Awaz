package com.example.keshavdulal.a14_simple_drawing;

//
/**
 * Radix-2 FFT implementation
 Here the input to the fft is a real number and output is complex number
 */

public class FFT {

    private final float[] ax;

    public FFT(float[] ax) {
        this.ax = ax;
        //    Complex[] x = makepowerof2(ax);
//        print(x);
//        Complex[] y = fft(x);
//        print(y);
    }

    public static Complex[] makepowerof2(float[] ax) {
        int N=ax.length;
        int M=N;
        // checking if N is a power of 2 or not
        int s=0;
        for(int i=0 ; i<M;i++){
            if (M%2==0){M=M/2;}

            else if (M%2!=0){s=1;}
        }

        if(s==1){// if N is not a power of 2
            int N1 = 1;
            while(N1 < N)
            {N1*=2;}

            Complex[] x = new Complex[N1];

            for (int i = 0; i < N; i++) {
                x[i] =new Complex(ax[i],0);
            }

            for (int i =N ; i < N1; i++) {// zero padding till the number is power of 2
                x[i] =new Complex(0,0);
            }
            return x;
        }

        else{// when N is already a power of 2
            Complex[] x = new Complex[N];

            for (int i = 0; i < N; i++) {
                x[i] =new Complex(ax[i],0);
            }
            return x;
        }
    }

    public static Complex[] fft(Complex[] x) {
        int N = x.length;
        if (N == 1) return new Complex[] { x[0] };
        // Splitting the odd and even terms for calculation of FFT
        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public static double[] absolute_value(Complex[] x){
        double[] a = new double[x.length];
        for(int i=0; i<x.length;i++){
            a[i]= x[i].abs();
        }
        return a;
    }

    public static void print(Complex[] x) {

        for (int i = 0; i < x.length; i++) {
            String a= x[i].toString();
            System.out.print(a+" , ");
        }
        System.out.println();
    }
}