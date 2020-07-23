package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class UnboundDataPostingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }


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
