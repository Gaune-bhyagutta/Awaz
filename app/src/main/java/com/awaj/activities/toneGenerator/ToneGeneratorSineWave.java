package com.awaj.activities.toneGenerator;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by amitgupta on 8/10/2016.
 */
public class ToneGeneratorSineWave extends ToneGenerator {

    File file = new File(Environment.getExternalStorageDirectory(), "SineWave.pcm");

    ToneGeneratorSineWave(int freq) {
        try {
            file.createNewFile();
            // Mechanism to store fetch data from mic and store it.
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            dataOutputStream = new DataOutputStream(bufferedOutputStream);

            for (double theta = 0; theta < 1000000; theta = theta + freq) {
                dataOutputStream.writeShort((int) (5000 * Math.cos(theta)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

