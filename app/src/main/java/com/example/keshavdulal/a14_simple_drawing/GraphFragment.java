package com.example.keshavdulal.a14_simple_drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by imas on 6/28/16.
 */
public class GraphFragment extends Fragment{
    public static  float graph_height;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.rect);
        linearLayout.addView(new ViewWithRedDot(getActivity()));
        return view;
    }

    public static class ViewWithRedDot extends View {
        public ViewWithRedDot(Context context) {

            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //BACKGROUND
            canvas.drawColor(Color.LTGRAY);
            //Paint Object
            Paint graphBoundaryObj = new Paint();
            graphBoundaryObj.setColor(Color.GRAY);
            graphBoundaryObj.setStrokeWidth(7);

            float X1 = 0;
            float Y1 = canvas.getHeight()/2;
            float AX = canvas.getWidth();
            float AY = canvas.getHeight()/2;
            float X2,Y2;

            float parallel_dist = 500;
            // graph_height =1;

            //Midline - Divider
            graphBoundaryObj.setStrokeWidth(2);
            canvas.drawLine(X1,Y1,AX,AY,graphBoundaryObj);

            //Vertical graphical line
            Paint graphLinesObj = new Paint();        //GLO graph-lines-object
            graphLinesObj.setColor(Color.GRAY);
            graphLinesObj.setStrokeWidth(5);

            int angle=0;
           /* MainActivity mainActivity = new MainActivity();
            short[] audioData = mainActivity.getAudioData();*/
            for(X1 = 0; X1<100;X1+=5){
                //invalidate();
                X2=X1;
                Y2=Y1-graph_height;
                canvas.drawLine(X1,Y1,X2,Y2,graphLinesObj);
                graph_height = ((float)AudioRecordClass.temp)/50;
            }
            postInvalidateDelayed(250);

        }
    }
}