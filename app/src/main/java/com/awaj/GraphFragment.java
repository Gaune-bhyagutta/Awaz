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

    myGraphView myGraphView = new myGraphView(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rect);
        linearLayout.addView(myGraphView);
        // Log.d("VIVZ", "Linear Layout - "+linearLayout.getHeight());
    }



    /**
     * End of myGraphView
     */
    public void updateGraph(float[] data) {
        myGraphView.updateGraph(data);
    }

    public void setMinBufferSizeInBytes(int size) {
        myGraphView.setMinBufferSizeInBytes(size);
    }

    public int getGraphFragmentMode(){
        return myGraphView.getGraphFragmentMode();
    }


}