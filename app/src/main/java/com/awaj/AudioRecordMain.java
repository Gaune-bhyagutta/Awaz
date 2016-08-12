package com.awaj;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Created by amitgupta on 8/12/2016.
 */
public class AudioRecordMain extends AsyncTask<Void,String ,Void>{

    final String TAG = AudioRecordMain.class.getSimpleName();

    StateClass stateClass = StateClass.getState();

    protected int AUDIO_SOURCE ;
    protected int SAMPLE_RATE_IN_HZ ;
    protected int CHANNELS_CONFIGURATION ;
    protected int AUDIO_ENCODING ;

    protected int NO_OF_SAMPLES;

    protected int MIN_BUFFERSIZE_IN_BYTES = NO_OF_SAMPLES * 2;

    AudioRecord audioRecord = null;

    short[] audioData;

    int numberOfShort;

    AudioRecordMainListener listener;

    AudioRecordMain(){

    }
    AudioRecordMain(int AUDIO_SOURCE,int SAMPLE_RATE_IN_HZ,int CHANNELS_CONFIGURATION,int AUDIO_ENCODING,
                    int NO_OF_SAMPLES,AudioRecordMainListener listener){

        this.listener = listener;

        this.AUDIO_SOURCE = AUDIO_SOURCE;
        this.SAMPLE_RATE_IN_HZ = SAMPLE_RATE_IN_HZ;
        this.CHANNELS_CONFIGURATION = CHANNELS_CONFIGURATION;
        this.AUDIO_ENCODING = AUDIO_ENCODING;
        this.NO_OF_SAMPLES = NO_OF_SAMPLES;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        startRecord();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.processExecuted();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        listener.processExecuting();
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
        audioData = new short[MIN_BUFFERSIZE_IN_BYTES];

        audioRecord = new AudioRecord(AUDIO_SOURCE,
                SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING,
                MIN_BUFFERSIZE_IN_BYTES);

        audioRecord.startRecording();//Start Recording Based on

        while(stateClass.getRecoderingState()){

            numberOfShort = audioRecord.read(audioData, 0, MIN_BUFFERSIZE_IN_BYTES);
            numberOfShort = MIN_BUFFERSIZE_IN_BYTES/2;

        }

    }


}
