package com.awaj;

import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Switch;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.ImageView;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.view.MenuInflater;
import android.media.MediaRecorder;
import android.database.SQLException;
import android.widget.CompoundButton;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;


import java.io.IOException;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbarObj;

    float[] ampValuesForGraph, freqValuesForGraph;

    StateClass stateClass = StateClass.getState();

    DatabaseHelper databaseHelper;

    final Timer timerStartObj = new Timer(3000000, 1000, MainActivity.this);
    //Instance Variable And Constants Initialization/Declaration

    private static ImageView homeIV, listIV, settingsIV;
    private static ImageView recLogo;


    public static TextView decibelTV;   //To Show Average of the decibel value after each buffer
    private static TextView frequencyTV;
    private static TextView notesTV;
    public static TextView timerTV;

    private static Button rec;
    private static Button play;

    private static AudioRecordFileDecibelFrequencyNoteGraph audioRecordFileDecibelFrequencyNoteGraph;
    private static AudioPlayClassMain audioPlayClassMain;


    private static int rec_btn_count = 0;
    private static int play_btn_count = 0;   //To Know Button was Pressed

    private static GraphFragment graphFragment = new GraphFragment();
    private static ListFragment listFragment = new ListFragment();

    int playState = 0;

    private final String TAG = MainActivity.class.getSimpleName();

    //START---Audio Record and Play Parameters-----
    // THE DEFINETIONS ARE DEFINED IN THE RESPECTIVE FUNCTION
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final int CHANNELS_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final int NO_OF_SAMPLES = 4096;
    public static final float RESOLUTION = SAMPLE_RATE_IN_HZ / NO_OF_SAMPLES;

    private static int MIN_BUFFER_SIZE_BYTES = NO_OF_SAMPLES * 2;

    Switch domainSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //database part
        Stetho.initializeWithDefaults(this);
        databaseHelper = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);

        try {
            databaseHelper.createDatabase();
        } catch (IOException e) {
            throw new Error(e);
        }
        try {
            databaseHelper.openDatabase();
        } catch (SQLException e) {
            throw e;
        }
        databaseHelper.getAllData();

        //frequency match test
        int match = databaseHelper.matchFreq(698.972);
        String note = databaseHelper.getNote(match);

        Log.d("VIVZ", "note=" + note + " match=" + match);

        //end of database part

        setContentView(R.layout.activity_main);

        /**Back Button within Toolbar*/
        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        decibelTV = (TextView) findViewById(R.id.decibel);
        frequencyTV = (TextView) findViewById(R.id.frequencyTV);
        notesTV = (TextView) findViewById(R.id.notesTV);

        /**START-Referencing UI Elements*/
        referenceToUIElements();
        /**End-Referencing UI Elements*/

        /**Start-GRAPH FRAGMENT*/
        FragmentManager myFragmentManager = getSupportFragmentManager();
        FragmentTransaction myFragmentTransaction = myFragmentManager.beginTransaction();
        myFragmentTransaction.add(R.id.graphFragmentLL, graphFragment, " ");
        myFragmentTransaction.commit();

        /**LIST FRAGMENT*/
//        FragmentManager fragmentManager1 = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
//        fragmentTransaction1.add(R.id.listLayout, listFragment," ");
//        fragmentTransaction1.commit();
        /**Fixed - Missing APP Name*/
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        /**Referencing UI Elements*/
        rec = (Button) findViewById(R.id.rec);
        play = (Button) findViewById(R.id.play);
        play.setTextColor(Color.parseColor("#808080"));
        /**Timer UI Setups*/
//        timerTV = (TextView) findViewById(R.id.timerTV);
//        timerTV.setText("00:00:00");
        /**Recording Logo*/
        recLogo = (ImageView) findViewById(R.id.reclogo);
        recLogo.setVisibility(View.INVISIBLE);

        homeIV = (ImageView) findViewById(R.id.home);
