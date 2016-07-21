package com.example.keshavdulal.a14_simple_drawing;
import android.os.CountDownTimer;

/***Created by keshavdulal on 21/07/16*/
public class Timer extends CountDownTimer {
    public long MS = 00L;
    public long SS = 00L;
    public long MM = 00L;
    public long HH = 00L;

    /**Instantiate a Timer object with total duration & countdown interval */
    /**CONSTRUCTOR*/
    public Timer(long millisInFuture, long countDownInterval){
        super(millisInFuture,countDownInterval);
    }

    @Override
    public void onTick(long l) {
//        MS++;
//        if (MS ==100) {MS=0;}
        SS++;
//        if(MS==25)    {SS++;MS=0;}
        if(SS == 60)    {MM++;SS=0;}
        if(MM == 60)    {HH++;MM=0;}

        StringBuilder displayTime = new StringBuilder();
        displayTime.append(String.format("%02d",HH));
        displayTime.append(":");
        displayTime.append(String.format("%02d",MM));
        displayTime.append(":");
        displayTime.append(String.format("%02d",SS));
//        displayTime.append(":");
//        displayTime.append(String.format("%02d",MS));

        MainActivity.timerTV.setText(displayTime);
    }

    @Override
    public void onFinish() {
    }
}
