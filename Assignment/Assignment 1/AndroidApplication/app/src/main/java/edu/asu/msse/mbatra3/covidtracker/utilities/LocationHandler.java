package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationHandler {

    public void location(Context context)
    {
        LocationManager locationManager= (LocationManager) context.getSystemService
                (Context.LOCATION_SERVICE);

        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            Log.i("Location",location.toString());
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
    }

    public void permissionRequest(Activity context){
        // IF WE DONT HAVE PERMISSION
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    // Test Code from
    // https://stackoverflow.com/questions/16055447/how-to-get-gps-location-once-for-5-mins-android/16055734#16055734


    Location gpslocation = null;
    LocationManager locMan;

    private static final int GPS_TIME_INTERVAL = 60000; // get gps location every 1 min
    private static final int GPS_DISTANCE= 1000; // set the distance value in meter

/*
   for frequently getting current position then above object value set to 0 for both you will get continues location but it drown the battery
*/

    private void obtainLocation(Context context){
        if(locMan==null)
            locMan = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            gpslocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if(isLocationListener){
//                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        GPS_TIME_INTERVAL, GPS_DISTANCE, GPSListener);
//            }
        }
    }

    private static final int HANDLER_DELAY = 1000*60*5;

    Handler handler = new Handler();
//    handler.postDelayed(new Runnable() {
//
//        public void run() {
//            myLocation = obtainLocation();
//            handler.postDelayed(this, HANDLER_DELAY);
//        }
//    }, START_HANDLER_DELAY);

    private LocationListener GPSListener = new LocationListener(){
        public void onLocationChanged(Location location) {
            // update location
            locMan.removeUpdates(GPSListener); // remove this listener
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
}

