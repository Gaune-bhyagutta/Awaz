package com.example.keshavdulal.a14_simple_drawing;

//

/**
 * Radix-2 FftOutput implementation
 Here the input to the fft is a real number and output is complex number
 */

public class FftOutput {

    public static Complex[] makePowerOf2(float[] input) {
        int length =input.length;
        int N=length;
        // checking if length is a power of 2 or not
        int flag=0;// flag to check if the length of the array is a power of 2 or not
                    // flag = 0 initially

        for(int i=0 ; i<N;i++){
            if (N%2==0){N=N/2;}

            else if (N%2!=0){flag=1;}// when the value of length is not a power of 2 set flag =1
        }

        if(flag==1){// if length is not a power of 2
            int newLength = 1;
            while(newLength < length)//calculating the value of new length to a closest value i.e the power of 2
            {newLength*=2;}

            Complex[] x = new Complex[newLength];// making an array of newLength

            for (int i = 0; i < length; i++) {
                x[i] =new Complex(input[i],0);// till the value of previous length the input is converted to complex number
            }

            for (int i =length ; i < newLength; i++) {// zero padding till the number is power of 2 (from previous length to new length
                x[i] =new Complex(0,0);
            }
            return x;
        }

        else{// when length is already a power of 2 where flag =0
            Complex[] x = new Complex[length];

            for (int i = 0; i < length; i++) {
                x[i] =new Complex(input[i],0);
            }
            return x;
        }
    }

    public static Complex[] fft(Complex[] complexInput) {
        int length = complexInput.length;
        if (length == 1) return new Complex[] { complexInput[0] };
        // Splitting the odd and even terms for calculation of Fft
        // fft of even terms
        Complex[] even = new Complex[length/2];
        for (int i = 0; i < length/2; i++) {
            even[i] = complexInput[2*i];
        }
        Complex[] fftEven = fft(even);// recursively calling the method fft to calculate the fft value for the even terms

        // fft of odd terms
        Complex[] odd  = even;
        for (int i = 0; i < length/2; i++) {
            odd[i] = complexInput[2*i + 1];
        }
        Complex[] fftOdd = fft(odd);//recursively calling method fft to calculate the fft value for odd terms

        // combine
        Complex[] y = new Complex[length];
        for (int i = 0; i < length/2; i++) {
            double kn = -2 * i * Math.PI / length;
            Complex twiddleFactor = new Complex(Math.cos(kn), Math.sin(kn));// calculating twiddle factor Wn^k= e^(j*2*pi*k/N) = cos(2*pi*k/N)-i*sin(2*pi*k/N)
            y[i] = fftEven[i].plus(twiddleFactor.times(fftOdd[i]));
            y[i + length/2] = fftEven[i].minus(twiddleFactor.times(fftOdd[i]));
        }
        return y;
    }

    public static double[] absoluteValue(Complex[] complexInput){//Calculating the absolute value for the complex number array
        int l = complexInput.length/2;
        double[] absoluteValue = new double[l];
        for(int i=0; i<l;i++){
            absoluteValue[i]= complexInput[i].abs();//calling abs method of Complex class
        }
        return absoluteValue;
    }

    public static void print(Complex[] complexInput) {// for printing the complex number

        for (int i = 0; i < complexInput.length; i++) {
            String toString= complexInput[i].toString();//calling toString method of Complex class to print the complex number
            System.out.print(toString+" , ");
        }
        System.out.println();
    }

    public static double[] callMainFft(float[] input){
        Complex[] fftInput = makePowerOf2(input);// for zero padding if it is not a power of 2
        Complex[] fftOutput = fft(fftInput);
       // System.out.print("FftOutput output: ");
        //print(y);
        double[] absValue = absoluteValue(fftOutput);
        //System.out.println("absolute value: "+ Arrays.toString(z));
        return absValue;
    }
}