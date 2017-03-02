package com.awaj.activities.toneGenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.awaj.R;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class FrequencyGeneratingActivity extends AppCompatActivity {

    Toolbar toolbarObj;
    /**
     * Update current frequency in this textview
     */
    public static TextView currFrequencyTV;
    /**
     * Adjust Volume & frequency levels using the seekbar
     */
    SeekBar volumeSeekbar, freqSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topsix_freq_generator);

        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);

        /**Back Button within Toolbar*/
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Referencing UI Elements*/
        currFrequencyTV = (TextView) findViewById(R.id.currentFrequencyTV);
        volumeSeekbar = (SeekBar) findViewById(R.id.volumeSeekbar);
        freqSeekBar = (SeekBar) findViewById(R.id.freqSeekBar);

        /**Handle Volume SeekBar*/
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /**Handle Frequency SeekBar*/
        freqSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                currFrequencyTV.setText(String.valueOf(seekBar.getProgress()));
//                currFrequencyTV.setText("onProgressChanged");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                currFrequencyTV.setText(String.valueOf(seekBar.getProgress()));
//                currFrequencyTV.setText("onProgressChanged");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                currFrequencyTV.setText(String.valueOf(seekBar.getProgress()));
//                currFrequencyTV.setText("onProgressChanged");
            }
        });
    }

    /**
     * Functionality to the Back Arrow button to go back to previous Activity
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
}
