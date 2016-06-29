package com.example.keshavdulal.a14_simple_drawing;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button Rec = (Button)findViewById(R.id.button);

        if(Rec!=null){
            Rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String s= Rec.getText().toString();
//                    if(s=="RECORD")
                    Rec.setText("STOP");
//                    String sh= Rec.getText().toString();
//                    if (sh=="STOP"){
//                        Rec.setText("PLAY");
//                    }
                }
            });
        }

    }

}
