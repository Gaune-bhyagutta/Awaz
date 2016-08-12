package com.awaj.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.awaj.R;
import com.awaj.AudioRecordClass;
import com.awaj.AudioRecordInterface;
import com.awaj.StateClass;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class ScaleTestingActivity extends AppCompatActivity {
    /**
     * Variables
     */
    Toolbar toolbarObj;
    static String decibelStr;
    static String noteStr;
    static String currentFrequencyStr = new String();
    AudioRecordClass audioRecordClass;

    /**
     * State class
     */
    StateClass stateClass = StateClass.getState();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**Avoid XML & Draw Custom View*/
        setContentView(new vocalTestingView(this));

        /**Back Button within Toolbar*/
//        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbarObj);
//        /**TODO:fix the back button*/
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Monitoring State Started*/
        stateClass.setRecoderingState(true);

        /**Audio Streaming From Here*/
        audioRecordClass = new AudioRecordClass(new AudioRecordInterface() {
            @Override
            public void processExecuting(float decibel, float frequency, String notes) {
                /**Updating the values along with typecasting*/
                currentFrequencyStr = String.valueOf(frequency);
                decibelStr = String.valueOf(decibel);
                noteStr = notes;
                System.out.println("Freq" + frequency);
            }
        });
        audioRecordClass.execute();
    }

    public static class vocalTestingView extends View {

        /**
         * Variables
         */
        Paint circle, bigCircle, line, paintText;
        float xc, yc, x2, y2, theta;
        float radius;

        boolean drawnOnce = false;

        int textSize;
        int textXPos;
        int textYPos;

        float noteFrequency = 184;
        float frequency = 189;
        float difference = noteFrequency - frequency;

        String lowerNotes = "A A#";
        String mainNote = "B";
        String higherNotes = "C C#";

        String currentNearestNote = "B";

        /**
         * Default Constructor
         */
        public vocalTestingView(Context context) {
            super(context);
            circle = new Paint();    //Paint Object
            bigCircle = new Paint();
            line = new Paint();
            paintText = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            /**1. Setup Canvas*/
            canvas.drawColor(getResources().getColor(R.color.blue_primary_dark));
            /**2. Display one Primary note & 4 secondary notes*/

            /**3. Display Adjustible Meter*/
            /**4. Display Current Frequency*/

            displayPrimaryNotes(canvas);
            displayAdjustableMeter(canvas);
            displayCurrentFrequency(canvas);
            setNotes(1);
        }

        public void displayPrimaryNotes(Canvas canvas) {
            textSize = 80;
            textXPos = canvas.getWidth() / 4;
            textYPos = canvas.getHeight()-canvas.getHeight() / 10;
            radius = canvas.getWidth() / 2;

            /**First Two Notes*/
            paintText.setColor(getResources().getColor(R.color.blue_secondary_text));
            paintText.setTextSize(textSize);
            canvas.drawText(lowerNotes, textXPos, textYPos, paintText);
            /**Main Note*/
            paintText.setTextSize(textSize + 30);
            paintText.setColor(getResources().getColor(R.color.blue_accent));
            canvas.drawText(mainNote, textXPos + textSize * 2 + 15, textYPos, paintText);
            /**Last Two Notes*/
            paintText.setTextSize(textSize);
            paintText.setColor(getResources().getColor(R.color.blue_secondary_text));
            canvas.drawText(higherNotes, textXPos + textSize * 4 + 10, textYPos, paintText);

            /**Draw a bounding rectangle*/
//            String primaryNotes = lowerNotes+mainNote+higherNotes;
//            float width = paintText.measureText(primaryNotes);
//            canvas.drawRect(textXPos,textYPos-80,textXPos+width,textYPos,paintText);
        }

        public void displayAdjustableMeter(Canvas canvas) {
        }

        public void displayCurrentFrequency(Canvas canvas) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        stateClass.setRecoderingState(false);
    }
}
