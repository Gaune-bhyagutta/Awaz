package com.awaj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by keshavdulal on 04/08/16.
 */
public class Splash extends AppCompatActivity{
    ImageView splashIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        splashIcon = (ImageView) findViewById(R.id.splashIcon);
    }

    public void goToMainActivity(View view){
        Intent toMainActivity = new Intent(Splash.this,MainActivity.class);
        startActivity(toMainActivity);
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        /**Animation Begins*/
        Animation splashAnimation = AnimationUtils.loadAnimation(Splash.this,R.layout.splash_animation);
        splashIcon.startAnimation(splashAnimation);
    }
}
