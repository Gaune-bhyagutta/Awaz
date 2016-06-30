package com.example.keshavdulal.a14_simple_drawing;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


public class MainActivity extends FragmentActivity {
    Boolean recording;
    Button Rec, Play;
    int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rec = (Button) findViewById(R.id.record);
        Play = (Button) findViewById(R.id.play);

        //Play.setEnabled(false);
        if(Rec!=null) {
            Rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    if (count==0){

                    Rec.setText("STOP");
                    Thread recordThread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            recording = true;
                            startRecord();
                        }
                    });

                    recordThread.start();
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                        count =1;
                    }

                    else{
                        recording =false;
                        Rec.setText("RECORD");
                        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
                        count=0;

                    }



                }
            });
        }

        if(Play!=null) {
            Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    playRecord();
                    Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                    //Rec.setEnabled(false);

                }
            });
        }

    }


    void startRecord(){


        File file = new File(Environment.getExternalStorageDirectory(), "sound.pcm");

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(11025,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];

            //float[] audioFloats = new float[audioData.length];


            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    11025,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();

            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
                 for(int i = 0; i < numberOfShort; i++){

                    //audioFloats[i] = ((float)Short.reverseBytes(audioData[i])/0x8000);
                    dataOutputStream.writeShort(audioData[i]);

//                    s[i]=Float.toString(audioFloats[i]);
//                    out = new OutputStreamWriter(openFileOutput(STORETEXT, 0));
//
//                    out.write(s[i]);*/


                }

            }

            audioRecord.stop();


            System.out.println("audio float: "+ Arrays.toString(audioData));
            dataOutputStream.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void playRecord(){

        File file = new File(Environment.getExternalStorageDirectory(), "sound.pcm");
        int shortSizeInBytes = Short.SIZE/Byte.SIZE;

        int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while(dataInputStream.available() > 0){
                audioData[i] = dataInputStream.readShort();
                i++;
            }

            dataInputStream.close();

            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    11025,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();
            audioTrack.write(audioData, 0, bufferSizeInBytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


