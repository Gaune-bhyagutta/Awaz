package com.awaj.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.awaj.R;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class MonitoringActivity extends AppCompatActivity {
    Toolbar toolbarObj;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.topsix_monitor);

            /**Back Button within Toolbar*/
            toolbarObj = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbarObj);
            /**TODO:fix the back button*/
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    /**Adding Functionality to the Back Arrow button to go back to previous Activity*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }

