package com.awaj.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.awaj.GraphFragment;
import com.awaj.MainActivity;
import com.awaj.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topsix_recorder);

        /**Back Button within Toolbar*/
        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecordingStateTV = (TextView) findViewById(R.id.recStateTV);
        RecordingButton = (Button) findViewById(R.id.recButton);

        /**Default Configs in UI*/
        RecordingStateTV.setBackgroundResource(R.drawable.greengradient);
        RecordingStateTV.setText("Ready to Record");
        RecordingButton.setText("REC");
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
        if (isRecording == true) {
            isRecording = false;
//            MainActivity.record();
            /**Timer*/
            timerTV.setText("00:00:00");
            timerStartObj.start();
            /**UI Changes*/
            RecordingStateTV.setBackgroundResource(R.drawable.redgradient);
            RecordingStateTV.setText("Recording...");
            RecordingButton.setText("STOP");
            RecordingButton.setBackgroundResource(R.drawable.greengradient);
        }
        /**Is not Recording*/
        else {
            isRecording = true;
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
        }
    }
}
