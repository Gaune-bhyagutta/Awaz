package com.awaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.awaj.activities.*;
import com.awaj.activities.toneGenerator.FrequencyGeneratingActivity;
import com.awaj.menuPages.About;
import com.awaj.menuPages.Manual;
import com.awaj.menuPages.Settings;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class Home extends AppCompatActivity {

    /**
     * Buttons in Home Page
     */
    Button Recorder, Monitor, Scaler, NoteGenerator, GuitarTuner, MainActivityLauncher;
    /**
     * Linear Layout to encapsulate above buttons
     */
    LinearLayout RecorderLL, MonitorLL, ScalerLL, NoteGeneratorLL, GuitarTunerLL, MainActivityLauncherLL;

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

        RecorderLL = (LinearLayout) findViewById(R.id.RecorderLL);
        MonitorLL = (LinearLayout) findViewById(R.id.MonitorLL);
        ScalerLL = (LinearLayout) findViewById(R.id.vocalTesterLL);
        NoteGeneratorLL = (LinearLayout) findViewById(R.id.noteGeneratorLL);
        GuitarTunerLL = (LinearLayout) findViewById(R.id.guitarTunerLL);
        MainActivityLauncherLL = (LinearLayout) findViewById(R.id.fourth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Recorder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;

            }
        });
    }

    /**
     * Menu options inflated in Appbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater ourMenuInflater = getMenuInflater();
        ourMenuInflater.inflate(R.menu.sample_menu, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
//            this.showAbout();
            startActivity(new Intent(Home.this, About.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**Invoke Menu Options*/
//    public void showManual(){
//
//    }
//    public void showAbout(){
//
//    }
//    public void showSettings(){
//
//    }

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
    /**End of Methods that use Explicit Intents*/
}
