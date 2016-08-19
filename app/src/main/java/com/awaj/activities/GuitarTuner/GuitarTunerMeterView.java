package com.awaj.activities.GuitarTuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.awaj.R;

/**
 * Subclass for drawing the meter
 */
public class GuitarTunerMeterView extends View {
    Paint innerCircle, outerBaseCircle, needle, paintText;
    float x2, y2, theta;
    float radius;

    boolean drawnOnce = false;
    static float currentFrequency=186, currentNoteFrequency=184, noteMinFrequency= 179 ,noteMaxFrequency=189;
    static float difference = 0;
    static float unit = noteMaxFrequency-noteMinFrequency;

    /**
     * Three Constructors
     */
    public GuitarTunerMeterView(Context context) {
        this(context, null);
    }

    public GuitarTunerMeterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuitarTunerMeterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        innerCircle = new Paint();    //Paint Object
        outerBaseCircle = new Paint();
        needle = new Paint();
        paintText = new Paint();
    }

    /**
     * Draws custom view
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        /**Setting up Variables*/
        canvas.drawColor(getResources().getColor(R.color.colorPrimaryDark));
        float cgh = canvas.getHeight();
        float cgw = canvas.getWidth();
        float cgh2 = cgh / 2;
        float cgw2 = cgw / 2;

        /**Setup Paint objects*/
        innerCircle.setColor(getResources().getColor(R.color.colorPrimary));
        innerCircle.setStyle(Paint.Style.FILL);

        outerBaseCircle.setColor(Color.LTGRAY);
        outerBaseCircle.setStyle(Paint.Style.FILL);

        needle.setColor(getResources().getColor(R.color.amber_accent));
        needle.setStrokeWidth(5);

        /**Draw a base of Outer Circle*/
        canvas.drawCircle(cgw2, cgh, cgw2, outerBaseCircle);
        /**Draw The Rotating Needle*/
        if (!drawnOnce) {
            drawnOnce = true;
            theta = (float) (Math.PI / 2);
        }
        x2 = cgw2 + (float) (cgw2 * Math.cos(theta));
        y2 = cgh - (float) (cgw2 * Math.sin(theta));
        canvas.drawLine(cgw2, cgh, x2, y2, needle);
        /**Draw Inner Base Circle to overlap bottom half of needle*/
        canvas.drawCircle(cgw2, cgh, cgw2 / 2, innerCircle);

        /**Dynamic Needle*/
        difference =currentNoteFrequency - currentFrequency;
//        if(difference<0){
//            if(theta>((Math.PI/2)+(Math.PI/unit*difference))) {
//                theta -= (float) (Math.PI / 180);
//            }
//        }
//        else if(difference>0){
//            if(theta<((Math.PI/2)+(Math.PI/unit*difference))) {
//                theta += (float) (Math.PI / 180);
//            }
//        }

        if(difference<0){
            theta = (float) ((Math.PI/2) + (Math.PI/unit*difference));
        }
        else if(difference>0){
            theta = (float) ((Math.PI/2)+(Math.PI/unit*difference));
        }

        invalidate();

    }/**End onDraw*/

//    public void drawPrimaryNotes(Canvas canvas) {
//        textSize = 80;
//        textXPos = canvas.getWidth() / 4;
//        textYPos = canvas.getHeight() / 10;
//        radius = canvas.getWidth() / 2;
//
//        /**First Two Notes*/
//        paintText.setColor(getResources().getColor(R.color.amber_secondary_text));
//        paintText.setTextSize(textSize);
//        canvas.drawText(lowerNotes, textXPos, textYPos, paintText);
//        /**Main Note*/
//        paintText.setTextSize(textSize + 30);
//        paintText.setColor(getResources().getColor(R.color.amber_accent));
//        canvas.drawText(mainNote, textXPos + textSize * 2 + 15, textYPos, paintText);
//        /**Last Two Notes*/
//        paintText.setTextSize(textSize);
//        paintText.setColor(getResources().getColor(R.color.amber_secondary_text));
//        canvas.drawText(higherNotes, textXPos + textSize * 3 + 10, textYPos, paintText);
//    }

//    public void drawFrequencyMeter(Canvas canvas) {
//        innerCircle.setColor(getResources().getColor(R.color.amber_primary_dark));
//        innerCircle.setStyle(Paint.Style.FILL);
//
//        outerBaseCircle.setColor(Color.LTGRAY);
//        outerBaseCircle.setStyle(Paint.Style.FILL);
//
//        needle.setColor(getResources().getColor(R.color.amber_accent));
//        needle.setStrokeWidth(5);
//        /**Larger Outer Base Circle*/
//        canvas.drawCircle(xc, yc, radius, outerBaseCircle);
//        /**Dynamic Needle*/
//        canvas.drawLine(xc, yc, x2, y2, needle);
//        /**Inner Circle*/
//        canvas.drawCircle(xc, yc, radius / 2, innerCircle);
//        /**Rectangle to overlap the bottom half of cirle*/
//        canvas.drawRect(0, yc, canvas.getWidth(), canvas.getHeight(), innerCircle);
//    }
}/**
 * End View
 */
