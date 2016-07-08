package com.example.keshavdulal.a14_simple_drawing;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    //For Log.d();
    private static final String TAG = MainActivity.class.getSimpleName();

    //
    private static Button record;
    private static Button play;
    private int rec_btn_count = 1;
    private static int play_btn_count =1;
    private static int playState=0; // is 1 when play button is pressed
    private static Boolean isRecording = false;
    private static Boolean isPlaying = false;

    //Fragment
    GraphFragment graphFragment = new GraphFragment();
    ListFragment listFragment = new ListFragment();

    // AudioRecordClass and AudioPlayClass Objects
    AudioRecordClass audioRecordClass;
    AudioPlayClass audioPlayClass;

    //Start of Oncreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG,"Inside onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Graph Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.graphLayout, graphFragment," ");
        fragmentTransaction.commit();

        //List Fragment
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
        fragmentTransaction1.add(R.id.listLayout, listFragment," ");
        fragmentTransaction1.commit();

        //Fixed - Missing APP Name
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Awazz");

        record = (Button) findViewById(R.id.rec);
        play = (Button) findViewById(R.id.play);

        // Start of Record Button
        if(record!=null) {
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    audioRecordClass = new AudioRecordClass();

                    // Start Recording by calling Thread
                    if (rec_btn_count == 1){

                        //RECORD Button
                        Log.d(TAG, "Clicked - Record");
                        record.setText("STOP");
                        record.setTextColor(Color.parseColor("#ff0000"));
                        play.setEnabled(false);

                        isRecording = true;

                        audioRecordClass.execute();


                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

                        rec_btn_count =0;
                    }

                    // Stop Recording
                    else if (rec_btn_count == 0){

                        //STOP Button
                        record.setText("RECORD");
                        record.setTextColor(Color.parseColor("#000000"));
                        play.setEnabled(true);

                        isRecording = false;

                        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_SHORT).show();

                        rec_btn_count =1;


                    }
                }
            });
        }// End of Record Button

        //Start of Play Button
        if(play!=null) {
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    audioPlayClass = new AudioPlayClass();

                    if(play_btn_count == 1){

                        //PLAY Buttton
                        playState = 1;

                        Log.d("TAG", "Clicked - PLAY");
                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        record.setEnabled(false);

                        isPlaying = true;
                        audioPlayClass.execute();

                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();

                        play_btn_count = 0;
                    }

                    else if (play_btn_count == 0){

                        //Code to pause/stop the playback
                        isPlaying = false;

                        play.setText("Play");

                        play.setTextColor(Color.parseColor("#00b900"));
                        record.setEnabled(true);

                        Toast.makeText(getApplicationContext(), "Stopping audio", Toast.LENGTH_SHORT).show();

                        play_btn_count = 1;
                    }

                }
            });
        }//End of Play Button

    }// End of onCreate()

    //Start of AudioRecordClass which extends AsyncTask for Thread implementation
    public static class AudioRecordClass extends AsyncTask{

        private static int recordValueToGraph;

        @Override
        protected Object doInBackground(Object[] objects) {
            startRecord();
            return null;
        }

        //Start of startRecord()
        public void startRecord(){

            Log.d(TAG, "Thread - Start record");
//            WHOLE PROCESS EXPLAINED IN BRIEF HERE:
//                 1.Create a file to store that data values that comes from the mic.
//                 2. Fix the bufferSize and AudioRecord Object.(Will be later in detail later).
//                 3.In java the data comes in the form of bytes-bytes-bytes-and so on.
//                 4.In the file that we have created we can store the same byte recieved.
//                 5.But as we have to use 16 bit PCM ENCODING SYSTEM(Quantitaion), We cannot store the data in Byte form.
//                 6.Thus we convert the data in short datatype and then store the array of short into the file.
//                 7. short(16 bit) = 2*byte(8-bit)
//                 8.And here we have used file to store the audio value from Mic and used the same file to play the Audio.
//                 9.We store the data in file as Short-Short-Short(array of short) and fetch the data in same way to fetch.
//                 10.But simply saying we do not needed to store and fetch from file for recording and playing for ONCE.
//                 11.for that purpose , we can use the array of short datatype
//                 12. Another thing is when we try to open the file via a text editor (notepad /notepad++ used by us) we cannot read
//                 the actual data(short datatype) that we have store in that file.Because we have stored 16bit-16bit-16bit----
//                 and most of the text editor use UTF-8 encoding which is 32-bit.
//                 13.Thus to read the data we have to store it using int datatypte . int-int-int
//                 14.And in this case we have to name the extension as (.txt).But when we store and fetch the data ourselves to mic and speaker
//                 respectively, the extension does not matter at all . To show that I have craeted Three File
    //                 ONE- as extension Sound.pcm
    //                 Two- as extension Sound.haha
    //                 Three- as extension Sound.txt
//                 15. AND MOST IMPORTANT THING TO REMEMBER :- OUR AMPLITUDE IS REPRESENTED BY 16 bit. SO WE USE SHORT
            File filePcm = new File(Environment.getExternalStorageDirectory(),"Sound.pcm");
//          File fileHaha = new File(Environment.getExternalStorageDirectory(),"Sound.haha");
//          File fileTxt = new File(Environment.getExternalStorageDirectory(),"Sound.txt");
//
//          -Above Three are Three different files as discussed above. In first two the files we pass the Array of short as the data
//            to be stored and similarly fetch the data in same way.This is to that the extension does not effect.
//           -And the Third kind of file stores tha data in integer form and has extension .txt so that text Editor(UFT-8) can
//            open and understahnd and show the data.PLEASE, NOTE THAT EXTENSION DOES AFFECT HERE.
//
            try {
                filePcm.createNewFile();
//                fileHaha.createNewFile();
//                fileTxt.createNewFile();

                // Mechanism to store fetch data from mic and store it.
                OutputStream outputStream = new FileOutputStream(filePcm);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

//                // Mechanism to store fetch data from mic and store it.
//                OutputStream outputStream = new FileOutputStream(fileHaha);
//                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
//                DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
//
//                // Mechanism to store fetch data from mic and store it.
//                OutputStream outputStream2 = new FileOutputStream(fileTxt);
//                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(outputStream2);
//                DataOutputStream dataOutputStream2 = new DataOutputStream(bufferedOutputStream2);

//            Call the static class of Audio Record to get the Buffer size in Byte that can handle the Audio data values
//                based on our SAMPLING RATE (44100 hz or frame per second in our case)
                int minBufferSize = AudioRecord.getMinBufferSize(44100,
                        AudioFormat.CHANNEL_IN_DEFAULT,
                        AudioFormat.ENCODING_PCM_16BIT);

                // The array short that will store the Audio data that we get From the mic.
                short[] audioData = new short[minBufferSize];
                float[] audioFloats= new float[audioData.length];

                //Create a Object of the AudioRecord class with the required Samplig Frequency(44100 hz)
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        44100,
                        AudioFormat.CHANNEL_IN_DEFAULT,
                        AudioFormat.ENCODING_PCM_16BIT,
                        minBufferSize);

//             object of the AudioRecord class calls the startRecording() function so that everything(mic,ADC) is ready and the data
//                can be fetch from mic-buffer-our array of short(audioData)
                audioRecord.startRecording();
                //GraphFragment gF = new GraphFragment();
                // it means while the user have  not pressed the STOP Button
                while(isRecording){

//                 numberOfShort=minBufferSize/2
//                   Actually what is happening is the minBufferSize(8 bit Buffer) is being converted to numberOfShort(16 bit buffer)
//                   AND THE MOST IMPORTANT PART IS HERE:- The actual value is being store here in the audioData array.
                    int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

//                This is part where we store that data to our 3 different files.
//                   For now I have used (.haha) and (.txt)
                    //Writing to Files
                    for(int i = 0; i < numberOfShort; i++){

                        //Saving audioData value to 3 different .extension files
                        dataOutputStream.writeShort(audioData[i]); // Store in Sound.PCM file as short-short-short--
//                        dataOutputStream.writeShort(audioData[i]);// Store in Sound.haha file as short-short-short--
//                        dataOutputStream2.writeInt(valu);//Store in Sound.txt as int-int-int--
                        recordValueToGraph = (int)audioData[i];//Convert the short to int to store in txt file
                        audioFloats[i] = ((float)Short.reverseBytes(audioData[i])/0x8000);

                    }

                }
                audioRecord.stop();
                dataOutputStream.close();

                System.out.println("Audio Data: "+ Arrays.toString(audioData));

//                dataOutputStream1.close();
//                dataOutputStream2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//End of StartRecord()

        public  static  int recordValueToGraph(){
            return recordValueToGraph;
        }
    }//End of AudioRecordClass


    public static class AudioPlayClass extends AsyncTask<Void,Void,Void>{

        private static int playValueToGraph;

        @Override
        protected Void doInBackground(Void... voids) {
            playRecord();
            return null;
        }

        //Start of playRecord()
        public void playRecord(){

            File filePcm = new File(Environment.getExternalStorageDirectory(), "Sound.pcm");
            //File fileHaha = new File(Environment.getExternalStorageDirectory(), "Sound.haha");
            int shortSizeInBytes = Short.SIZE/Byte.SIZE;

            final int bufferSizeInBytes = (int)(filePcm.length()/shortSizeInBytes);
            final short[] audioData = new short[bufferSizeInBytes];



            try {
                InputStream inputStream = new FileInputStream(filePcm);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

                final AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        44100,
                        AudioFormat.CHANNEL_IN_DEFAULT,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSizeInBytes,
                        AudioTrack.MODE_STREAM);

                audioTrack.play();
                while(isPlaying) {
                    while (dataInputStream.available() > 0) {
                        int i = 0;
                        while (dataInputStream.available() > 0 && i < audioData.length) {
                            audioData[i] = dataInputStream.readShort();
                            playValueToGraph=audioData[i];
                            i++;
                        }
                        //For Playing
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                audioTrack.write(audioData, 0, bufferSizeInBytes);
                            }
                        }).start();
                        //For Graph

                    }
                }
                audioTrack.pause();
                audioTrack.flush();
                dataInputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//End of playRecord()

        //Start of
        public static int playValueToGraph(){
            return playValueToGraph;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            play.setText("Play");
            play.setTextColor(Color.parseColor("#00b900"));
            record.setEnabled(true);
            play_btn_count = 1;
        }
    }//End of AudioPlay

    public  static int getPlaystate(){
        return  playState;
    }

}//End of MainActivity
