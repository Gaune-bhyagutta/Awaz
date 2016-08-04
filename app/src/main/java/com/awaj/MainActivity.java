package com.awaj;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    ImageView homeIV,listIV,settingsIV;

    public static float frequency;

    //Insatnce Variable And Constants Initialization/Declaration
    static TextView decibelTV;   //To Show Average of the decibel value after each buffer
    static TextView frequencyTV;
    TextView notesTV;

    static Button rec;
    static Button play;

    public static GraphFragment graphFragment = new GraphFragment();
    ListFragment listFragment = new ListFragment();

    //Reference of AudioRecordClass and AudioPlayClass created which are INNER classes.
    AudioRecordClass audioRecordClass;
    AudioPlayClass audioPlayClass;

    public static TextView timerTV;
    public static final Timer timerStartObj = new Timer(3000000, 1000);
    public static ImageView recLogo;
    static int rec_btn_count = 0;
    static int play_btn_count =0;   //To Know Button was Pressed
    public static boolean isRecording = false;    //To Know RECORDING Or STOPPED
    public static boolean isPlaying = false;//To Know PLAYING or STOPPED
    private static int playState = 0;   //TO Know RECORDING or PLAYING

    final String TAG = MainActivity.class.getSimpleName();

    //START---Audio Record and Play Parameters-----
    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public static final int SAMPLE_RATE_IN_HZ = 44100;
    public static final int CHANNELS_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
    public static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


    public static int noOfSamples = 4096;
    public static float resolution = SAMPLE_RATE_IN_HZ / noOfSamples;

    //END---Audio Record and Play Parameters-----
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decibelTV = (TextView) findViewById(R.id.decibel);
        frequencyTV = (TextView) findViewById(R.id.frequencyTV);
        notesTV = (TextView) findViewById(R.id.notesTV);

        rec = (Button) findViewById(R.id.rec);
        play = (Button) findViewById(R.id.play);

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
        timerTV = (TextView) findViewById(R.id.timerTV);
        timerTV.setText("00:00:00");
        /**Recording Logo*/
        recLogo = (ImageView) findViewById(R.id.reclogo);
        recLogo.setVisibility(View.INVISIBLE);

        homeIV = (ImageView) findViewById(R.id.home);
        listIV = (ImageView) findViewById(R.id.list);

        /**HOME - LIST - SETTING Button*/
        listIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toListTabs = new Intent(MainActivity.this, ListTabs.class);
                startActivity(toListTabs);
            }
        });

        /**Start of Record Button*/


        if(rec !=null) {
            rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    audioRecordClass = new AudioRecordClass();

                    if (rec_btn_count == 0){


                        /**Code to handle click of "RECORD" button*/
                        playState = 0;
                        isRecording = true;
                        audioRecordClass.execute();
                        rec_btn_count = 1;

                        rec.setText("STOP");
                        rec.setTextColor(Color.parseColor("#ff0000"));
                        play.setEnabled(false);
                        play.setTextColor(Color.parseColor("#808080"));
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

                        recLogo.setVisibility(View.VISIBLE);

                        timerTV.setText("00:00:00");
                        timerStartObj.start();
                        //freqTV.setText(Float.toString(fundamentalFrequency));
                    } else if (rec_btn_count == 1) {
                        /**Code to handle click of "Rec-STOP" button*/
                        isRecording = false;
                        play.setTextColor(Color.parseColor("#00ff00"));
                        recLogo.setVisibility(View.INVISIBLE);

                        timerStartObj.cancel();

                        timerStartObj.SS=0L;
                        timerStartObj.MM=0L;
                        timerStartObj.HH=0L;
                        timerStartObj.MS=0L;


                    }
                }
            });
        }/**End of Record Button*/

        /**Start of Play Button*/
        if (play != null) {
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    audioPlayClass = new AudioPlayClass();


                    if(play_btn_count == 0){


                        //PLAY Buttton
                        playState =1;

                        play_btn_count = 1;
                        Log.d(TAG, "Clicked - play audio");
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        rec.setEnabled(false);

                        isPlaying = true;

                        audioPlayClass.execute();

                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        rec.setEnabled(false);
                        rec.setTextColor(Color.parseColor("#808080"));
//                        Log.d("VIVZ", "Clicked - play audio");
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        timerTV.setText("00:00:00");
                        timerStartObj.start();
                    }

                    else if (play_btn_count == 1){

                        /**Code to pause/stop the playback*/
                        isPlaying = false;

//                        Log.d("VIVZ", "Clicked - Stop audio");
//                        Log.d("VIVZ", "isPlaying="+isPlaying);
//                        Toast.makeText(getApplicationContext(), "Stopping audio", Toast.LENGTH_SHORT).show();

                        timerStartObj.cancel();

                        timerStartObj.SS=0L;
                        timerStartObj.MM=0L;
                        timerStartObj.HH=0L;
                        timerStartObj.MS=0L;


                    }
                }/**End of Play Button*/
            });
        }
    }

    public static int playState(){
        return playState;
    }

    // Returns the minimum buffer size required for the successful creation of an AudioRecord object, in byte units.
    public int getRecordBufferSize(){
        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING);
        return minBufferSize;
    }
    public int getPlayBufferSize(){
        int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING);

        return minBufferSize;
    }

    public static void updateDecibel(float decibel){
        decibelTV.setText(String.valueOf(decibel));
    }

    public static void updateFrequncy(float frequency){
        frequencyTV.setText(String.valueOf(frequency));
    }

    public static  void updateRecordState(){
        rec.setText("RECORD");
        rec.setTextColor(Color.parseColor("#ffffff"));
        play.setEnabled(true);
//            Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_SHORT).show();

        rec_btn_count =0;
    }



    public  static  void updatePlayState(){
        rec.setTextColor(Color.parseColor("#ffffff"));
        play.setText("play");
        play.setTextColor(Color.parseColor("#00b900"));
        rec.setEnabled(true);
        play_btn_count = 0;

        isPlaying=false;

        timerStartObj.cancel();
        timerStartObj.SS = 0L;
        timerStartObj.MM = 0L;
        timerStartObj.HH = 0L;
        timerStartObj.MS = 0L;
    }




}/**
 * End of MainActivity
 */