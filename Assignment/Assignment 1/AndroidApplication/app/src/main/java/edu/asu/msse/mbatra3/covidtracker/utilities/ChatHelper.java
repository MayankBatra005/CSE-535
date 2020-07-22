package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatHelper{
//        extends AsyncTask<String, String, String> {
//    @Override
//    protected String doInBackground(String... strings) {
//        Log.i("ASU Impact LAb", strings[0]);
//        return "Done";
//    }

    File root;
    public static ChatHelper object;
    private ChatHelper(){

    }

    public static ChatHelper getInstance(){
        if(object==null){
            object=new ChatHelper();
        }
        return object;
    }

    public void initFile(Context context){
        root = new File(context.getFilesDir().getPath(), "Chat");
        if (!root.exists()) {
            root.mkdirs();
        }

    }

    public File getFile(){
        return root;
    }

    public void generateChatFile(String sFileName, String sBody) {
        try {

            File gpxfile = new File(getFile(), sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            Log.i("Chat Body",sBody);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Log.i( "Saved","Chat Saved" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
