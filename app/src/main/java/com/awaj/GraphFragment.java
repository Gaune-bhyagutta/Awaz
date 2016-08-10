package com.awaj;

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
    /**For AMP Visualization*/
    static float[] recordAudioData = null;
    static float[] playAudioData = null;
//    /**
//     * For FREQ Visualization
//     */
//    static float[] recordAudioDataFreq = null;
//    static short[] playAudioDataFreq = null;

    static MainActivity mainActivity = new MainActivity();

    /**
     * 0-Wave 1-Thread
     */
    public static int GRAPH_VIZ_MODE = 0;
    public static int GRAPH_REFRESH_DELAY = 10;
    /**
     * 0-AMP 1-FREQ
     */
    public static int GRAPH_DOMAIN_MODE = 0;

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
            canvas.drawColor(Color.parseColor("#dddddd"));

            /**Boundary Paint Object*/
//            Paint graphBoundaryPO = new Paint();
//            graphBoundaryPO.setColor(Color.parseColor("#880000"));
//            graphBoundaryPO.setStrokeWidth(7);
            /**Visualization Paint Object*/
            Paint graphVisualizationPO = new Paint();
            graphVisualizationPO.setColor(Color.parseColor("#333333"));
            graphVisualizationPO.setStrokeWidth(1);
            /**Midline - Divider*/
            //canvas.drawLine(X1, Y1, AX, AY, graphBoundaryPO);

            if (recordAudioData == null) {
                int length = mainActivity.getRecordBufferSize();
                //length=length/2;
                recordAudioData = new float[length];
            }
            if (playAudioData == null) {
                int length = mainActivity.getPlayBufferSize();
                length = length / 4;
                playAudioData = new float[length];
            }

            /**ACTUAL PLOTS*/
            DrawMeshLines dml = new DrawMeshLines();
            /**TODO: update method name Follow Convention of: aBcdEfgh*/
            dml.DrawMesh(canvas, GRAPH_DOMAIN_MODE);

            if (MainActivity.playState() == 1) {
//                plotVisualization(canvas, graphVisualizationPO, playAudioData);
                plotPlayBackVisualization(canvas, graphVisualizationPO);
            } else if (MainActivity.playState() != 1) {
                plotRecordingVisualization(canvas, graphVisualizationPO);
//                plotVisualization(canvas, graphVisualizationPO, recordAudioData);
            }
            /*** End of onDraw */
        }

        //        public void plotVisualization(Canvas canvas, Paint graphVisualizationPO, float dataArray[]) {
////            int playBuffIndex = (playAudioData.length - canvas.getWidth()) / 2;
//            int playBuffIndex = 0;
//            int recordBuffIndex = 0;
////            int index = 0;
//            float newX, newY, xIncrementFactor = 1;
//            float oldX = 0, oldY = canvas.getHeight() / 2;
//            float X1 = 0;
//            float Y1 = canvas.getHeight() / 2;
//            float X2, Y2;
//            double heightNormalizer = 0;
//
//            /**Left Side Limiter for Visualization*/
//            int dim = canvas.getHeight() / 30;
//            if (GRAPH_DOMAIN_MODE == 0) {
//                /**Amp*/
//                heightNormalizer = (canvas.getHeight() / 2) * 0.00003051757812;
//            } else if (GRAPH_VIZ_MODE == 1) {
//                /**Freq*/
//                int freq = 7000;
//                heightNormalizer = 1;
////                index = 0;
//                xIncrementFactor = canvas.getWidth() / (freq / MainActivity.resolution);
//            }
//            for (X1 = dim; X1 <= canvas.getWidth(); X1 += xIncrementFactor) {
//                try {
////                    graph_height = (float) (dataArray[index] * heightNormalizer);
//                    if(MainActivity.playState()==1){
//                        graph_height = (float) (playAudioData[playBuffIndex] * heightNormalizer);
//                    }
//                    else {
//                        graph_height=(float) (recordAudioData[recordBuffIndex]*heightNormalizer);
//                    }
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//                X2 = X1;
//                Y2 = Y1 - graph_height;
//
//                if (GRAPH_VIZ_MODE == 0) {
//                    /**Wave View*/
//                    canvas.drawLine(X1, Y1, X2, Y2, graphVisualizationPO);
//                } else if (GRAPH_VIZ_MODE == 1) {
//                    /**Thread View*/
//                    newX = X2;
//                    newY = Y2;
//                    canvas.drawLine(oldX, oldY, newX, newY, graphVisualizationPO);
//                    oldX = newX;
//                    oldY = newY;
//                }
//                playBuffIndex++;
//                recordBuffIndex++;
//                postInvalidateDelayed(GRAPH_REFRESH_DELAY);
//            }
////                int maxValue = (int) (FrequencyValue.findMaxValue(playAudioData) * heightNormalizer);
//            int horizontalBarHeight = (int) ((canvas.getHeight() / 2) - graph_height);
////                graphVisualizationPO.setColor(Color.parseColor("#ff0000"));
//            graphVisualizationPO.setStrokeWidth(2);
//            canvas.drawLine(0, horizontalBarHeight, canvas.getWidth(), horizontalBarHeight, graphVisualizationPO);
////            postInvalidateDelayed(GRAPH_REFRESH_DELAY);
//        }
        public void plotRecordingVisualization(Canvas canvas, Paint graphVisualizationPO) {
            int recordBuffIndex = 0;
            float newX, newY, xIncrementFactor = 1;
            float oldX = 0, oldY = canvas.getHeight() / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;
            double heightNormalizer = 0;

            /**Left Side Limiter for Visualization*/
            int dim = canvas.getHeight() / 30;
            if (GRAPH_DOMAIN_MODE == 0) {
                /**Amplitude*/
                heightNormalizer = (canvas.getHeight() / 2) * 0.00003051757812;
            } else if (GRAPH_DOMAIN_MODE == 1) {
                /**Freq*/
                int freq = 7000;
                heightNormalizer = 1;
//                recordBuffIndex = 0;
                xIncrementFactor = canvas.getWidth() / (freq / MainActivity.resolution);
            }
            for (X1 = dim; X1 <= canvas.getWidth(); X1 += xIncrementFactor) {
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
            /**Expt*/
            int maxValue = (int) (FrequencyValue.findMaxValue(recordAudioData) * heightNormalizer);
            int horizontalBarHeight = (int) ((canvas.getHeight() / 2) - maxValue);
            graphVisualizationPO.setColor(Color.parseColor("#ff0000"));
            graphVisualizationPO.setStrokeWidth(2);
            canvas.drawLine(0, horizontalBarHeight, canvas.getWidth(), horizontalBarHeight, graphVisualizationPO);
        }

        public void plotPlayBackVisualization(Canvas canvas, Paint graphVisualizationPO) {
            int playBuffIndex = 0;
            float newX, newY, xIncrementFactor = 1;
            float oldX = 0, oldY = canvas.getHeight() / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;
            double heightNormalizer = 0;

            /**Left Side Limiter for Visualization*/
            int dim = canvas.getHeight() / 30;
            if (GRAPH_DOMAIN_MODE == 0) {
                /**Amp*/
                heightNormalizer = (canvas.getHeight() / 2) * 0.00003051757812;
            } else if (GRAPH_VIZ_MODE == 1) {
                /**Freq*/
                int freq = 7000;
                heightNormalizer = 1;
//                playBuffIndex = 0;
                xIncrementFactor = canvas.getWidth() / (freq / MainActivity.resolution);
            }
            for (X1 = dim; X1 <= canvas.getWidth(); X1 += xIncrementFactor) {
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
            int maxValue = (int) (FrequencyValue.findMaxValue(playAudioData) * heightNormalizer);
            int horizontalBarHeight = (int) ((canvas.getHeight() / 2) - maxValue);
            graphVisualizationPO.setColor(Color.parseColor("#ff0000"));
            graphVisualizationPO.setStrokeWidth(2);
            canvas.drawLine(0, horizontalBarHeight, canvas.getWidth(), horizontalBarHeight, graphVisualizationPO);
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

    public void setRecordBufferSize(int size) {
        recordAudioData = new float[size];
    }

    public void setPlayBufferSize(int size) {
        playAudioData = new float[size];
    }
}