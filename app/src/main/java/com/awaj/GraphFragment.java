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

    private float graph_height;
    private float[] audioData =null;

   /**
     * 0-Wave 1-Thread
     */
    private int GRAPH_VIZ_MODE = 0;
    private int GRAPH_REFRESH_DELAY = 1;
    /**
     * 0-AMP/TIME 1-AMP/FREQ
     */
    private int GRAPH_INFO_MODE = 0;

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

            /**Visualization Paint Object*/
            Paint graphVisualizationPO = new Paint();
            graphVisualizationPO.setColor(Color.parseColor("#ffffff"));
            graphVisualizationPO.setStrokeWidth(1);


//            if (audioData == null) {
//                audioData = new float[length];
//            }

            /**ACTUAL PLOTS*/
            drawMeshLines(canvas);
            if (GRAPH_INFO_MODE== 1) {
                frequencyAmplitudeGraph(canvas, graphVisualizationPO);
            } else {
                timeAmplitudeGraph(canvas, graphVisualizationPO);
            }
        }
        /**
         * End of onDraw
         */

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

        public void timeAmplitudeGraph(Canvas canvas, Paint graphVisualizationPO){
            double heightNormalizer = (canvas.getHeight() / 2) * 0.00003051757812;
            int index = 0;
            float newX, newY;
            float oldX = 0, oldY = canvas.getHeight() / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;

            for (X1 = 0; X1 <= canvas.getWidth(); X1++) {
                try {
                    graph_height = (float) (audioData[index] * heightNormalizer);
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
                index++;
                postInvalidateDelayed(GRAPH_REFRESH_DELAY);
            }
        }

        public void frequencyAmplitudeGraph(Canvas canvas, Paint graphVisualizationPO){

            double heightNormalizer = 1;
            int index = 0;
            float newX, newY;
            float oldX = 0, oldY = canvas.getHeight() / 2;
            float X1 = 0;
            float Y1 = canvas.getHeight() / 2;
            float X2, Y2;

            for (X1 = 0; X1 <= canvas.getWidth(); X1++) {
                try {
                    graph_height = (float) (audioData[index] * heightNormalizer);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                X2 = X1;
                Y2 = Y1 - graph_height;

                canvas.drawLine(X1, Y1, X2, Y2, graphVisualizationPO);

                index++;
                postInvalidateDelayed(GRAPH_REFRESH_DELAY);
            }

        }
    }

    /**
     * End of myGraphView
     */
    public void updateGraph(float[] data) {
        audioData = data;
    }

    public void setMinBufferSizeInBytes(int size) {
        audioData = new float[size];
    }

    public int getGraphFragmentMode(){
        return GRAPH_INFO_MODE;
    }


}