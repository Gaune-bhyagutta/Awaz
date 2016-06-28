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
            //canvas.drawColor(Color.BLACK);          //BACKGROUND
            Paint graphBoundaryObj = new Paint();     //Paint Object
            graphBoundaryObj.setColor(Color.RED);
            graphBoundaryObj.setStrokeWidth(7);

            //canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 3, graphBoundaryObj);
            //x,y,radius,paintobj

            float OX = 0;
            float OY= 900;

            float AX = 1450;
            float AY = 900;

//            float BX = 0;
//            float BY = 150;

            float parallel_dist = 800;
            float graph_height =1;
            //OA
            canvas.drawLine(OX,OY,AX,AY,graphBoundaryObj);
            //OB
            //canvas.drawLine(OX,OY,BX,BY,graphBoundaryObj);

            //Line Parallel to OA
            canvas.drawLine(OX,OY-parallel_dist,AX,AY-parallel_dist,graphBoundaryObj);
            //Midline - Divider
            graphBoundaryObj.setStrokeWidth(1);
            canvas.drawLine(OX,OY-(parallel_dist/2),AX,AY-(parallel_dist/2),graphBoundaryObj);

            //Vertical graphical line
            Paint graphLinesObj = new Paint();        //GLO graph-lines-object
            graphLinesObj.setColor(Color.BLACK);
            //graphLinesObj.setStrokeWidth(1);

//            canvas.drawLine(OX,OY,OX,OY-graph_height,graphLinesObj);
           // int dir = 1;                //1-up 2-down

            //Ups & downs Graph
            int angle=0;
            for(OX = 0; OX<1450;OX+=10){

                graph_height = (float) (200*Math.sin(angle*3.141/180));
                canvas.drawLine(OX,OY-(parallel_dist/2),OX,OY-graph_height-(parallel_dist/2),graphLinesObj);
//
                angle=angle+3;
//

            }


        }
    }
}
