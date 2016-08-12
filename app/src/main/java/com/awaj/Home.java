package com.awaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.awaj.activities.*;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class Home extends AppCompatActivity {

    Button Recorder, Monitor, Scaler, NoteGenerator, GuitarTuner, MainActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Recorder = (Button) findViewById(R.id.audioRecord);
        Monitor = (Button) findViewById(R.id.audioMonitor);
        Scaler = (Button) findViewById(R.id.scaleTester);
        NoteGenerator = (Button) findViewById(R.id.noteGenerator);
        GuitarTuner = (Button) findViewById(R.id.guitarTuner);
        MainActivityLauncher = (Button) findViewById(R.id.mainActivityLauncher);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    /**
     * Create Intents in all Methods & Invoke Respective Activities
     */
    public void callAudioRecorder(View view) {
        startActivity(new Intent(Home.this, RecordingActivity.class));
    }

    public void callAudioMonitor(View view) {
        startActivity(new Intent(Home.this, MonitoringActivity.class));
    }

    public void callScaleTester(View view) {
        startActivity(new Intent(Home.this, ScaleTestingActivity.class));
    }

    public void callNoteGenerator(View view) {
        startActivity(new Intent(Home.this, NoteGeneratingActivity.class));
    }

    public void callGuitarTuner(View view) {
        startActivity(new Intent(Home.this, GuitarTuningActivity.class));
    }

    public void callFrequencyGenerator(View view) {
        startActivity(new Intent(Home.this, FrequencyGeneratingActivity.class));
    }

    public void callMainActivity(View view) {
        startActivity(new Intent(Home.this, MainActivity.class));
    }
}
