package com.awaj.activities;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.awaj.AudioPlayClassMain;
import com.awaj.AudioRecordFile;
import com.awaj.AudioRecordFileListener;
import com.awaj.GraphFragment;
import com.awaj.MainActivity;
import com.awaj.R;
import com.awaj.StateClass;
import com.awaj.Timer;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class RecordingActivity extends AppCompatActivity {

    private TextView RecordingStateTV;
    private Button RecordingButton;
    private boolean isRecording = false;
    Toolbar toolbarObj;

    /**
     * TIMER
     */
    final Timer timerStartObj = new Timer(3000000, 1000, RecordingActivity.this);
    static TextView timerTV;

    /**
     * Graph Fragment
     */
    private static GraphFragment graphFragment = new GraphFragment();

    /**Object For Recording*/
    AudioRecordFile myAudioRecordFile;
    StateClass myStateClass = StateClass.getState();
    /**Object for playback*/
    AudioPlayClassMain myAudioPlayClassMain;

    //START---Audio Record and Play Parameters-----
    // THE DEFINETIONS ARE DEFINED IN THE RESPECTIVE FUNCTION
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final int CHANNELS_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final int NO_OF_SAMPLES = 4096;
    public static final float RESOLUTION = SAMPLE_RATE_IN_HZ / NO_OF_SAMPLES;

    private static int MIN_BUFFER_SIZE_BYTES = NO_OF_SAMPLES * 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topsix_recorder);

        /**Back Button within Toolbar*/
        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecordingStateTV = (TextView) findViewById(R.id.recStateTV);
        RecordingButton = (Button) findViewById(R.id.recButton);

        /**Default Configs in UI*/
        RecordingStateTV.setBackgroundResource(R.drawable.greengradient);
        RecordingStateTV.setText("Ready to Record");
        RecordingButton.setText("RECORD");
        RecordingButton.setBackgroundResource(R.drawable.redgradient);

        /**Timer UI Setups*/
        timerTV = (TextView) findViewById(R.id.timerTV);
        timerTV.setText("00:00:00");

        /**Start-GRAPH FRAGMENT*/
//        FragmentManager myFragmentManager = getSupportFragmentManager();
//        FragmentTransaction myFragmentTransaction = myFragmentManager.beginTransaction();
//        myFragmentTransaction.add(R.id.graphFragmentLL, graphFragment, " ");
//        myFragmentTransaction.commit();

    }

    public void recButtonClicked(View view) {
        /**1.Start & Stop Recording here*/
        /**2.Configure Timer*/
        /**3.Configure Graph*/
        /**Is Recording*/
        if (myStateClass.getRecoderingState() == false) {
            myStateClass.setRecoderingState(true);
//            MainActivity.record();
            /**Timer*/
            timerTV.setText("00:00:00");
            timerStartObj.start();
            /**UI Changes*/
            RecordingStateTV.setBackgroundResource(R.drawable.redgradient);
            RecordingStateTV.setText("Recording...");
            RecordingButton.setText("STOP");
            RecordingButton.setBackgroundResource(R.drawable.greengradient);

            /**Start Recording*/
            myAudioRecordFile = new AudioRecordFile(AUDIO_SOURCE,
                    SAMPLE_RATE_IN_HZ,
                    CHANNELS_CONFIGURATION,
                    AUDIO_ENCODING,
                    NO_OF_SAMPLES,new AudioRecordFileListener() {
                @Override
                public void processExecuting() {
                    /**todo: Add UI changes here*/
                }

                @Override
                public void processExecuted() {
                    /**todo:after recording completion*/
                }
            });
            myAudioRecordFile.execute();
        }
        /**Is not Recording*/
        else {
            myStateClass.setRecoderingState(false);
            /**Timer*/
            timerStartObj.cancel();
            timerStartObj.SS = 0L;
            timerStartObj.MM = 0L;
            timerStartObj.HH = 0L;
            timerStartObj.MS = 0L;
            /**UI Changes*/
            RecordingStateTV.setBackgroundResource(R.drawable.greengradient);
            RecordingStateTV.setText("Ready to Record");
            RecordingButton.setText("REC");
            RecordingButton.setBackgroundResource(R.drawable.redgradient);
            /***/

        }
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

    @Override
    protected void onPause() {
        super.onPause();
        myStateClass.setRecoderingState(false);
    }
}
