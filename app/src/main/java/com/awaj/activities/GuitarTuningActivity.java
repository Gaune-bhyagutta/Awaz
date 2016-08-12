package com.awaj.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.awaj.AudioRecordClass;
import com.awaj.AudioRecordInterface;
import com.awaj.R;
import com.awaj.StateClass;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class GuitarTuningActivity extends AppCompatActivity {
    /**Variables*/
    static String decibelStr;
    static String noteStr;
    static String currentFrequencyStr = new String();
    AudioRecordClass audioRecordClass;

    /**State class*/
    StateClass stateClass = StateClass.getState();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Avoiding the XML Layout*/
        //setContentView(R.layout.topssix_guitar_tuner);
        /**Drawing Custom View*/
        setContentView(new guitarTunerMeterView(this, new Canvas()));

        /**Monitoring State Started*/
        stateClass.setRecoderingState(true);

        Log.d("VIVZ","onCreate");

        /**Audio Streaming From Here*/
//        audioRecordClass = new AudioRecordClass(new AudioRecordInterface() {
//            @Override
//            public void processExecuting(float decibel, float frequency, String notes) {
//        Log.d("VIVZ","Inside Process Executing");
//                /**Updating the values along with typecasting*/
//                currentFrequencyStr = String.valueOf(frequency);
//                decibelStr = String.valueOf(decibel);
//                noteStr = notes;
//                //Log.d("VIVZ","Freq: "+currentFrequencyStr+" Notes: "+notes);
//                System.out.println("Freq"+frequency);
//            }
//        });
//        audioRecordClass.execute();

    }


    /**
     * Subclass for drawing the meter
     */
    public static class guitarTunerMeterView extends View {
        Paint circle, bigCircle, line, paintText;
        float xc = 0;
        float yc = 0;
        int radius;
        float x2 = xc - radius, y2 = yc;

        int textSize = 80;
        int textXPos;
        int textYPos;

        String lowerNotes = "A A#";
        String mainNote = "B";
        String higherNotes = "C C#";

        String currentNearestNote = "B";

        public guitarTunerMeterView(Context context, Canvas canvas) {
            super(context);
            circle = new Paint();    //Paint Object
            bigCircle = new Paint();
            line = new Paint();
            paintText = new Paint();
        }


        @Override
        protected void onDraw(Canvas canvas) {//
            //super.onDraw(canvas);
            setNotes(3);

            /**Setting up Variables*/
            canvas.drawColor(getResources().getColor(R.color.amber_primary_dark));

            textXPos = canvas.getWidth() / 4;
            textYPos = canvas.getHeight() / 10;
            radius = canvas.getWidth() / 2;

            xc = canvas.getWidth() / 2;
            yc = canvas.getHeight() - canvas.getHeight() / 10;

            circle.setColor(getResources().getColor(R.color.amber_primary_dark));
            circle.setStyle(Paint.Style.FILL);

            bigCircle.setColor(Color.LTGRAY);
            bigCircle.setStyle(Paint.Style.FILL);

            line.setColor(getResources().getColor(R.color.amber_accent));
            line.setStrokeWidth(5);

            paintText.setColor(getResources().getColor(R.color.amber_secondary_text));
            paintText.setTextSize(textSize);

            /**Invocation of Various Sub Draw Methods*/
            /**Draws Text of Main Note & Two Nearest lower & higher Notes*/
            drawPrimaryNotes(canvas);
            /**Draw Dynamic Frequency Meter*/
            drawFrequencyMeter(canvas);
            /**Draw Nearest Notes & Current frequency*/
//            drawNotesAndFreq(canvas);

            /**Draw Current Nearest Note*/
            paintText.setColor(getResources().getColor(R.color.amber_accent));
            canvas.drawText(currentNearestNote, xc - xc / 10, yc - yc / 15, paintText);

            /**Draw Current Frequency Here*/
            paintText.setColor(getResources().getColor(R.color.amber_primary_text));
            paintText.setTextSize(70);
            canvas.drawText(currentFrequencyStr, xc - xc / 10, yc + yc / 15, paintText);

//        float x2=(float)(radius*Math.cos(theta)),y2=(float)(radius*Math.sin(theta));

            if (x2 < canvas.getWidth()) {
                x2 += 1;
            }
            y2 = yc - (float) (Math.sqrt(Math.pow(radius, 2) - Math.pow((x2 - xc), 2)));
            invalidate();

        }//End onDraw

        public void drawPrimaryNotes(Canvas canvas) {
            /**Draws Text of Main Note & Two Nearest lower & higher Notes*/
            canvas.drawText(lowerNotes, textXPos, textYPos, paintText);
            paintText.setTextSize(textSize + 30);
            paintText.setColor(getResources().getColor(R.color.amber_accent));
            canvas.drawText(mainNote, textXPos + textSize * 2 + 15, textYPos, paintText);
            paintText.setTextSize(textSize);
            paintText.setColor(getResources().getColor(R.color.amber_secondary_text));
            canvas.drawText(higherNotes, textXPos + textSize * 3 + 10, textYPos, paintText);
        }

        public void drawFrequencyMeter(Canvas canvas) {
            /**Larger Outer Base Circle*/
            canvas.drawCircle(xc, yc, radius, bigCircle);
            /**Dynamic Needle*/
            canvas.drawLine(xc, yc, x2, y2, line);
            /**Inner Circle*/
            canvas.drawCircle(xc, yc, radius / 2, circle);
            /**Rectangle to overlap the bottom half of cirle*/
            canvas.drawRect(0, yc, canvas.getWidth(), canvas.getHeight(), circle);
        }

        public void drawNotesAndFreq(Canvas canvas) {
//            /**Draw Current Nearest Note*/
//            paintText.setColor(getResources().getColor(R.color.amber_accent));
//            canvas.drawText(currentNearestNote, xc - xc / 10, yc - yc / 15, paintText);
//
//            /**Draw Current Frequency Here*/
//            paintText.setColor(getResources().getColor(R.color.amber_primary_text));
//            paintText.setTextSize(70);
//            canvas.drawText(currentFrequencyStr, xc - xc / 10, yc + yc / 15, paintText);
        }

        public void setNotes(int index) {
            String[] Notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "E#", "F", "F#", "G", "G#"};
            int first, second, third, fourth, fifth;

            /**Default Position of indexes*/
            first = index - 2;
            second = index - 1;
            third = index;
            fourth = index + 1;
            fifth = index + 2;
            /**Tackling first two & Last two index positions*/
            if (index == 0) {
                first = 10;
                second = 11;
            } else if (index == 1) {
                first = 11;
                second = 0;
            } else if (index == 10) {
                fourth = 11;
                fifth = 0;
            } else if (index == 11) {
                fourth = 0;
                fifth = 1;
            }
            lowerNotes = Notes[first] + Notes[second];
            mainNote = Notes[third];
            higherNotes = Notes[fourth] + Notes[fifth];
        }
    }//End View

    @Override
    protected void onPause() {
        super.onPause();
        stateClass.setRecoderingState(false);
    }
}