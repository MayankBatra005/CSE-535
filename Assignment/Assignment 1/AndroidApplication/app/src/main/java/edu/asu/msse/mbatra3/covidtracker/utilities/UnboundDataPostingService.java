package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class UnboundDataPostingService extends Service {
    public UnboundDataPostingService(){}
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("On Start", "Service Started");
//        return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Hey there ",Toast.LENGTH_SHORT).show();
        return Service.START_NOT_STICKY;
    }
//    @Override
//    public void onCreate() {
//        Log.i("On Create", "Service Created");
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.i("On Destroy", "Service Destroyed");
//    }


    /* Driver code : To be written in Driving class

    // Starting the service
    protected void onStart(){
    super.onStart();
    Intent intent = new Intent(this, UnboundMyService.class);
    startService(intent);
    }

    // Stoping the service
    protected void onStop(){
    super.onStop();
    Intent intent = new Intent(this, UnboundMyService.class);
    stopService(intent);
   }

     */
}
