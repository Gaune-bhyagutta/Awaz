package com.awaj.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.awaj.AudioRecordFrequencyNote;
import com.awaj.AudioRecordFrequencyNoteListener;
import com.awaj.AudioRecordInterface;
import com.awaj.R;
import com.awaj.StateClass;

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

    AudioRecordFrequencyNote audioRecordFrequencyNote;

    TextView musicNotesTV, currentNoteTV, currentFrequencyTV;


    String lowerNotes = "A A#";
    String mainNote = "B";
    String higherNotes = "C C#";
    String currentNearestNote = "B";

    /**
     * State class
     */
    StateClass stateClass = StateClass.getState();

    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final int CHANNELS_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final int NO_OF_SAMPLES = 22050;
    //private static final float RESOLUTION =SAMPLE_RATE_IN_HZ / NO_OF_SAMPLES;
    // private static int MIN_BUFFER_SIZE_BYTES = NO_OF_SAMPLES*2;

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

        /**Referencing UI Elements*/
        musicNotesTV = (TextView) findViewById(R.id.musicNotesTV);
        currentNoteTV = (TextView) findViewById(R.id.currentNoteTV);
        currentFrequencyTV = (TextView) findViewById(R.id.currentFrequencyTV);


        /**Audio Streaming From Here*/
        audioRecordFrequencyNote = new AudioRecordFrequencyNote(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNELS_CONFIGURATION, AUDIO_ENCODING,
                NO_OF_SAMPLES, new AudioRecordFrequencyNoteListener() {

            @Override
            public void processExecuted() {
                Toast.makeText(getApplicationContext(), "Finished Recording", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void processExecuting(String frequency, String notes) {
                /**Updating the values along with typecasting*/
                currentFrequencyStr = frequency;
                if (currentFrequencyStr.length() >= 6) {
                    currentFrequencyStr = currentFrequencyStr.substring(0, 5) + "Hz";
                } else if (currentFrequencyStr.length() == 5) {
                    currentFrequencyStr = currentFrequencyStr.substring(0, 4) + "Hz";
                } else if (currentFrequencyStr.length() == 4) {
                    currentFrequencyStr = currentFrequencyStr.substring(0, 3) + "Hz";
                }
                noteStr = notes;

                /**LOG*/
                //Log.d("VIVZ","Freq: "+currentFrequencyStr+" Notes: "+notes);

                /**Update Values in TextViews*/
                musicNotesTV.setText(getFivePrimaryNotes(1));
                currentNoteTV.setText(noteStr);
                currentFrequencyTV.setText(currentFrequencyStr);
            }
        });
        audioRecordFrequencyNote.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stateClass.setRecoderingState(false);
    }

    /**
     * Adding Functionality to the Back Arrow button to go back to previous Activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns the String to be displayed in Primary Notes TextView Based on Current Index Position
     */
    public String getFivePrimaryNotes(int index) {
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
        return lowerNotes + mainNote + higherNotes;
    }
}