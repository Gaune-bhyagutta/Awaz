package com.awaj;

import android.content.Context;
import android.media.AudioRecord;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by amitgupta on 8/12/2016.
 */
public class AudioRecordFile extends AudioRecordMain {

    Context context = MyApplication.getAppContext();
    File folder;
    File filePcm;

    AudioRecordFileListener listener;

    OutputStream outputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    DataOutputStream dataOutputStream = null;

    AudioRecordFile(){}

    public AudioRecordFile(int AUDIO_SOURCE, int SAMPLE_RATE_IN_HZ, int CHANNELS_CONFIGURATION,
                    int AUDIO_ENCODING, int NO_OF_SAMPLES,
                    AudioRecordFileListener listener) {
    /*    super(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNELS_CONFIGURATION, AUDIO_ENCODING, NO_OF_SAMPLES,
                (AudioRecordMainListener) listener);*/
        this.listener = listener;

        this.AUDIO_SOURCE = AUDIO_SOURCE;
        this.SAMPLE_RATE_IN_HZ = SAMPLE_RATE_IN_HZ;
        this.CHANNELS_CONFIGURATION = CHANNELS_CONFIGURATION;
        this.AUDIO_ENCODING = AUDIO_ENCODING;
        this.NO_OF_SAMPLES = NO_OF_SAMPLES ;

        MIN_BUFFERSIZE_IN_BYTES = NO_OF_SAMPLES * 2;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.processExecuting();
    }

    @Override
    public void startRecord() {
        //super.startRecord();
        audioData = new short[MIN_BUFFERSIZE_IN_BYTES];

        audioRecord = new AudioRecord(AUDIO_SOURCE,
                SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING,
                MIN_BUFFERSIZE_IN_BYTES);

        audioRecord.startRecording();//Start Recording Based on

        folder = context.getExternalFilesDir("Awaj");
        filePcm = new File(folder,"Sound"+System.currentTimeMillis()+".pcm");
        try {
            outputStream = new FileOutputStream(filePcm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bufferedOutputStream = new BufferedOutputStream(outputStream);
        dataOutputStream = new DataOutputStream(bufferedOutputStream);

        while(stateClass.getRecoderingState()){

            numberOfShort = audioRecord.read(audioData, 0, MIN_BUFFERSIZE_IN_BYTES);
            numberOfShort = MIN_BUFFERSIZE_IN_BYTES/2;

            for(int i = 0; i < numberOfShort; i++){
                try {
                    dataOutputStream.writeShort(audioData[i]); // Store in Sound.haha file as short-short-short--
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (audioRecord != null) {
                        audioRecord.release();
                    }
                }
            }

        }

    }
}
