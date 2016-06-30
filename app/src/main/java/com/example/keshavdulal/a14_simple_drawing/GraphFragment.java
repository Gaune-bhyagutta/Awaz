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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_fragment, container, false);
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
            //super.onDraw(canvas);
            canvas.drawColor(Color.LTGRAY);          //BACKGROUND
            Paint graphBoundaryObj = new Paint();     //Paint Object
            graphBoundaryObj.setColor(Color.GRAY);
            graphBoundaryObj.setStrokeWidth(7);

            float OX = 0;
            float OY = canvas.getHeight()/2;
            float AX = canvas.getWidth();
            float AY = canvas.getHeight()/2;

            float parallel_dist = 500;
            float graph_height =1;

            //Midline - Divider
            graphBoundaryObj.setStrokeWidth(2);
            canvas.drawLine(OX,OY,AX,AY,graphBoundaryObj);

            //Vertical graphical line
            Paint graphLinesObj = new Paint();        //GLO graph-lines-object
            graphLinesObj.setColor(Color.GRAY);
            graphLinesObj.setStrokeWidth(5);

            int angle=0;
            for(OX = 0; OX<1450;OX+=5){
                graph_height = (float) (100*(Math.sin(angle*3.141/180)+(Math.cos(angle*3.141/180))));
                canvas.drawLine(OX,OY,OX,OY-graph_height,graphLinesObj);
                angle=angle+3;
            }
        }
    }
}
