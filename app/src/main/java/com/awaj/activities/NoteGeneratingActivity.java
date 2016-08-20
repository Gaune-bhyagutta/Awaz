package com.awaj.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.awaj.R;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class NoteGeneratingActivity extends AppCompatActivity {
    private Toolbar toolbarObj;
    private Button monitorButton;
    private boolean monitorButtonState = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topsix_note_generator);

        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);

        /**Back Button within Toolbar*/
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        monitorButton = (Button) findViewById(R.id.monitorButton);

        monitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**Monitoring*/
                if (monitorButtonState == true) {
                    monitorButtonState = false;
                    monitorButton.setText("Start Monitoring");
                }
                /**Not Monitoring*/
                else {
                    monitorButtonState = true;
                    monitorButton.setText("Stop Monitoring");
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
}
