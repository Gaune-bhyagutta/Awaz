package com.awaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class Home extends AppCompatActivity {

    Button Recorder,Monitor,Scaler,NoteGenerator,GuitarTuner,MainActivityLauncher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Recorder.findViewById(R.id.audioRecord);
        Monitor.findViewById(R.id.audioMonitor);
        Scaler.findViewById(R.id.scaleTester);
        NoteGenerator.findViewById(R.id.noteGenerator);
        GuitarTuner.findViewById(R.id.guitarTuner);
        MainActivityLauncher.findViewById(R.id.mainActivityLauncher);

    }

    /**Create Intents in all Methods & Invoke Respective Activities*/
    public void callAudioRecorder(){
        Intent myIntent = new Intent(Home.this,RecordingActivity.class);
        startActivity(myIntent);
    }
    public void callAudioMonitor(){
        Intent myIntent = new Intent(Home.this,MonitoringActivity.class);
        startActivity(myIntent);
    }
    public void callScaleTester(){
        Intent myIntent = new Intent(Home.this,ScaleTestingActivity.class);
        startActivity(myIntent);
    }
    public void callNoteGenerator(){
        Intent myIntent = new Intent(Home.this,NoteGeneratingActivity.class);
        startActivity(myIntent);
    }
    public void callGuitarTuner(){
        Intent myIntent = new Intent(Home.this,GuitarTuningActivity.class);
        startActivity(myIntent);
    }
    public void callToneGenerator(){
        Intent myIntent = new Intent(Home.this,ToneGenearatingActivity.class);
        startActivity(myIntent);
    }
    public void callMainActivity(){
        Intent myIntent = new Intent(Home.this,MainActivity.class);
        startActivity(myIntent);
    }
}
