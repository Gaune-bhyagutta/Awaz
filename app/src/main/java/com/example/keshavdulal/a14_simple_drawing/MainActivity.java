package com.example.keshavdulal.a14_simple_drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(new ViewWithRedDot(this));
//        ViewWithRedDot myViewobj = new ViewWithRedDot(this);

    }

    public static class ViewWithRedDot extends View {
        public ViewWithRedDot(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawColor(Color.BLACK);          //BACKGROUND
            Paint circlePaintObj = new Paint();     //Paint Object
            circlePaintObj.setColor(Color.RED);
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 3, circlePaintObj);
            //x,y,radius,paintobj
        }
    }
}
