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
    static short[] recordAudioData = null;
    static short[] playAudioData = null;
    static MainActivity mainActivity = new MainActivity();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.graph_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.rect);
        linearLayout.addView(new myGraphView(getActivity()));
       // Log.d("VIVZ", "Linear Layout - "+linearLayout.getHeight());
    }

    public static class myGraphView extends View {
        public myGraphView(Context context) {

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
            if (recordAudioData==null){
                int length=mainActivity.getRecordBufferSize();
                recordAudioData = new short[length];
            }
            if (playAudioData==null){
                int length=mainActivity.getPlayBufferSize();
                playAudioData = new short[length];
            }
            int recordBuffIndex = (recordAudioData.length/2-canvas.getWidth())/2;
            int playBuffIndex = (playAudioData.length/2-canvas.getWidth())/2;
            if (MainActivity.playState()==1){
                for(X1 = 0; X1<canvas.getWidth();X1+=5){
                    //invalidate();
                    X2=X1;
                    Y2=Y1-graph_height;
                    canvas.drawLine(X1,Y1,X2,Y2,graphLinesObj);
                    graph_height = ((float) playAudioData[playBuffIndex])/50;
                    playBuffIndex++;
                }
                postInvalidateDelayed(250);
            }else {

                for (X1 = 0; X1 < canvas.getWidth(); X1+=5 ) {
                    //invalidate();
                    X2 = X1;
                    Y2 = Y1 - graph_height;
                    canvas.drawLine(X1, Y1, X2, Y2, graphLinesObj);
                    graph_height = ((float) recordAudioData[recordBuffIndex]) / 50;
                    recordBuffIndex++;
                }
                postInvalidateDelayed(250);
            }


        }
    }
    //updating audiodata from mainactivity
    public void updateRecordGraph(short[] data){
        recordAudioData = data;
    }
    public void setRecordBufferSize(int size){
        recordAudioData = new short[size];
    }
    public void updatePlayGraph(short[] data){
        playAudioData = data;
    }
    public void setPlayBufferSize(int size){
        playAudioData = new short[size];
    }
}