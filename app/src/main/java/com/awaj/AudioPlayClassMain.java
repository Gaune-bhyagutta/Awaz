package com.awaj;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by amitgupta on 8/4/2016.
 */
//Start of AudioPlayClassMain
public class AudioPlayClassMain extends AsyncTask<Void,String,Boolean> {

    public static int noOfSamples = getRecordBufferSize();
    private static int MIN_BUFFER_SIZE_BYTES = noOfSamples*2;
    private static final int SAMPLE_RATE_IN_HZ = 22050;
    private static final int CHANNELS_CONFIGURATION = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    StateClass stateClass = StateClass.getState();
    protected AudioPlayMainListener listener;
    final String TAG = AudioPlayClassMain.class.getSimpleName();
    Boolean sucessfull;
    //DatabaseHelper databaseHelper;
    Context context = MyApplication.getAppContext();

    /*@Override
    protected void onPreExecute() {
        databaseHelper = new DatabaseHelper(MyApplication.getAppContext());
        databaseHelper.getAllData();
    }*/

    AudioPlayClassMain(){

    }
    public AudioPlayClassMain(AudioPlayMainListener listener) {
        // set null or default listener or accept as argument to constructor
        this.listener = listener;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        sucessfull = false;
        Log.d(TAG, "doInBackground");
        playRecord();
        Log.d(TAG, "end of doInBackground");

        return sucessfull;
    }

    @Override
    protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);

        //MainActivity.updateDecibel(Float.valueOf(values[0]));
        //MainActivity.updateFrequncy(Float.valueOf(values[1]));
        //MainActivity.updateNotes(values[2]);
        listener.processExecuting();
    }

    //Start of playRecord()
    public void playRecord(){

        Log.d(TAG, "playRecord()");
        File folder = context.getExternalFilesDir("Awaj");
        Log.d("VIVZ", "after folder");
        File latest = getLatestModified();
        Log.d("VIVZ", "after latest");

        File filePcm;
        if (latest==null){
            Toast.makeText(MyApplication.getAppContext(), "Please Record something", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            filePcm = new File(folder,latest.getName());
        }

        //File filePcm = new File(Environment.getExternalStorageDirectory(), "Sound.pcm");

        //int minBufferSize = getPlayBufferSize();
        int minBufferSize = getMinBufferSizeInBytes();

        short[] audioData = new short[minBufferSize/2];
        int audioInt[] = new int[minBufferSize/2];
        float audioFloat[] = new float[minBufferSize/2];


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
                    getSampleRateInHz(),
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();

            //float audioFloatsForFFT[] = new float[audioData.length];
            //float audioFloatsForAmp[] = new float[audioData.length];

//            DecibelCalculation decibelCalculation = new DecibelCalculation();
            while (stateClass.getPlayingState() && dataInputStream.available() > 0) {
                int i = 0;
                while (dataInputStream.available() > 0 && i < audioData.length) {
                    audioData[i] = dataInputStream.readShort();

                    audioInt[i]=(int)audioData[i];
                    audioFloat[i]=(float) audioInt[i];

                    /**This one is for FFT*/
  //                  audioFloatsForFFT[i] = (float) audioInt[i];
                    /**This one is for Amplitude Visualization*/
    //                audioFloatsForAmp[i]=(float)audioInt[i];
                    i++;
                }
                audioTrack.write(audioData, 0, audioData.length);
      //          float decibelValue = decibelCalculation.decibelCalculation(audioData);
        //        float[] fftOutput = FftOutput.callMainFft(audioFloatsForFFT);

          //      float frequency = FrequencyValue.getFundamentalFrequency(fftOutput);
            //    int match = databaseHelper.matchFreq(frequency);

              //  String note = databaseHelper.getNote(match);
                //MainActivity.plotGraph(audioFloatsForAmp,audioFloatsForFFT);

//                if(listener!=null)
//                    listener.onDataLoaded(decibelValue,frequency,note);

//                publishProgress(String.valueOf(decibelValue),String.valueOf(frequency),note);
                //publishProgress(audioData[]);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (audioTrack != null) {
                audioTrack.release();
            }
        }
    }

    /**
     * End of playRecord()
     */

    @Override
    protected void onPostExecute(Boolean aVoid) {

        listener.processExecuted();

        Log.d(TAG, "onPostExecute");


    }

    public File getLatestModified(){
        Log.d(TAG, "iniside getLatestModified");
        File dir = context.getExternalFilesDir("Awaj");
        File[] files = dir.listFiles();
        if (files == null || files.length==0){
            Log.d(TAG, "iniside if");
            return  null;
        }
        File lastModifiedFile = files[0];
        for (int i=1; i<files.length;i++){
            if (lastModifiedFile.lastModified() < files[i].lastModified()){
                lastModifiedFile = files[i];
            }
        }
        Log.d(TAG, "name of file="+lastModifiedFile.getName());
        return lastModifiedFile;
    }
    public static int getRecordBufferSize() {
        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING);
        return minBufferSize;
    }
    public static int getMinBufferSizeInBytes() {
        return MIN_BUFFER_SIZE_BYTES;
    }
    public static int getSampleRateInHz() {
        return SAMPLE_RATE_IN_HZ;
    }


}//End Of AudioPlayClassMain
