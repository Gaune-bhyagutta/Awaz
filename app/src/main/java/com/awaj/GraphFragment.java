package com.awaj;

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

public class GraphFragment extends Fragment {

    public static float graph_height;
    /**
     * For AMP Visualization
     */
    static float[] recordAudioData = null;
    static float[] playAudioData = null;
//    /**
//     * For FREQ Visualization
//     */
//    static float[] recordAudioDataFreq = null;
//    static short[] playAudioDataFreq = null;

    /**
     * 0-Wave 1-Thread
     */
    public static int GRAPH_VIZ_MODE = 0;
    public static int GRAPH_REFRESH_DELAY = 1;
    /**
     * 0-AMP 1-FREQ
     */
    private int GRAPH_INFO_MODE = 1;

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

    public class myGraphView extends View {
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
                int length = MainActivity.getMinBufferSizeInBytes()/2;
                //length=length/2;
                recordAudioData = new float[length];
            }
            if (playAudioData == null) {
                int length = MainActivity.getMinBufferSizeInBytes()/2;
                //length = length / 4;
                playAudioData = new float[length];
            }
            //int playBuffIndex = (playAudioData.length / 2 - canvas.getWidth()) / 2;

            /**Not my Section*/
//            double heightNormalizer = ((canvas.getHeight()/2)*1/(200));
//            int recordBuffIndex = (recordAudioData.length/2 - canvas.getWidth()) / 2;
//            int recordBuffIndex = 1;

