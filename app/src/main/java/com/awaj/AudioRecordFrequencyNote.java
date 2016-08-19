package com.awaj;

import android.media.AudioRecord;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by amitgupta on 8/15/2016.
 */
public class AudioRecordFrequencyNote extends AudioRecordMain{

    public  AudioRecordFrequencyNoteListener listener;
    DatabaseHelper databaseHelper;

    public AudioRecordFrequencyNote(int AUDIO_SOURCE, int SAMPLE_RATE_IN_HZ, int CHANNELS_CONFIGURATION, int AUDIO_ENCODING, int NO_OF_SAMPLES,
                             AudioRecordFrequencyNoteListener listener) {
        //super(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNELS_CONFIGURATION, AUDIO_ENCODING, NO_OF_SAMPLES, listener);

        this.listener = listener;

        this.AUDIO_SOURCE = AUDIO_SOURCE;
        this.SAMPLE_RATE_IN_HZ = SAMPLE_RATE_IN_HZ;
        this.CHANNELS_CONFIGURATION = CHANNELS_CONFIGURATION;
        this.AUDIO_ENCODING = AUDIO_ENCODING;
        this.NO_OF_SAMPLES = NO_OF_SAMPLES;

        MIN_BUFFERSIZE_IN_BYTES = NO_OF_SAMPLES *2;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);
        listener.processExecuting(values[0],values[1], values[2], values[3]);
    }

    @Override
    protected void onPostExecute(Void Void) {
//        super.onPostExecute(Void);
        listener.processExecuted();
    }

    @Override
    public void startRecord() {
       // super.startRecord();
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


        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        DataOutputStream dataOutputStream = null;

        AudioRecord audioRecord = null;
        try {


            /**Call the static class of Audio Record to get the Buffer size in Byte that can handle the Audio data values based on our SAMPLING RATE (44100 hz or frame per second in our case)*/
            //int MIN_BUFFERSIZE_IN_BYTES = getRecordBufferSize();//WE CAN FIX THE BUFFER SZIE BY OURSELVES
            // MIN_BUFFERSIZE_IN_BYTES = MainActivity.getMIN_BUFFERSIZE_IN_BYTES();//FIXED THE BUFFER SZIE BY OURSELVES

            // The array short that will store the Audio data that we get From the mic.
            short[] audioData = new short[MIN_BUFFERSIZE_IN_BYTES];


            //Create a Object of the AudioRecord class with the NECESSARY CONFIGURATION
            audioRecord = new AudioRecord(AUDIO_SOURCE,
                    SAMPLE_RATE_IN_HZ,
                    CHANNELS_CONFIGURATION,
                    AUDIO_ENCODING,
                    MIN_BUFFERSIZE_IN_BYTES);

//            /** object of the AudioRecord class calls the startRecording() function so that every is ready and the
//             * data can be fetch from mic-buffer-our array of short(audioData)*/


            audioRecord.startRecording();//Start Recording Based on

            // it means while the user have  not pressed the RECORD-STOP Button
            // Log.d(TAG, "State:"+String.valueOf(stateClass.getRecoderingState()));


            while(stateClass.getRecoderingState()){


//                /** numberOfShort=minBufferSize/2
//                   Actually what is happening is the minBufferSize(8 bit Buffer) is being converted to numberOfShort(16 bit buffer)
//                   AND THE MOST IMPORTANT PART IS HERE:- The actual value is being store here in the audioData array.
//                 */
                //Writes short values into short Array and returns numberOfShort
                int numberOfShort = audioRecord.read(audioData, 0, MIN_BUFFERSIZE_IN_BYTES);
                numberOfShort = MIN_BUFFERSIZE_IN_BYTES/2;
                //int numberOfShort = MIN_BUFFERSIZE_IN_BYTES/2;
                int[] audioDataHalf = new int[audioData.length/2];
                int[] audioInt = new int[audioData.length/2];
                float[] audioFloatsForAmp = new float[audioData.length/2];
                float[] audioFloatsForFFT= new float[audioData.length/2];

                //sending audioData to graph fragment
                //graphFragment.updateRecordGraph(audioFloatsForFFT);
                int recordValueToGraph;


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

                float[] fftOutput = FftOutput.callMainFft(audioFloatsForFFT);

                /**Fundamental Frequency*/

                float frequency = FrequencyValue.getFundamentalFrequency(fftOutput);
                float frequency1 = frequency;
//                MainActivity.plotGraph(audioFloatsForAmp,audioFloatsForFFT);

                databaseHelper = new DatabaseHelper(MyApplication.getAppContext());
                databaseHelper.getAllData();
                int match = databaseHelper.matchFreq(frequency);
                String note = databaseHelper.getNote(match);
                int nearestMatch = databaseHelper.nearestMatch(frequency1);
                String nearestNote = databaseHelper.getNote(nearestMatch);
                float nearestNoteFrequency = databaseHelper.getNoteFrequency(nearestMatch);
//                if(listener!=null)
//                    listener.onDataLoaded(decibelValue,frequency,note);
                publishProgress(String.valueOf(frequency),note, nearestNote, String.valueOf(nearestNoteFrequency));


            }
            Log.d(TAG,"Here");
            audioRecord.stop();
        } finally {
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
