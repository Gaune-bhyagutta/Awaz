package com.awaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by keshavdulal on 04/08/16.
 */
public class Splash extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    public void goToMainActivity(View view){
        Intent toMainActivity = new Intent(Splash.this,MainActivity.class);
        startActivity(toMainActivity);
    }
}
