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
                == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    // Test Code from
    // https://stackoverflow.com/questions/16055447/how-to-get-gps-location-once-for-5-mins-android/16055734#16055734


}

