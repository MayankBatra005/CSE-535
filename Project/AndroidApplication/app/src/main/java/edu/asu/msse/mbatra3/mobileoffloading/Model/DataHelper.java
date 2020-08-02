package edu.asu.msse.mbatra3.mobileoffloading.Model;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataHelper {
    public static void createNotes(String content, Context context) {
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, "GPSData.txt");
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }
}
