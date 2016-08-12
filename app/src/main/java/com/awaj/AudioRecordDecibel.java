package com.awaj;

import android.media.AudioRecord;

public class AudioRecordDecibel extends AudioRecordMain{


    AudioRecordDecibelListener listener;

    protected int AUDIO_SOURCE ;
    protected int SAMPLE_RATE_IN_HZ ;
    protected int CHANNELS_CONFIGURATION ;
    protected int AUDIO_ENCODING ;

    protected int NO_OF_SAMPLES;

    protected int MIN_BUFFERSIZE_IN_BYTES;

    AudioRecordDecibel(int AUDIO_SOURCE, int SAMPLE_RATE_IN_HZ, int CHANNELS_CONFIGURATION, int AUDIO_ENCODING, int NO_OF_SAMPLES,
                       AudioRecordDecibelListener listener) {
        //super(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNELS_CONFIGURATION, AUDIO_ENCODING, NO_OF_SAMPLES, (AudioRecordMainListener) listener);

        this.listener = listener;

        this.AUDIO_SOURCE = AUDIO_SOURCE;
        this.SAMPLE_RATE_IN_HZ = SAMPLE_RATE_IN_HZ;
        this.CHANNELS_CONFIGURATION = CHANNELS_CONFIGURATION;
        this.AUDIO_ENCODING = AUDIO_ENCODING;
        this.NO_OF_SAMPLES = NO_OF_SAMPLES ;

        MIN_BUFFERSIZE_IN_BYTES = NO_OF_SAMPLES * 2;
    }

    @Override
    protected void onProgressUpdate(String... values) {
       // super.onProgressUpdate(values);
        listener.processExecuting(values[0]);
    }

    public void startRecord() {
       // super.startRecord();
        audioData = new short[MIN_BUFFERSIZE_IN_BYTES];

        audioRecord = new AudioRecord(AUDIO_SOURCE,
                SAMPLE_RATE_IN_HZ,
                CHANNELS_CONFIGURATION,
                AUDIO_ENCODING,
                MIN_BUFFERSIZE_IN_BYTES);

        audioRecord.startRecording();//Start Recording Based on
        String decibel ="0";
        while(stateClass.getRecoderingState()){

            numberOfShort = audioRecord.read(audioData, 0, MIN_BUFFERSIZE_IN_BYTES);
            numberOfShort = MIN_BUFFERSIZE_IN_BYTES/2;

            int[] audioDataHalf = new int[audioData.length/2];
            int[] audioInt = new int[audioData.length/2];
            float[] audioFloatsForAmp = new float[audioData.length/2];
            float[] audioFloatsForFFT= new float[audioData.length/2];

            DecibelCalculation decibelCalculation = new DecibelCalculation();
            for(int i = 0; i < numberOfShort; i++){
                //dataOutputStream.writeShort(audioData[i]); // Store in Sound.haha file as short-short-short--

                audioDataHalf[i] = audioData[i];
                audioInt[i]=(int)audioData[i];
                /**This one is for FFT*/
                audioFloatsForFFT[i] = (float) audioInt[i];
                /**This one is for Amplitude Visualization*/
                audioFloatsForAmp[i]=(float)audioInt[i];
            }

            float decibelValue = decibelCalculation.decibelCalculation(audioData);
            publishProgress(String.valueOf(decibelValue));
        }

    }
}
