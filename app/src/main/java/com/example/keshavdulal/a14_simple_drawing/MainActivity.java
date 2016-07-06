package com.example.keshavdulal.a14_simple_drawing;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    //Fragment
    Button Rec, Play;
    int rec_btn_count = 0, play_btn_count =0;
    GraphFragment graphFragment = new GraphFragment();
    ListFragment listFragment = new ListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.graphLayout, graphFragment," ");
        fragmentTransaction.commit();

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.add(R.id.listLayout, listFragment," ");
        fragmentTransaction1.commit();

        //Fixed - Missing APP Name
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Rec = (Button) findViewById(R.id.rec);
        Play = (Button) findViewById(R.id.play);

        // Start of Record Button
        if(Rec!=null) {
            Rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    if (rec_btn_count == 0){
                        //RECORD Button
                        Log.d(TAG, "onClick: ");

                        Log.d(TAG, "Clicked - Record");
                        Rec.setText("STOP");
                        Rec.setTextColor(Color.parseColor("#ff0000"));
                        Play.setEnabled(false);

                        Thread recordThread = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Log.d(TAG, "Start Recording");
                                AudioRecordClass.startRecord();
                            }
                        }); // End of record Thread
                        recordThread.start();
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                        rec_btn_count =1;
                    }

                    else if (rec_btn_count == 1){
                        //STOP Button
                        AudioRecordClass.stopRecording();
                        Log.d(TAG, "Clicked - Stop");
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
                        Log.d(TAG, "Clicked - PLAY");
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


