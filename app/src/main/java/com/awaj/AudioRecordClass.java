package com.awaj;

import android.graphics.Color;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by amitgupta on 8/4/2016.
 */

// Start of AudioRecordClass (inner Class)

public class AudioRecordClass extends AsyncTask<Void,Float,Void> {

    public Boolean recording = true;
    final String TAG = AudioRecordClass.class.getSimpleName();


    @Override
    protected Void doInBackground(Void... voids) {
        startRecord();
        //publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Float... values ) {
        //super.onProgressUpdate(values);
        MainActivity.updateDecibel(values[0]);
        MainActivity.updateFrequncy(values[1]);
        //notesTV.setText(String.valueOf(values[2]));
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
            //int minBufferSizeInBytes = getRecordBufferSize();//WE CAN FIX THE BUFFER SZIE BY OURSELVES
            int minBufferSizeInBytes = MainActivity.noOfSamples*2;//FIXED THE BUFFER SZIE BY OURSELVES

            // The array short that will store the Audio data that we get From the mic.
            short[] audioData = new short[minBufferSizeInBytes];
            float[] audioFloatsForFFT= new float[audioData.length];

            //Create a Object of the AudioRecord class with the NECESSARY CONFIGURATION
            audioRecord = new AudioRecord(MainActivity.AUDIO_SOURCE,
                    MainActivity.SAMPLE_RATE_IN_HZ,
                    MainActivity.CHANNELS_CONFIGURATION,
                    MainActivity.AUDIO_ENCODING,
                    minBufferSizeInBytes);

//            /** object of the AudioRecord class calls the startRecording() function so that every is ready and the
//             * data can be fetch from mic-buffer-our array of short(audioData)*/

            //setting the size of the audioData array for graph fragment
            MainActivity.graphFragment.setRecordBufferSize(minBufferSizeInBytes);
            audioRecord.startRecording();//Start Recording Based on

            // it means while the user have  not pressed the RECORD-STOP Button
            while(MainActivity.isRecording){


//                /** numberOfShort=minBufferSize/2
//                   Actually what is happening is the minBufferSize(8 bit Buffer) is being converted to numberOfShort(16 bit buffer)
//                   AND THE MOST IMPORTANT PART IS HERE:- The actual value is being store here in the audioData array.
//                 */
                //Writes short values into short Array and returns numberOfShort
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSizeInBytes);

                int[] audioInt = new int[audioData.length];
                float[] audioFloatsForAmp = new float[audioData.length];

                //sending audioData to graph fragment
                //graphFragment.updateRecordGraph(audioFloatsForFFT);
                int recordValueToGraph;

                float decibel =0;
                float decibelTotal=0;
                int decibelCount=0;
                for(int i = 0; i < numberOfShort; i++){
                    //dataOutputStream.writeShort(audioData[i]); // Store in Sound.haha file as short-short-short--

                    dataOutputStream.writeShort(audioData[i]);
                    if(audioData[i]!=0) {
                        decibel = (float) (20 * Math.log10(Math.abs((int) audioData[i]) / 32678.0));
                        decibelCount++;
                    }
                    decibelTotal = decibel + decibelTotal;
                    //recordValueToGraph = (int)audioData[i];//Convert the short to int to store in txt file
                    audioInt[i]=(int)audioData[i];
                    /**This one is for FFT*/
                    audioFloatsForFFT[i] = (float) audioInt[i];
                    /**This one is for Amplitude Visualization*/
                    audioFloatsForAmp[i]=(float)audioInt[i];


                }
                float decibelAverage = decibelTotal / decibelCount;

                float[] fftOutput = FftOutput.callMainFft(audioFloatsForFFT);

                if (GraphFragment.GRAPH_INFO_MODE == 0) {
                    /**Amplitude Mode*/
                    MainActivity.graphFragment.updateRecordGraph(audioFloatsForAmp);
                } else if (GraphFragment.GRAPH_INFO_MODE == 1) {
                    /**Frequency Mode*/
                    MainActivity.graphFragment.updateRecordGraph(fftOutput);
                }
                /**Fundamental Frequency*/

                float frequency = FrequencyValue.getFundamentalFrequency(fftOutput);
                publishProgress(decibelAverage,frequency);

            }
            audioRecord.stop();

        } catch (IOException e) {

            e.printStackTrace();
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

    @Override
    protected void onPostExecute(Void Void) {

        MainActivity.updateRecordState();
        Log.d(TAG, "onPost execute stop recording");

    }
}//End of AudioRecordClass