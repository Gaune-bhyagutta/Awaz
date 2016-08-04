package com.awaj;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

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
public class AudioPlayClass extends AsyncTask<Void,Float,Boolean> {

    final String TAG = AudioPlayClass.class.getSimpleName();
    Boolean sucessfull;
    @Override
    protected Boolean doInBackground(Void... voids) {
        sucessfull = false;
        Log.d(TAG, "doInBackground");
        playRecord();
        Log.d(TAG, "end of doInBackground");

        return sucessfull;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
//            super.onProgressUpdate(values);

        MainActivity.updateDecibel(values[0]);
        MainActivity.updateFrequncy(values[1]);
    }

    //Start of playRecord()
    public void playRecord(){

        Log.d(TAG, "playRecord()");
        File filePcm = new File(Environment.getExternalStorageDirectory(), "Sound.pcm");

        //int minBufferSize = getPlayBufferSize();
        int minBufferSize = MainActivity.noOfSamples*2;

        short[] audioData = new short[minBufferSize/4];
        int audioInt[] = new int[minBufferSize/4];
        float audioFloat[] = new float[minBufferSize/4];
        float decibel =0;
        float decibelTotal=0;
        int decibelCount=0;

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

                    MainActivity.SAMPLE_RATE_IN_HZ,

                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize,
                    AudioTrack.MODE_STREAM);
            MainActivity.graphFragment.setPlayBufferSize(audioData.length);
            audioTrack.play();

            float audioFloatsForFFT[] = new float[audioData.length];
            float audioFloatsForAmp[] = new float[audioData.length];
            while (MainActivity.isPlaying && dataInputStream.available() > 0) {
                int i = 0;
                while (dataInputStream.available() > 0 && i < audioData.length) {
                    audioData[i] = dataInputStream.readShort();
                    if(audioData[i]!=0) {
                        decibel = (float) (20 * Math.log10(Math.abs((int) audioData[i]) / 32678.0));
                        decibelCount++;
                    }
                    decibelTotal = decibel + decibelTotal;
                    audioInt[i]=(int)audioData[i];
                    audioFloat[i]=(float) audioInt[i];

                    /**This one is for FFT*/
                    audioFloatsForFFT[i] = (float) audioInt[i];
                    /**This one is for Amplitude Visualization*/
                    audioFloatsForAmp[i]=(float)audioInt[i];
                    i++;
                }
                audioTrack.write(audioData, 0, audioData.length);
                float decibelAverage = decibelTotal / decibelCount;
                float[] fftOutput = FftOutput.callMainFft(audioFloatsForFFT);

                /**Amplitude Mode*/
                if(GraphFragment.GRAPH_INFO_MODE == 0){
                    MainActivity.graphFragment.updatePlayGraph(audioFloatsForAmp);
                }
                /**Frequency Mode*/
                else if(GraphFragment.GRAPH_INFO_MODE == 1){
                    MainActivity.graphFragment.updatePlayGraph(FftOutput.callMainFft(audioFloatsForFFT));
                }
                float frequency = FrequencyValue.getFundamentalFrequency(fftOutput);
                publishProgress(decibelAverage,frequency);
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


}//End Of AudioPlayClass
