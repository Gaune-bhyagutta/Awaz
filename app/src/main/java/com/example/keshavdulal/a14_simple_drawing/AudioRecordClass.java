package com.example.keshavdulal.a14_simple_drawing;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by amitgupta on 7/6/2016.
 */
public class AudioRecordClass {

    public  static int temp;
    public static Boolean recording = true;

    public static void startRecord(){
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
            //GraphFragment gF = new GraphFragment();
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

                    temp = (int)audioData[i];//Convert the short to int to store in txt file
                    //GraphFragment.graph_height=temp;
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

    public  static  void stopRecord(){
        recording = false;
    }
}
