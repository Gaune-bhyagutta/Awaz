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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awaj.AudioPlayClassMain;
import com.awaj.AudioRecordFileDecibelFrequencyNoteGraph;
import com.awaj.AudioRecordFileDecibelFrequencyNoteGraphListener;
import com.awaj.GraphFragment;
import com.awaj.R;
import com.awaj.StateClass;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class MonitoringActivity extends AppCompatActivity {
    private static AudioRecordFileDecibelFrequencyNoteGraph recObj;
    StateClass myStateClass = StateClass.getState();
    Toolbar toolbarObj;

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

    LinearLayout timeDomainLL, freqDomainLL;
    private static GraphFragment timeDomainGraphFragment = new GraphFragment();
    private static GraphFragment freqDomainGraphFragment = new GraphFragment();

    TextView decibelTV,frequencyTV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topsix_monitor);

        /**Back Button within Toolbar*/
        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeDomainLL = (LinearLayout) findViewById(R.id.timeFragmentLL);
        freqDomainLL = (LinearLayout) findViewById(R.id.freqFragmentLL);

        decibelTV = (TextView) findViewById(R.id.decibelTV);
        frequencyTV = (TextView) findViewById(R.id.frequencyTV);

        /**Start-Time DomainGRAPH FRAGMENT*/
        FragmentManager timeDomainFragmentMgr = getSupportFragmentManager();
        FragmentTransaction timeDomainFragmentTransaction = timeDomainFragmentMgr.beginTransaction();
        timeDomainFragmentTransaction.add(R.id.timeFragmentLL, timeDomainGraphFragment, " ");
        timeDomainFragmentTransaction.commit();
        timeDomainGraphFragment.setMinBufferSizeInBytes(MIN_BUFFER_SIZE_BYTES);

        /**Start-GRAPH FRAGMENT*/
        FragmentManager freqDomainFragmentMgr = getSupportFragmentManager();
        FragmentTransaction freqDomainFragmentTransaction = freqDomainFragmentMgr.beginTransaction();
        freqDomainFragmentTransaction.add(R.id.freqFragmentLL, freqDomainGraphFragment, " ");
        freqDomainFragmentTransaction.commit();
        freqDomainGraphFragment.setMinBufferSizeInBytes(MIN_BUFFER_SIZE_BYTES);

        /**Start Recording*/
        myStateClass.setRecoderingState(true);
        recObj = new AudioRecordFileDecibelFrequencyNoteGraph(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNELS_CONFIGURATION, AUDIO_ENCODING, NO_OF_SAMPLES, new AudioRecordFileDecibelFrequencyNoteGraphListener() {
            @Override
            public void processExecuted() {
            }

            @Override
            public void processExecuting(String decibel, String frequency, String note, float[] ampValues, float[] freqValues) {
                GraphFragment.GRAPH_DOMAIN_MODE = 2;
                GraphFragment.TIME_DOMAIN = true;
                timeDomainGraphFragment.updateGraph(ampValues, freqValues);

                GraphFragment.FREQ_DOMAIN = true;
                freqDomainGraphFragment.updateGraph(freqValues, freqValues);

                /**Updating Text Views*/
                decibel = decibel.substring(0, 4) + "dB";
                decibelTV.setText(decibel);

                if (frequency.length() >= 6) {
                    frequency = frequency.substring(0, 5) + "Hz";
                } else if (frequency.length() == 5) {
                    frequency = frequency.substring(0, 4) + "Hz";
                } else if (frequency.length() == 4) {
                    frequency = frequency.substring(0, 3) + "Hz";
                }
                frequencyTV.setText(frequency);

            }
        });
        recObj.execute();

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
        GraphFragment.GRAPH_DOMAIN_MODE = 0;
        GraphFragment.TIME_DOMAIN = false;
        GraphFragment.FREQ_DOMAIN = false;
    }
}

