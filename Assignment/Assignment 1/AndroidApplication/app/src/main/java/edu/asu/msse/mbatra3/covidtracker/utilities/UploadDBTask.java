package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import edu.asu.msse.mbatra3.covidtracker.Model.Data;
import edu.asu.msse.mbatra3.covidtracker.R;

public class UploadDBTask extends AsyncTask<String, String, String> {


    Context context;
    public  UploadDBTask(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            String url = "http://10.218.107.121/cse535/upload_video.php";
            String charset = "UTF-8";
            String group_id = "4";
            String ASUid = "1216214610";
            String accept = "1";


//            File videoFile = new File(Environment.getExternalStorageDirectory()+"/my_folder/Action1.mp4");

            // Create function  in chat helper class and it will return the file object over here
            File videoFile = new File(context.getFilesDir().getPath()+ Data.getInstance()
                    .getDbName()+".db");
            String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            String CRLF = "\r\n"; // Line separator required by multipart/form-data.

            URLConnection connection;

            connection = new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            ) {
                // Send normal accept.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"accept\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(accept).append(CRLF).flush();

                // Send normal accept.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"id\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(ASUid).append(CRLF).flush();

                // Send normal accept.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"group_id\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                writer.append(CRLF).append(group_id).append(CRLF).flush();


                // Send video file.
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + videoFile.getName() + "\"").append(CRLF);
//                writer.append("Content-Type: video/mp4; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!

                // changing for sqlite DB
                writer.append("Content-Type: multipart/form-data; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
                writer.append(CRLF).flush();
                FileInputStream vf = new FileInputStream(videoFile);
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = vf.read(buffer, 0, buffer.length)) >= 0)
                    {
                        output.write(buffer, 0, bytesRead);

                    }
                    //   output.close();
                    //Toast.makeText(getApplicationContext(),"Read Done", Toast.LENGTH_LONG).show();
                }catch (Exception exception)
                {


                    //Toast.makeText(getApplicationContext(),"output exception in catch....."+ exception + "", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(exception));
                    publishProgress(String.valueOf(exception));
                    // output.close();

                }

                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


                // End of multipart/form-data.
                writer.append("--" + boundary + "--").append(CRLF).flush();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Request is lazily fired whenever you need to obtain information about response.
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            Log.i("Response code for DB file",""+responseCode); // Should be 200

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
