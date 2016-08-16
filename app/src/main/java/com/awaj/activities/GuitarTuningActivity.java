package com.awaj.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.awaj.AudioRecordClass;
import com.awaj.AudioRecordInterface;
import com.awaj.R;
import com.awaj.StateClass;
import com.awaj.GuitarTuner;
import com.awaj.activities.GuitarTuner.GuitarTunerMeterView;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class GuitarTuningActivity extends AppCompatActivity {
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
        /**Setting up XML*/
        setContentView(R.layout.topssix_guitar_tuner);

        /**Back Button within Toolbar*/
        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                //Log.d("VIVZ","Freq: "+currentFrequencyStr+" Notes: "+notes);
                System.out.println("Freq" + frequency);
            }
        });
        audioRecordClass.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stateClass.setRecoderingState(false);
    }
    /**Adding Functionality to the Back Arrow button to go back to previous Activity*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}