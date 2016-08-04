package com.awaj;

import android.os.CountDownTimer;
import android.view.View;

/***
 * Created by keshavdulal on 21/07/16
 */
public class Timer extends CountDownTimer {
    public long MS = 00L;
    public long SS = 00L;
    public long MM = 00L;
    public long HH = 00L;
    public boolean state = false;

    /**Instantiate a Timer object with total duration & countdown interval */
    /**
     * CONSTRUCTOR
     */
    public Timer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
        SS++;
        if (SS == 60) {
            MM++;
            SS = 0;
        }
        if (MM == 60) {
            HH++;
            MM = 0;
        }

        StringBuilder displayTime = new StringBuilder();
        displayTime.append(String.format("%02d", HH));
        displayTime.append(":");
        displayTime.append(String.format("%02d", MM));
        displayTime.append(":");
        displayTime.append(String.format("%02d", SS));

//        displayTime.append(":");
//        displayTime.append(String.format("%02d",MS));

        MainActivity.timerTV.setText(displayTime);
        if (MainActivity.playState() != 1) {
            if (state == true) {
                MainActivity.recLogo.setVisibility(View.VISIBLE);
                state = false;
            } else {
                MainActivity.recLogo.setVisibility(View.INVISIBLE);
                state = true;
            }
        }
    }

    @Override
    public void onFinish() {
        MS = 00L;
        SS = 00L;
        MM = 00L;
        HH = 00L;
    }
}