            /**ACTUAL PLOTS*/
            drawMeshLines(canvas);
            if (MainActivity.playState() == 1) {
                plotPlayBackVisualization(canvas, graphVisualizationPO);
            } else if(MainActivity.playState() != 1){
                plotRecordingVisualization(canvas, graphVisualizationPO);
            }
        }
        /**
         * End of onDraw
         */

        public void plotPlayBackVisualization(Canvas canvas, Paint graphVisualizationPO) {
//            int playBuffIndex = (playAudioData.length - canvas.getWidth()) / 2;
            int playBuffIndex = 0;
            float newX, newY;
            float oldX = 0, oldY = canvas.getHeight() / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;
            double heightNormalizer = 0;
            if (GRAPH_INFO_MODE == 0) {
                /**Amp*/
                heightNormalizer = (canvas.getHeight() / 2) * 0.00003051757812;
            } else {
                /**Freq*/
                heightNormalizer = 1;
                playBuffIndex = 1;
            }
//<<<<<<< HEAD
//            for (X1 = 0; X1 < canvas.getWidth(); X1+=(canvas.getWidth()/605))
//            try {
//=======
            for (X1 = 0; X1 <= canvas.getWidth(); X1++) {
                try {
                    graph_height = (float) (playAudioData[playBuffIndex] * heightNormalizer);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                X2 = X1;
                Y2 = Y1 - graph_height;

                if (GRAPH_VIZ_MODE == 0) {
                    /**Wave View*/
                    canvas.drawLine(X1, Y1, X2, Y2, graphVisualizationPO);
                } else if (GRAPH_VIZ_MODE == 1) {
                    /**Thread View*/
                    newX = X2;
                    newY = Y2;
                    canvas.drawLine(oldX, oldY, newX, newY, graphVisualizationPO);
                    oldX = newX;
                    oldY = newY;
                }
                playBuffIndex++;
                postInvalidateDelayed(GRAPH_REFRESH_DELAY);
            }
        }

        public void plotRecordingVisualization(Canvas canvas, Paint graphVisualizationPO) {
            int recordBuffIndex=0;
//            int recordBuffIndex = 1;
            float newX, newY;
            float oldX = 0, oldY = canvas.getHeight() / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;
            double heightNormalizer = 0;
            if (GRAPH_INFO_MODE == 0) {
                /**Amplitude*/
                heightNormalizer = (canvas.getHeight() / 2) * 0.00003051757812;
//                recordBuffIndex = (recordAudioData.length - canvas.getWidth()) / 2;
            } else if(GRAPH_INFO_MODE==1){
                /**Freq*/
                heightNormalizer = 1;
//                recordBuffIndex = 0;
            }

            for (X1 = 0; X1 < canvas.getWidth(); X1++){
                try {
                    graph_height = (float) (recordAudioData[recordBuffIndex] * heightNormalizer);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                X2 = X1;
                Y2 = Y1 - graph_height;

                if (GRAPH_VIZ_MODE == 0) {
                    /**Wave View*/
                    canvas.drawLine(X1, Y1, X2, Y2, graphVisualizationPO);
                } else if (GRAPH_VIZ_MODE == 1) {
                    /**Thread View*/
                    newX = X2;
                    newY = Y2;
                    canvas.drawLine(oldX, oldY, newX, newY, graphVisualizationPO);
                    oldX = newX;
                    oldY = newY;
                }
                recordBuffIndex++;
                postInvalidateDelayed(GRAPH_REFRESH_DELAY);
            }
        }

        public void drawMeshLines(Canvas canvas) {
            //Mesh Lines
            int cgh = canvas.getHeight();
            int cgw = canvas.getWidth();
            int cgh2 = cgh / 2;
            int cgw2 = cgw / 2;
            int meshDim = cgh / 30;
            int i;
            /**Mesh Paint Object*/
            Paint meshObj = new Paint();
            meshObj.setColor(Color.parseColor("#333333"));
            meshObj.setStrokeWidth(1);

            /**Text Paint Object*/
            Paint textObj = new Paint();
            textObj.isAntiAlias();
            textObj.setColor(Color.parseColor("#ff0000"));
            textObj.setTextSize(20);

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

            if (GRAPH_INFO_MODE == 0) {
                /**AMP*/
            } else if (GRAPH_INFO_MODE == 1) {
                /**Freq Labels*/
                /**BASELINE*/
                canvas.drawLine(0, cgh2, cgw, cgh2, textObj);

                /**Vertical Labels*/
                float dbIncrement = 0;
                float yDecrement = 0;
                for (i = 0; i < cgh2; i++) {
                    canvas.drawText(Float.toString(dbIncrement), 0, cgh2, textObj);


                }
//                canvas.drawText("10",0,cgh2-100,textObj);
//                canvas.drawText("20",0,cgh2-200,textObj);
//                canvas.drawText("30",0,cgh2-300,textObj);
//                canvas.drawText("40",0,cgh2-400,textObj);
//                canvas.drawText("50",0,cgh2-500,textObj);
//                canvas.drawText("60",0,cgh2-600,textObj);

                /**Horizontal Labels*/
                float freqValue = 0;
                float xIncrement = 0;
                for (i = 0; i < cgw; i++) {
                    /**Move in X-Axis only*/
                    canvas.drawText(Float.toString(freqValue), xIncrement, cgh2 + 20, textObj);
                    freqValue += 0.5;
                    xIncrement += meshDim * 2;
                }
            }
        }
    }

    /**
     * End of myGraphView
     */

    /**AVOID THIS DIRECT ACCESS !!! */
    /***
     * Amplitude Visualization
     */
    public void updateRecordGraph(float[] data) {
        recordAudioData = data;
    }

    public void updatePlayGraph(float[] data) {
        playAudioData = data;
    }

    public void setMinBufferSizeInBytes(int size) {
        recordAudioData = new float[size];
        playAudioData = new float[size];
    }

    public int getGraphFragmentMode(){
        return GRAPH_INFO_MODE;
    }


//
//    /**
//     * Frequency Visualization
//     */
//    public void updateRecordGraphFreq(float[] data) {
//        recordAudioDataFreq = data;
//    }
//
//    public void updatePlayGraphFreq(short[] data) {
//        playAudioDataFreq = data;
//    }
//
//    public void setRecordBufferSizeFreq(int size) {
//        recordAudioDataFreq = new float[size];
//    }
//
//    public void setPlayBufferSizeFreq(int size) {
//        playAudioDataFreq = new short[size];
//    }
}