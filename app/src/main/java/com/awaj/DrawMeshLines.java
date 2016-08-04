package com.awaj;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*** Created by keshavdulal on 04/08/16*/
public class DrawMeshLines {
//    private int GRAPH_DOMAIN_MODE;

    /**Constructor*/
    public DrawMeshLines() {
    }

    /**Draws outline in the canvas*/
    public void DrawMesh(Canvas canvas,int GRAPH_DOMAIN_MODE) {
        //Mesh Lines
        int cgh = canvas.getHeight();
        int cgw = canvas.getWidth();
        int cgh2 = cgh / 2;
        int cgw2 = cgw / 2;
        int meshDim = cgh / 30;
        int i;
        /**Mesh Paint Object*/
        Paint meshObj = new Paint();
        meshObj.setColor(Color.parseColor("#aaaaaa"));
        meshObj.setStrokeWidth(1);

        /**Text Paint Object*/
        Paint textObj = new Paint();
        textObj.isAntiAlias();
        textObj.setColor(Color.parseColor("#aa0000"));
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

        if (GRAPH_DOMAIN_MODE == 0) {
            /**AMP*/
            /**Vertical Labels*/
            int dbIncrement = -90;
            float yDecrement = 0;
            for (i = 0; i < cgh2; i++) {
                canvas.drawText(Integer.toString(dbIncrement), 0, cgh2 - yDecrement + 8, textObj);
                dbIncrement += 10;
                yDecrement += meshDim * 2;
            }
            /**Midline*/
//            if (GRAPH_VIZ_MODE == 0) {
//                textObj.setColor(Color.WHITE);
//                canvas.drawLine(meshDim - 1, cgh2, cgw, cgh2, textObj);
//            }
        } else if (GRAPH_DOMAIN_MODE == 1) {
            /**Freq Labels*/
            /**BASELINE*/
            canvas.drawLine(0, cgh2, cgw, cgh2, textObj);

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
