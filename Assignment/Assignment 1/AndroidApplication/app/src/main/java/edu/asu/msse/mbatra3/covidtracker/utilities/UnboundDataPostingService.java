package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import edu.asu.msse.mbatra3.covidtracker.Model.Data;

public class UnboundDataPostingService extends Service {
    LocationManager locationManager;
    private static final int GPS_TIME_INTERVAL = 900000;    // 15 minutes in milli seconds
    private static final int GPS_DISTANCE= 0;
    LocationListener locationListener;
    public UnboundDataPostingService(){}
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("On Start", "Service Started");
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method

            }
        }, 0, 1000);
        return Service.START_NOT_STICKY;
    }

     /* Timer code
    new Timer().scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        //your method
    }
}, 0, 1000);

     */
    @Override
    public void onCreate() {
        Log.i("On Create", "Service Created");
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
         Log.i("Location", location.toString());
         String[] coordinates=extractRawCoordinates(location);
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         try {
            if(Data.getInstance().insertData(""+coordinates[0],""+coordinates[1],
                  ""+timestamp)){
                  Log.i("Insertion","Success");}
            } catch (Exception e) {
                  e.printStackTrace();
            }
         boolean status = false;
           try {
           status = Data.getInstance().insertData(""+coordinates[0],""+coordinates[1],
                         ""+timestamp);
              } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("Status of Datbase",""+status);
                ArrayList<String> result=Data.getInstance().fetchData2();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.
                    GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, locationListener);

          /* Timer code  Just uncomment with time lines
    new Timer().scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        //your method
    }
    }, 0, 1000);

     */

        UploadDBTask task=new UploadDBTask(this);
        task.execute();
    }

    public String[] extractRawCoordinates(Location location){
        String[] coordinates=new String[2];
        coordinates[0]=""+location.getLongitude();
        coordinates[1]=""+location.getLatitude();
        Log.i("xcoordinate: ",coordinates[0]);
        Log.i("ycoordinate: ",coordinates[1]);
        return coordinates;
    }
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
