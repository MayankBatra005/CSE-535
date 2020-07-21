package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class DownloadTask extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
//        Toast.makeText(getApplicationContext(), "Starting to execute Background Task", Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... text) {

        File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
        File directory = new File(SDCardRoot, "/my_folder/"); //create directory to keep your downloaded file
        if (!directory.exists())
        {
            directory.mkdir();
        }
        //publishProgress();
//            Toast.makeText(getApplicationContext(),"In Background Task", Toast.LENGTH_LONG).show();
        String fileName = "Action1" + ".mp4"; //song name that will be stored in your device in case of song
        //String fileName = "myImage" + ".jpeg"; in case of image
        try
        {
            InputStream input = null;
            try{

                URL url = new URL("https://www.signingsavvy.com/media/mp4-ld/7/7231.mp4"); // link of the song which you want to download like (http://...)
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(95 * 1000);
                urlConnection.setConnectTimeout(95 * 1000);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("X-Environment", "android");


                urlConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        /** if it necessarry get url verfication */
                        //return HttpsURLConnection.getDefaultHostnameVerifier().verify("your_domain.com", session);
                        return true;
                    }
                });
                urlConnection.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());


                urlConnection.connect();
                input = urlConnection.getInputStream();
                //input = url.openStream();
                OutputStream output = new FileOutputStream(new File(directory, fileName));

                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0)
                    {
                        output.write(buffer, 0, bytesRead);

                    }
                    output.close();
                    //Toast.makeText(getApplicationContext(),"Read Done", Toast.LENGTH_LONG).show();
                }
                catch (Exception exception)
                {


                    //Toast.makeText(getApplicationContext(),"output exception in catch....."+ exception + "", Toast.LENGTH_LONG).show();
                    Log.d("Error", String.valueOf(exception));
                    publishProgress(String.valueOf(exception));
                    output.close();

                }
            }
            catch (Exception exception)
            {

                //Toast.makeText(getApplicationContext(), "input exception in catch....."+ exception + "", Toast.LENGTH_LONG).show();
                publishProgress(String.valueOf(exception));

            }
            finally
            {
                input.close();
            }
        }
        catch (Exception exception)
        {
            publishProgress(String.valueOf(exception));
        }

        return "true";
    }




    @Override
    protected void onProgressUpdate(String... text) {
       // Toast.makeText(getApplicationContext(), "In Background Task" + text[0], Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(String text){
//        VideoView vv = (VideoView) findViewById(R.id.videoView);
//        vv.setVideoPath(Environment.getExternalStorageDirectory()+"/my_folder/Action1.mp4");
//        vv.start();
//        Button bt4 = (Button)findViewById(R.id.button3);
//        bt4.setEnabled(true);
    }



    public void startRecording()
    {
        File mediaFile = new
                File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myvideo.mp4");




        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
        Uri fileUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    private boolean hasCamera() {
//        if (getPackageManager().hasSystemFeature(
//                PackageManager.FEATURE_CAMERA_ANY)){
//            return true;
//        } else {
            return false;
//        }
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

//        if (requestCode == VIDEO_CAPTURE) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "Video has been saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Video recording cancelled.",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Failed to record video",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
    }
}


