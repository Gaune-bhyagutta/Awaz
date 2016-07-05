package com.example.keshavdulal.a14_simple_drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GraphFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.rect);
        linearLayout.addView(new myGraphView(getActivity()));
        Log.d("VIVZ", "Linear Layout - "+linearLayout.getHeight());
        return view;
    }

    public static class myGraphView extends View {
        public myGraphView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(final Canvas canvas) {
            super.onDraw(canvas);
            //Sine Height for demo
            //graph_height = (float) (100*(Math.sin(angle*3.141/180)+(Math.cos(angle*3.141/180))));
            //Random Graph Demo
            //graph_height = (float) (500 * Math.random());
            //graph_height = (float) MainActivity.temp;
            //angle=angle+3;
//            Thread height_generator = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    fine_tune();
//                }
//            });
            //BACKGROUND
            canvas.drawColor(Color.LTGRAY);
            //Paint Object
            Paint graphBoundaryObj = new Paint();
            graphBoundaryObj.setColor(Color.GRAY);
            graphBoundaryObj.setStrokeWidth(7);

            float OX = 0;
            float OY = canvas.getHeight()/2;
            float AX = canvas.getWidth();
            float AY = canvas.getHeight()/2;
            float graph_height;
            float hp_avg = 2200;
            float height_processor = 0;
            float x = hp_avg/(canvas.getHeight()/500);
            //Log.d("VIVZ", "Canvas.getHeight = "+canvas.getHeight());
            //Height = 1118

            //Midline - Divider
            graphBoundaryObj.setStrokeWidth(2);
            canvas.drawLine(OX,OY,AX,AY,graphBoundaryObj);

            //Vertical graphical line
            Paint graphLinesObj = new Paint();        //GLO graph-lines-object
            graphLinesObj.setColor(Color.RED);
            graphLinesObj.setStrokeWidth(1);

            for(OX = 0; OX<canvas.getWidth();OX++){
                height_processor = 0;
                //float [] height_processor = new float[hp_avg];
                for (int j = 0; j < hp_avg; j++){
                    height_processor += (float) AudioRecordClass.valueToGraph();
                }
                graph_height = height_processor/x;
                canvas.drawLine(OX,OY,OX,OY-graph_height,graphLinesObj);
            }
            invalidate();
            //postInvalidateDelayed(100);
        }
    }
//    void fine_tune(Canvas canvas){}
}