//        listIV = (ImageView) findViewById(R.id.list);
        /**End-GRAPH FRAGMENT*/

        /**Switches*/
        domainSwitch = (Switch) findViewById(R.id.domainSwitch);

        /**----------------------------------------------------------------------------*/
        /**HOME - LIST - SETTING Button*/
        if (listIV != null) {
            listIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toListTabs = new Intent(MainActivity.this, ListTabs.class);
                    startActivity(toListTabs);
                }
            });
        }

        //Setting To be made before Recording,Playing and Graph Plotting
        graphFragment.setMinBufferSizeInBytes(MIN_BUFFER_SIZE_BYTES);

        /**Start of Record Button*/
        record();
        /**End of Record Button*/

        /**Start of Play Button*/
        play();
        /**End of Play Button*/

    }//End-onCreate()


    // METHOD/FUNCTION DEFINITION SECTION

    // Returns the minimum buffer size required for the successful creation of an AudioRecord object, in byte units.
    public static int setMinBufferSizeInBytes(int x) {
        return x;
    }

    public static int getMinBufferSizeInBytes() {
        return MIN_BUFFER_SIZE_BYTES;
    }


    public static void updateDecibel(String decibel) {
        //The CALCULATED DECIBEL VALUE IN AudioPlayClass/AudioRecordClass is SENT to show in TEXTVIEW
        decibel = decibel.substring(0, 4) + "dB";
        decibelTV.setText(decibel);
    }

    public static void updateFrequncy(String frequency) {
        //The CALCULATED FREQUNCY VALUE IN AudioPlayClass/AudioRecordClass is SENT to show in TEXTVIEW
        if (frequency.length() >= 6) {
            frequency = frequency.substring(0, 5) + "Hz";
        } else if (frequency.length() == 5) {
            frequency = frequency.substring(0, 4) + "Hz";
        } else if (frequency.length() == 4) {
            frequency = frequency.substring(0, 3) + "Hz";
        }
        frequencyTV.setText(frequency);
    }


    public static void updateNotes(String notes) {
        //The CALCULATED FREQUNCY VALUE IN AudioPlayClassMain/AudioRecordClass i SENT to show in TEXTVIEW
        notesTV.setText(String.valueOf(notes));
    }

    public static void updateRecordState() {
        rec.setText("RECORD");
        rec.setTextColor(Color.parseColor("#ffffff"));
        play.setEnabled(true);
        rec_btn_count = 0;
    }

    public void updatePlayState() {
        rec.setTextColor(Color.parseColor("#ffffff"));
        play.setText("play");
        play.setTextColor(Color.parseColor("#00b900"));
        rec.setEnabled(true);
        play_btn_count = 0;

        //= false;

        timerStartObj.cancel();
        timerStartObj.SS = 0L;
        timerStartObj.MM = 0L;
        timerStartObj.HH = 0L;
        timerStartObj.MS = 0L;
    }


    //START OF Graph Fragment Section
    public static void plotGraph(float[] audioFloatsForAmp, float[] fftOutput) {
        graphFragment.updateGraph(audioFloatsForAmp, fftOutput);
    }

    //END OF Graph Fragment Section
    //Start-Functions in MainActivity
    //Start-record()
    public void record() {
        if (rec != null) {

            rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    audioRecordFileDecibelFrequencyNoteGraph = new AudioRecordFileDecibelFrequencyNoteGraph(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNELS_CONFIGURATION, AUDIO_ENCODING,
                            NO_OF_SAMPLES, new AudioRecordFileDecibelFrequencyNoteGraphListener() {
                        @Override
                        public void processExecuting(String decibel, String frequency, String note, float[] ampValues, float freqValues[]) {
                            updateNotes(note);
                            updateFrequncy(frequency);
                            updateDecibel(decibel);
                            ampValuesForGraph = ampValues;
                            freqValuesForGraph = freqValues;
                            plotGraph(ampValues, freqValues);
                        }

                        @Override
                        public void processExecuted() {
                            //Toast.makeText(getApplicationContext(),"Finished Recording",Toast.LENGTH_SHORT).show();
                            updateRecordState();
                        }
                    });

                    if (rec_btn_count == 0) {

                        /**Code to handle click of "RECORD" button*/
                        playState = 0;
                        stateClass.setRecoderingState(true);

                        audioRecordFileDecibelFrequencyNoteGraph.execute();

                        rec_btn_count = 1;

                        rec.setText("STOP");
                        rec.setTextColor(Color.parseColor("#ff0000"));
                        play.setEnabled(false);
                        play.setTextColor(Color.parseColor("#808080"));
                        Toast.makeText(MyApplication.getAppContext(), "Recording started", Toast.LENGTH_SHORT).show();

//                        timerTV.setText("00:00:00");
                        timerStartObj.start();
                        recLogo.setVisibility(View.VISIBLE);
                    } else if (rec_btn_count == 1) {
                        /**Code to handle click of "Rec-STOP" button*/
                        stateClass.setRecoderingState(false);
                        play.setTextColor(Color.parseColor("#00ff00"));
                        recLogo.setVisibility(View.INVISIBLE);

                        timerStartObj.cancel();
                        timerStartObj.SS = 0L;
                        timerStartObj.MM = 0L;
                        timerStartObj.HH = 0L;
                        timerStartObj.MS = 0L;
                    }
                }
            });
        }
    }//End-record()

    //Start-play()
    public void play() {
        if (play != null) {
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    audioPlayClassMain = new AudioPlayFrequencyDbGraph(new AudioPlayFrequencyDbGraphListener() {

                        @Override
                        public void processExecuting(Float frequency, String note, Float db, float[] fftValues, float[] ampValues) {

                            updateDecibel(String.valueOf(db));
                            updateFrequncy(String.valueOf(frequency));
                            updateNotes(note);
                            ampValuesForGraph = ampValues;
                            freqValuesForGraph = fftValues;
                            plotGraph(ampValuesForGraph, freqValuesForGraph);
                        }

                        @Override
                        public void processExecuting(Float frequency, String note, Float db) {

                        }

                        @Override
                        public void processExecuting(Float frequency, String note) {

                        }

                        @Override
                        public void processExecuting() {

                        }

                        @Override
                        public void processExecuted() {
                            updatePlayState();
                        }
                    });

                    if (play_btn_count == 0) {
                        playState = 1;
                        play_btn_count = 1;
                        Log.d(TAG, "Clicked - play audio");
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        rec.setEnabled(false);

                        stateClass.setPlayingState(true);

                        audioPlayClassMain.execute();


                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        rec.setEnabled(false);
                        rec.setTextColor(Color.parseColor("#808080"));
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();

//                        timerTV.setText("00:00:00");
                        timerStartObj.start();
                        recLogo.setVisibility(View.INVISIBLE);
                    } else if (play_btn_count == 1) {
                        /**Code to pause/stop the playback*/
                        stateClass.setPlayingState(false);

                        timerStartObj.cancel();
                        timerStartObj.SS = 0L;
                        timerStartObj.MM = 0L;
                        timerStartObj.HH = 0L;
                        timerStartObj.MS = 0L;

                        recLogo.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    /**
     * End of onCreate
     */

    @Override
    protected void onResume() {
        super.onResume();

        /**Monitoring Domain Switch States*/
        domainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final boolean isDomainSwitchChecked = domainSwitch.isChecked();
                if (isDomainSwitchChecked) {
                    GraphFragment.GRAPH_DOMAIN_MODE = 1;
//                    domainSwitch.setText("Freq");
                } else {
                    GraphFragment.GRAPH_DOMAIN_MODE = 0;
//                    domainSwitch.setText("AMP");
                }
            }
        });
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

    protected void onPause() {
        super.onPause();
        stateClass.setRecoderingState(false);
    }

    public void referenceToUIElements() {
        //Start-To Show Decibels,Frequncy and Notes
        decibelTV = (TextView) findViewById(R.id.decibel);
        frequencyTV = (TextView) findViewById(R.id.frequencyTV);
        notesTV = (TextView) findViewById(R.id.notesTV);
        //End- To Show Decibels,Frequncy and Notes

        //Start-To Record and Play
        rec = (Button) findViewById(R.id.rec);
        play = (Button) findViewById(R.id.play);
        play.setTextColor(Color.parseColor("#808080"));
        //End-To Record and Play


        /**Start-Timer UI Setups*/
        timerTV = (TextView) findViewById(R.id.timerTV);
        timerTV.setText("00:00:00");
        recLogo = (ImageView) findViewById(R.id.reclogo);
        recLogo.setVisibility(View.INVISIBLE);
        homeIV = (ImageView) findViewById(R.id.home);
//        listIV = (ImageView) findViewById(R.id.list);
    }
}/***
 * End of MainActivity
 */