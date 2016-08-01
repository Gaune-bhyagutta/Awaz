package com.awaj;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.TextView;
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
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    ImageView homeIV,listIV,settingsIV;
    static TextView timerTV;
    final Timer timerStartObj = new Timer(3000000,1000);
    static ImageView recLogo;



    //Insatnce Variable And Constants Initialization/Declaration
    TextView decibel;   //To Show Average of the decibel value after each buffer

    Button rec, play;

    GraphFragment graphFragment = new GraphFragment();
    ListFragment listFragment = new ListFragment();

    //Reference of AudioRecordClass and AudioPlayClass created which are INNER classes.
    AudioRecordClass audioRecordClass;
    AudioPlayClass audioPlayClass;

    int rec_btn_count = 0, play_btn_count =0;   //To Know Button was Pressed
    private boolean isRecording = false;    //To Know RECORDING Or STOPPED
    private boolean isPlaying = false;//To Know PLAYING or STOPPED
    private static int playState = 0;   //TO Know RECORDING or PLAYING

    final String TAG = MainActivity.class.getSimpleName();

    //Audio Record Parameters
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final int CHANNELS_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decibel = (TextView) findViewById(R.id.decibel);


        decibel = (TextView) findViewById(R.id.decibel);
        rec = (Button) findViewById(R.id.rec);
        play = (Button) findViewById(R.id.play);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.graphFragmentLL, graphFragment," ");
        fragmentTransaction.commit();


        /**LIST FRAGMENT*/
