package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatHelper extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        Log.i("ASU Impact LAb", strings[0]);
        return "Done";
    }

    public void generateChatFile(Context context, String sFileName, String sBody) {
        try {
            File root = new File(context.getFilesDir().getPath(), "Chat");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            Log.i("Chat Body",sBody);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
