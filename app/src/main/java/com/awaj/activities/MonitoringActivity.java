package com.awaj.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.awaj.GraphFragment;
import com.awaj.R;

/**
 * Created by keshavdulal on 08/08/16.
 */
public class MonitoringActivity extends AppCompatActivity {
    Toolbar toolbarObj;

    LinearLayout timeDomainLL, freqDomainLL;
    private static GraphFragment timeDomainGraphFragment = new GraphFragment();
    private static GraphFragment freqDomainGraphFragment = new GraphFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topsix_monitor);

        /**Back Button within Toolbar*/
        toolbarObj = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarObj);
        /**TODO:fix the back button*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeDomainLL = (LinearLayout) findViewById(R.id.timeFragmentLL);
        freqDomainLL = (LinearLayout) findViewById(R.id.freqFragmentLL);

        /**Start-Time DomainGRAPH FRAGMENT*/
//        FragmentManager timeDomainFragmentMgr = getSupportFragmentManager();
//        FragmentTransaction timeDomainFragmentTransaction = timeDomainFragmentMgr.beginTransaction();
//        timeDomainFragmentTransaction.add(R.id.timeFragmentLL, timeDomainGraphFragment, " ");
//        timeDomainFragmentTransaction.commit();

        /**Start-GRAPH FRAGMENT*/
//        FragmentManager freqDomainFragmentMgr = getSupportFragmentManager();
//        FragmentTransaction freqDomainFragmentTransaction = freqDomainFragmentMgr.beginTransaction();
//        freqDomainFragmentTransaction.add(R.id.freqFragmentLL, freqDomainGraphFragment, " ");
//        freqDomainFragmentTransaction.commit();

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

