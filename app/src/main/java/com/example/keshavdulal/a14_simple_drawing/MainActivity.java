package com.example.keshavdulal.a14_simple_drawing;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class MainActivity extends FragmentActivity{
    //Boolean recording;
    Button Rec, Play;
    int rec_btn_count = 0, play_btn_count =0;
    //short[] audioData;
    //public  static  int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Awazz");
        Rec = (Button) findViewById(R.id.rec);
        Play = (Button) findViewById(R.id.play);

        //Play.setEnabled(false);
        // Start of Record Button
        if(Rec!=null) {
            Rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    if (rec_btn_count == 0){
                        //RECORD Button
                        Log.d("VIVZ", "Clicked - Record");
                        Rec.setText("STOP");
                        Rec.setTextColor(Color.parseColor("#ff0000"));
                        Play.setEnabled(false);

                        Thread recordThread = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                AudioRecordClass.startRecord();
                            }
                        }); // End of record Thread
                        recordThread.start();
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                        rec_btn_count =1;
                    }

                    else if (rec_btn_count == 1){
                        //STOP Button
                        Log.d("VIVZ", "Clicked - Stop");
                        AudioRecordClass.stopRecord();
                        Rec.setText("RECORD");
                        Rec.setTextColor(Color.parseColor("#000000"));
                        Play.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_SHORT).show();
                        rec_btn_count =0;


                    }
                }
            });
        }// End of Record Button

        //Start of Record Button
        if(Play!=null) {
            Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(play_btn_count == 0){
                        //PLAY Buttton
                        Log.d("VIVZ", "Clicked - PLAY");
                        AudioPlayClass.playRecord();
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        Play.setText("Stop");
                        Play.setTextColor(Color.parseColor("#ff0000"));
                        Rec.setEnabled(false);
                        play_btn_count = 1;
                    }

                    else if (play_btn_count == 1){
                        //Code to pause/stop the playback
                        Play.setText("Play");
                        Play.setTextColor(Color.parseColor("#00b900"));
                        Rec.setEnabled(true);
                        play_btn_count = 0;
                    }

                }
            });
        }//End of Play Button

    }// End of onCreate()

}//End of MainActivity