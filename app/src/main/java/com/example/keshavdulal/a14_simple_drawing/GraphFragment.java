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

public class GraphFragment extends Fragment {

    public static float graph_height;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rect);
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
            canvas.drawColor(Color.BLACK);

            /**Boundary Paint Object*/
//            Paint graphBoundaryPO = new Paint();
//            graphBoundaryPO.setColor(Color.parseColor("#880000"));
//            graphBoundaryPO.setStrokeWidth(7);
            /**Visualization Paint Object*/
            Paint graphVisualizationPO = new Paint();
            graphVisualizationPO.setColor(Color.parseColor("#ffffff"));
            graphVisualizationPO.setStrokeWidth(1);
            /**Midline - Divider*/
            //canvas.drawLine(X1, Y1, AX, AY, graphBoundaryPO);

            if (recordAudioData == null) {
                int length = mainActivity.getRecordBufferSize();
                recordAudioData = new short[length];
            }
            if (playAudioData == null) {
                int length = mainActivity.getPlayBufferSize();
                length = length/4;
                playAudioData = new short[length];
            }
            //int playBuffIndex = (playAudioData.length / 2 - canvas.getWidth()) / 2;

            /**ACTUAL PLOTS*/
            drawMeshLines(canvas);
            if (MainActivity.playState() == 1) {
                plotPlayBackVisualization(canvas, graphVisualizationPO);
            }
            else {
                plotRecordingVisualization(canvas, graphVisualizationPO);
            }
        }/**End of onDraw*/

        public void plotPlayBackVisualization(Canvas canvas, Paint graphVisualizationPO){
            int i =0;
            float newX,newY;
            float oldX = 0,oldY = canvas.getHeight()/2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;

            for (X1 = 0; X1 <= canvas.getWidth(); X1++) {
                graph_height = ((float) playAudioData[i]) / 100;
                X2 = X1;
                Y2 = Y1 - graph_height;

                newX = X2;  newY = Y2;
                canvas.drawLine(oldX,oldY, newX,newY, graphVisualizationPO);
                oldX = newX;oldY = newY;

                //playBuffIndex++;
                i++;
            }
            postInvalidateDelayed(1);
        }

        public void plotRecordingVisualization(Canvas canvas, Paint graphVisualizationPO){
            float newX,newY;
            float oldX = 0,oldY = canvas.getHeight()/2;
            int recordBuffIndex = (recordAudioData.length / 2 - canvas.getWidth()) / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;

            for (X1 = 0; X1 <= canvas.getWidth(); X1++) {
                graph_height = ((float) recordAudioData[recordBuffIndex]) / 100;
                X2 = X1;
                Y2 = Y1 - graph_height;
//              canvas.drawLine(X2-1, Y2-1, X2, Y2, graphLinesObj);

                newX = X2;  newY = Y2;
                canvas.drawLine(oldX,oldY, newX,newY, graphVisualizationPO);
                oldX = newX;oldY = newY;
                recordBuffIndex++;
            }
            postInvalidateDelayed(1);
        }

        public void drawMeshLines(Canvas canvas) {
            //Mesh Lines
            int cgh = canvas.getHeight();
            int cgw = canvas.getWidth();
            int cgh2 = cgh / 2;
            int cgw2 = cgw / 2;
            int meshDim = cgh2 / 20;
            int i;
            /**Mesh Paint Object*/
            Paint meshObj = new Paint();
            meshObj.setColor(Color.parseColor("#333333"));
            meshObj.setStrokeWidth(1);

            //Horizontal Lines - Top Segment
            for (i = cgh2; i >= 0; i -= meshDim) {
                canvas.drawLine(0, i, cgw, i, meshObj);
            }
            //Horizontal Lines - Bottom Segment
            for (i = cgh2; i <= cgh; i += meshDim) {
                canvas.drawLine(0, i, cgw, i, meshObj);
            }

            //Vertical Lines - Left Segment
            for (i = cgw2; i >= 0; i -= meshDim) {
                canvas.drawLine(i, 0, i, cgh, meshObj);
            }
            //Vertical Lines - Right Segment
            for (i = cgw2; i <= cgw; i += meshDim) {
                canvas.drawLine(i, 0, i, cgh, meshObj);
            }
        }
    }
    //

    //updating audiodata from mainactivity
    public void updateRecordGraph(short[] data) {
        recordAudioData = data;
    }

    public void setRecordBufferSize(int size) {
        recordAudioData = new short[size];
    }

    public void updatePlayGraph(short[] data) {
        playAudioData = data;
    }

    public void setPlayBufferSize(int size) {
        playAudioData = new short[size];
    }
}