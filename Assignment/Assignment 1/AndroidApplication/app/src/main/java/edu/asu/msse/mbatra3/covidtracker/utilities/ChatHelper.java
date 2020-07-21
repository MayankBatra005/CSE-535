package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.os.AsyncTask;
import android.util.Log;

public class ChatHelper extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        Log.i("ASU Impact LAb", strings[0]);
        return "Done";
    }
}