//        FragmentManager fragmentManager1 = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
//        fragmentTransaction1.add(R.id.listLayout, listFragment," ");
//        fragmentTransaction1.commit();
        /**Fixed - Missing APP Name*/
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("A-W-A-J");
//        setTitle(Html.fromHtml("AWAJ"));
        timerTV = (TextView) findViewById(R.id.timerTV);
        rec = (Button) findViewById(R.id.rec);
        play = (Button) findViewById(R.id.play);
        play.setTextColor(Color.parseColor("#808080"));
        timerTV.setText("00:00:00");
        recLogo = (ImageView)findViewById(R.id.reclogo);
        recLogo.setVisibility(View.INVISIBLE);

        homeIV = (ImageView) findViewById(R.id.home);
        listIV = (ImageView) findViewById(R.id.list);

        /**HOME - LIST - SETTING Button*/
        listIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toListTabs = new Intent(MainActivity.this, ListTabs.class);
                startActivity(toListTabs);
            }
        });

        /**Start of Record Button*/

        if(rec !=null) {
            rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    audioRecordClass = new AudioRecordClass();

                    if (rec_btn_count == 0){

                        /**Code to handle click of "RECORD" button*/
                        playState = 0;
                        isRecording = true;
                        audioRecordClass.execute();
                        rec_btn_count =1;

                        rec.setText("STOP");
                        rec.setTextColor(Color.parseColor("#ff0000"));
                        play.setEnabled(false);
                        play.setTextColor(Color.parseColor("#808080"));
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();

                        recLogo.setVisibility(View.VISIBLE);

                        timerTV.setText("00:00:00");
                        timerStartObj.start();

                    }

                    else if (rec_btn_count == 1){
                        /**Code to handle click of "Rec-STOP" button*/
                        isRecording = false;
                        play.setTextColor(Color.parseColor("#00ff00"));
                        recLogo.setVisibility(View.INVISIBLE);

                        timerStartObj.cancel();
                        timerStartObj.SS=0L;
                        timerStartObj.MM=0L;
                        timerStartObj.HH=0L;
                        timerStartObj.MS=0L;

                    }
                }
            });
        }/**End of Record Button*/

        /**Start of Play Button*/
        if(play !=null) {
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    audioPlayClass = new AudioPlayClass();

                    if(play_btn_count == 0){


                        //PLAY Buttton
                        playState =1;
                        play_btn_count = 1;
                        Log.d(TAG, "Clicked - play audio");
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        rec.setEnabled(false);

                        isPlaying = true;

                        audioPlayClass.execute();

                        play.setText("Stop");
                        play.setTextColor(Color.parseColor("#ff0000"));
                        rec.setEnabled(false);
                        rec.setTextColor(Color.parseColor("#808080"));
                        Log.d("VIVZ", "Clicked - play audio");
                        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_SHORT).show();
                        timerTV.setText("00:00:00");
                        timerStartObj.start();
                    }

                    else if (play_btn_count == 1){

                        /**Code to pause/stop the playback*/
                        isPlaying = false;

                        Log.d("VIVZ", "Clicked - Stop audio");
                        Log.d("VIVZ", "isPlaying="+isPlaying);
//                        Toast.makeText(getApplicationContext(), "Stopping audio", Toast.LENGTH_SHORT).show();

                        timerStartObj.cancel();
                        timerStartObj.SS=0L;
                        timerStartObj.MM=0L;
                        timerStartObj.HH=0L;
                        timerStartObj.MS=0L;

                    }
                }/**End of Play Button*/
            });
        }
    }/**End of onCreate()*/

    // Start of AudioRecordClass (inner Class)

    public class AudioRecordClass extends AsyncTask<Void,Float,Void>{
        public Boolean recording = true;


        @Override
        protected Void doInBackground(Void... voids) {
            startRecord();
            //publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            //super.onProgressUpdate(values);
            decibel.setText(String.valueOf(values[0]));
        }

        public void startRecord(){

            Log.d(TAG, "Thread - Start record");
//        /**RECORDING PROCESS:
//            1.Create a file to store that data values that comes from the mic.
//            2. Fix the bufferSize and AudioRecord Object.(Will be later in detail later).
//            3.In java the data comes in the form of bytes-bytes-bytes-and so on.
//            4.In the file that we have created we can store the same byte received.
//            5.But as we have to use 16 bit PCM ENCODING SYSTEM(Quantisation), We cannot store the data in Byte form.
//            6.Thus we convert the data in short datatype and then store the array of short into the file.
//            7. short(16 bit) = 2*byte(8-bit)
//            8.And here we have used file to store the audio value from Mic and used the same file to play the Audio.
//            9.We store the data in file as Short-Short-Short(array of short) and fetch the data in same way to fetch.
//            10.But simply saying we do not needed to store and fetch from file for recording and playing for ONCE.
//            11.for that purpose , we can use the array of short datatype
//            12. Another thing is when we try to open the file via a text editor (notepad /notepad++ used by us) we cannot read
//                the actual data(short datatype) that we have store in that file.Because we have stored 16bit-16bit-16bit----
//                and most of the text editor use UTF-8 egit branchncoding which is 32-bit.
//            13.Thus to read the data we have to store it using int datatypte . int-int-int
//            14.And in this case we have to name the extension as (.txt).But when we store and fetch the data ourselves to mic and speaker
//                respectively, the extension does not matter at all . To show that I have created Three File
//                ONE- as extension Sound.pcm
//            15. AND MOST IMPORTANT THING TO REMEMBER :- OUR AMPLITUDE IS REPRESENTED BY 16 bit. SO WE USE SHORT
//         */
            File filePcm = new File(Environment.getExternalStorageDirectory(),"Sound.pcm");

            OutputStream outputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            DataOutputStream dataOutputStream = null;

            AudioRecord audioRecord = null;
            try {
                filePcm.createNewFile();
                // Mechanism to store fetch data from mic and store it.
                outputStream = new FileOutputStream(filePcm);
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                dataOutputStream = new DataOutputStream(bufferedOutputStream);

            /**Call the static class of Audio Record to get the Buffer size in Byte that can handle the Audio data values based on our SAMPLING RATE (44100 hz or frame per second in our case)*/
                int minBufferSizeInBytes = getRecordBufferSize();//WE CAN FIX THE BUFFER SZIE BY OURSELVES

                // The array short that will store the Audio data that we get From the mic.
                short[] audioData = new short[minBufferSizeInBytes];
                float[] audioFloats= new float[audioData.length];

                //Create a Object of the AudioRecord class with the NECESSARY CONFIGURATION
                audioRecord = new AudioRecord(AUDIO_SOURCE,
                        SAMPLE_RATE_IN_HZ,
                        CHANNELS_CONFIGURATION,
                        AUDIO_ENCODING,
                        minBufferSizeInBytes);

//            /** object of the AudioRecord class calls the startRecording() function so that every is ready and the
//             * data can be fetch from mic-buffer-our array of short(audioData)*/

                //setting the size of the audioData array for graph fragment
                graphFragment.setRecordBufferSize(minBufferSizeInBytes);
                audioRecord.startRecording();//Start Recording Based on

                // it means while the user have  not pressed the RECORD-STOP Button
                while(isRecording){


//                /** numberOfShort=minBufferSize/2
//                   Actually what is happening is the minBufferSize(8 bit Buffer) is being converted to numberOfShort(16 bit buffer)
//                   AND THE MOST IMPORTANT PART IS HERE:- The actual value is being store here in the audioData array.
//                 */
                    //Writes short values into short Array and returns numberOfShort
                    int numberOfShort = audioRecord.read(audioData, 0, minBufferSizeInBytes);

                    int[] audioInt = new int[audioData.length];
                    float[] audioFloat = new float[audioData.length];

                    //sending audioData to graph fragment
                    graphFragment.updateRecordGraph(audioData);
                    int recordValueToGraph;

                    float decibel =0;
                    float decibelTotal=0;
                    for(int i = 0; i < numberOfShort; i++){
                        //dataOutputStream.writeShort(audioData[i]); // Store in Sound.haha file as short-short-short--
                        dataOutputStream.writeShort(audioData[i]);
                        if(audioData[i]!=0)
                            decibel = (float) (20*Math.log10(Math.abs((int)audioData[i])/32678.0));
                        decibelTotal = decibel + decibelTotal;
                        recordValueToGraph = (int)audioData[i];//Convert the short to int to store in txt file
                        audioFloats[i] = ((float)Short.reverseBytes(audioData[i])/0x8000);

                    }
                    float decibelAverage = decibelTotal / numberOfShort;
                    publishProgress(decibelAverage);



                    /** FFT calculation part - WAS HERE **/
//                    double[] fft = FftOutput.callMainFft(audioFloat);
//                    float freq = FrequencyValue.getFundamentalFrequency(fft);
//                    System.out.println("Freq "+ freq);

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(decibel!=null)
//                                decibel.setText(Float.toString(averageDecibel));
//                        }
//                    }).start();
//                    if(decibel!=null)
//                        decibel.setText(Float.toString(averageDecibel));

                }


                audioRecord.stop();//Turns off the MIC,ADC and Others
                audioRecord.release();
                audioRecord = null;


            } catch (IOException e){
                e.printStackTrace();
            }finally {
                    if (dataOutputStream!=null){
                        try {
                            dataOutputStream.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(bufferedOutputStream!=null){
                        try {
                            bufferedOutputStream.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (outputStream!=null){
                        try {
                            outputStream.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (audioRecord!=null) {
                        audioRecord.release();
                    }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rec.setText("RECORD");
            rec.setTextColor(Color.parseColor("#ffffff"));
            play.setEnabled(true);
//            Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_SHORT).show();
            rec_btn_count =0;

            Log.d(TAG, "onPost execute stop recording");

        }
    }//End of AudioRecordClass

    //Start of AudioPlayClass
    public class AudioPlayClass extends AsyncTask<Void,Void,Boolean>{

        Boolean sucessfull;
        @Override
        protected Boolean doInBackground(Void... voids) {
            sucessfull = false;
            Log.d(TAG, "doInBackground");
            playRecord();
            Log.d(TAG, "end of doInBackground");

            return sucessfull;
        }

        //Start of playRecord()
        public void playRecord(){

            Log.d(TAG, "playRecord()");
            File filePcm = new File(Environment.getExternalStorageDirectory(), "Sound.pcm");



            int minBufferSize = getPlayBufferSize();

            short[] audioData = new short[minBufferSize/4];

            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            DataInputStream dataInputStream = null;
            AudioTrack audioTrack = null;
            try {
                inputStream = new FileInputStream(filePcm);
                bufferedInputStream = new BufferedInputStream(inputStream);
                dataInputStream = new DataInputStream(bufferedInputStream);
                audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        SAMPLE_RATE_IN_HZ,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        minBufferSize,
                        AudioTrack.MODE_STREAM);
                graphFragment.setPlayBufferSize(audioData.length);
                audioTrack.play();

                while (isPlaying && dataInputStream.available() > 0) {
                    int i = 0;
                    while (dataInputStream.available() > 0 && i < audioData.length) {
                        audioData[i] = dataInputStream.readShort();
                        //playValueToGraph = audioData[i];
                        i++;
                    }
                    audioTrack.write(audioData, 0, audioData.length);
                    graphFragment.updatePlayGraph(audioData);
                }
                audioTrack.pause();
                audioTrack.flush();
                audioTrack.stop();
                audioTrack.release();

                sucessfull=true;

                Log.d(TAG, "end of playrecord()");

            } catch (FileNotFoundException e) {
                e.printStackTrace();/**/
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (dataInputStream!=null){
                    try {
                        dataInputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if(bufferedInputStream!=null){
                    try {
                        bufferedInputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if (inputStream!=null){
                    try {
                        inputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if (audioTrack!=null) {
                    audioTrack.release();
                }
            }
        }/**End of playRecord()*/

        @Override
        protected void onProgressUpdate(Void... values) {

            Log.d(TAG, "onProgressUpdate");

        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            rec.setTextColor(Color.parseColor("#ffffff"));
            play.setText("play");
            play.setTextColor(Color.parseColor("#00b900"));
            rec.setEnabled(true);
            play_btn_count = 0;

           Log.d(TAG, "onPostExecute");

            isPlaying=false;
            timerStartObj.cancel();
            timerStartObj.SS=0L;
            timerStartObj.MM=0L;
            timerStartObj.HH=0L;
            timerStartObj.MS=0L;
        }

    }//End Of AudioPlayClass


    public static int playState(){
        return playState;
    }

    // Returns the minimum buffer size required for the successful creation of an AudioRecord object, in byte units.
    public int getRecordBufferSize(){
        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING);
        return minBufferSize;
    }
    public int getPlayBufferSize(){
        int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING);
        return minBufferSize;
    }
}/**End of MainActivity*/