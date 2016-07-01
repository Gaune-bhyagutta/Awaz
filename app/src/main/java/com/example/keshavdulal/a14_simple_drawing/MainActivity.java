package com.example.keshavdulal.a14_simple_drawing;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

public class MainActivity extends FragmentActivity {
    Boolean recording;
    Button Rec, Play;
    int rec_btn_count = 0, play_btn_count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Awazz");
        Rec = (Button) findViewById(R.id.rec);
        Play = (Button) findViewById(R.id.play);

        //Play.setEnabled(false);
        // Start of Record Button
        if(Rec!=null) {
            Rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    if (rec_btn_count == 0){
                        //RECORD Button
                        Log.d("VIVZ", "Clicked - Record");
                        Rec.setText("STOP");
                        Rec.setTextColor(Color.parseColor("#ff0000"));
                        Play.setEnabled(false);

                        Thread recordThread = new Thread(new Runnable(){
                            @Override
                            public void run() {
                                recording = true;
                                startRecord();
                            }
                        }); // End of record Thread
                        recordThread.start();
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                        rec_btn_count =1;
                    }

                    else if (rec_btn_count == 1){
                        //STOP Button
                        Log.d("VIVZ", "Clicked - Stop");
                        recording =false;
                        Rec.setText("RECORD");
                        Rec.setTextColor(Color.parseColor("#000000"));
                        Play.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_SHORT).show();
                        rec_btn_count =0;


                    }
                }
            });
        }// End of Record Button

        //Start of Record Button
        if(Play!=null) {
            Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(play_btn_count == 0){
                        //PLAY Buttton
                        Log.d("VIVZ", "Clicked - PLAY");
                        playRecord();
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        Play.setText("Stop");
                        Play.setTextColor(Color.parseColor("#ff0000"));
                        Rec.setEnabled(false);
                        play_btn_count = 1;
                    }

                    else if (play_btn_count == 1){
                        //Code to pause/stop the playback
                        Play.setText("Play");
                        Play.setTextColor(Color.parseColor("#00b900"));
                        Rec.setEnabled(true);
                        play_btn_count = 0;
                    }

                }
            });
        }//End of Play Button

    }// End of onCreate()


    void startRecord(){
        Log.d("VIVZ", "Thread - Start record");
        /* WHOLE PROCESS EXPLAINED IN BRIEF HERE:
            1.Create a file to store that data values that comes from the mic.
            2. Fix the bufferSize and AudioRecord Object.(Will be later in detail later).
            3.In java the data comes in the form of bytes-bytes-bytes-and so on.
            4.In the file that we have created we can store the same byte recieved.
            5.But as we have to use 16 bit PCM ENCODING SYSTEM(Quantitaion), We cannot store the data in Byte form.
            6.Thus we convert the data in short datatype and then store the array of short into the file.
            7. short(16 bit) = 2*byte(8-bit)
            8.And here we have used file to store the audio value from Mic and used the same file to play the Audio.
            9.We store the data in file as Short-Short-Short(array of short) and fetch the data in same way to fetch.
            10.But simply saying we do not needed to store and fetch from file for recording and playing for ONCE.
            11.for that purpose , we can use the array of short datatype
            12. Another thing is when we try to open the file via a text editor (notepad /notepad++ used by us) we cannot read
                the actual data(short datatype) that we have store in that file.Because we have stored 16bit-16bit-16bit----
                and most of the text editor use UTF-8 encoding which is 32-bit.
            13.Thus to read the data we have to store it using int datatypte . int-int-int
            14.And in this case we have to name the extension as (.txt).But when we store and fetch the data ourselves to mic and speaker
                respectively, the extension does not matter at all . To show that I have craeted Three File
                ONE- as extension Sound.pcm
                Two- as extension Sound.haha
                Three- as extension Sound.txt
             15. AND MOST IMPORTANT THING TO REMEMBER :- OUR AMPLITUDE IS REPRESENTED BY 16 bit. SO WE USE SHORT
         */


        File filePcm = new File(Environment.getExternalStorageDirectory(),"Sound.pcm");
        File fileHaha = new File(Environment.getExternalStorageDirectory(),"Sound.haha");
        File fileTxt = new File(Environment.getExternalStorageDirectory(),"Sound.txt");
       /*  -Above Three are Three different files as discussed above. In first two the files we pass the Array of short as the data
            to be stored and similarly fetch the data in same way.This is to that the extension does not effect.
           -And the Third kind of file stores tha data in integer form and has extension .txt so that text Editor(UFT-8) can
            open and understahnd and show the data.PLEASE, NOTE THAT EXTENSION DOES AFFECT HERE.
*/

        try {
            filePcm.createNewFile();
            fileHaha.createNewFile();
            fileTxt.createNewFile();

            // Mechanism to store fetch data from mic and store it.
            OutputStream outputStream = new FileOutputStream(fileHaha);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            // Mechanism to store fetch data from mic and store it.
            OutputStream outputStream1 = new FileOutputStream(filePcm);
            BufferedOutputStream bufferedOutputStream1 = new BufferedOutputStream(outputStream1);
            DataOutputStream dataOutputStream1 = new DataOutputStream(bufferedOutputStream1);

            // Mechanism to store fetch data from mic and store it.
            OutputStream outputStream2 = new FileOutputStream(fileTxt);
            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(outputStream2);
            DataOutputStream dataOutputStream2 = new DataOutputStream(bufferedOutputStream2);

            /*Call the static class of Audio Record to get the Buffer size in Byte that can handle the Audio data values
                based on our SAMPLING RATE (44100 hz or frame per second in our case)
             */
            int minBufferSize = AudioRecord.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT);

            // The array short that will store the Audiio data that we get From the mic.
            short[] audioData = new short[minBufferSize];

            //Create a Object of the AudioRecord class with the required Samplig Frequency(44100 hz)
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    44100,
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            /* object of the AudioRecord class calls the startRecording() function so that every is ready and the data
                can be fetch from mic-buffer-our array of short(audioData)
             */
            audioRecord.startRecording();

            // it means while the user have  not pressed the STOP Button
            while(recording){

                /* numberOfShort=minBufferSize/2
                   Actually what is happening is the minBufferSize(8 bit Buffer) is being converted to numberOfShort(16 bit buffer)
                   AND THE MOST IMPORTANT PART IS HERE:- The actual value is being store here in the audioData array.
                 */
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                /*This is part where we store that data to our 3 different files.
                   For now I have used (.haha) and (.txt)
                 */
                for(int i = 0; i < numberOfShort; i++){
                    dataOutputStream.writeShort(audioData[i]); // Store in Sound.haha file as short-short-short--
                    dataOutputStream1.writeShort(audioData[i]);
                    int temp = (int)audioData[i];//Convert the short to int to store in txt file
                    dataOutputStream2.writeInt(temp);//Store in Sound.txt as int-int-int--
                }

            }

            audioRecord.stop();

            System.out.println("Audio Data: "+ Arrays.toString(audioData));
            dataOutputStream.close();
            dataOutputStream1.close();
            dataOutputStream2.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Start of playRecord()
    void playRecord(){

        File filePcm = new File(Environment.getExternalStorageDirectory(), "Sound.pcm");
        File fileHaha = new File(Environment.getExternalStorageDirectory(), "Sound.haha");
        int shortSizeInBytes = Short.SIZE/Byte.SIZE;

        int bufferSizeInBytes = (int)(filePcm.length()/shortSizeInBytes);
        short[] audioData = new short[bufferSizeInBytes];

        try {
            InputStream inputStream = new FileInputStream(fileHaha);
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
                    44100,
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();
            audioTrack.write(audioData, 0, bufferSizeInBytes);

            //Playback Controls
            if(audioTrack.getPlayState() == audioTrack.PLAYSTATE_PLAYING){
                //Changes to button at the end of playback
                Play.setText("Playing");
                Play.setTextColor(Color.parseColor("#00b900"));
                Rec.setEnabled(true);
                play_btn_count = 0;

            }
            if(audioTrack.getPlayState() == audioTrack.PLAYSTATE_PAUSED){

            }
            if(audioTrack.getPlayState() == audioTrack.PLAYSTATE_STOPPED){
                //Changes to button at the end of playback
                Play.setText("Play");
                //Green
                //Play.setTextColor(Color.parseColor("#00b900"));
                Rec.setEnabled(true);
                play_btn_count = 0;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//End of playRecord()
}//End of MainActivity


