package com.awaj;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;
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
//Start of AudioPlayClass
public class AudioPlayClass extends AsyncTask<Void,String,Boolean> {

    final String TAG = AudioPlayClass.class.getSimpleName();
    Boolean sucessfull;
    DatabaseHelper databaseHelper;
    Context context = MyApplication.getAppContext();

    @Override
    protected void onPreExecute() {
        databaseHelper = new DatabaseHelper(MyApplication.getAppContext());
        databaseHelper.getAllData();
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

        MainActivity.updateDecibel(Float.valueOf(values[0]));
        MainActivity.updateFrequncy(Float.valueOf(values[1]));
        MainActivity.updateNotes(values[2]);
    }

    //Start of playRecord()
    public void playRecord(){

        Log.d(TAG, "playRecord()");
        File folder = context.getExternalFilesDir("Awaj");
        File latest = getLatestModified();
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
        int minBufferSize = MainActivity.getMinBufferSizeInBytes();

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
                    MainActivity.getSampleRateInHz(),
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();

            float audioFloatsForFFT[] = new float[audioData.length];
            float audioFloatsForAmp[] = new float[audioData.length];

            DecibelCalculation decibelCalculation = new DecibelCalculation();
            while (MainActivity.getValueOfisPlaying() && dataInputStream.available() > 0) {
                int i = 0;
                while (dataInputStream.available() > 0 && i < audioData.length) {
                    audioData[i] = dataInputStream.readShort();

                    audioInt[i]=(int)audioData[i];
                    audioFloat[i]=(float) audioInt[i];

                    /**This one is for FFT*/
                    audioFloatsForFFT[i] = (float) audioInt[i];
                    /**This one is for Amplitude Visualization*/
                    audioFloatsForAmp[i]=(float)audioInt[i];
                    i++;
                }
                audioTrack.write(audioData, 0, audioData.length);
                float decibelValue = decibelCalculation.decibelCalculation(audioData);
                float[] fftOutput = FftOutput.callMainFft(audioFloatsForFFT);

                float frequency = FrequencyValue.getFundamentalFrequency(fftOutput);
                int match = databaseHelper.matchFreq(frequency);

                String note = databaseHelper.getNote(match);
                MainActivity.plotGraph(audioFloatsForAmp,audioFloatsForFFT);
                publishProgress(String.valueOf(decibelValue),String.valueOf(frequency),note);
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

        MainActivity.updatePlayState();

        Log.d(TAG, "onPostExecute");


    }

    private File getLatestModified(){
        File dir = context.getExternalFilesDir("Awaj");
        File[] files = dir.listFiles();
        if (files == null || files.length==0){
            return  null;
        }
        File lastModifiedFile = files[0];
        for (int i=1; i<files.length;i++){
            if (lastModifiedFile.lastModified() < files[i].lastModified()){
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }


}//End Of AudioPlayClass
