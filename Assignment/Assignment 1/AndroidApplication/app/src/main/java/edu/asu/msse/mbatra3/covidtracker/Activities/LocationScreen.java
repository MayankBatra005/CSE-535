package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.sql.Timestamp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import edu.asu.msse.mbatra3.covidtracker.Model.Data;
import edu.asu.msse.mbatra3.covidtracker.R;
import edu.asu.msse.mbatra3.covidtracker.utilities.UnboundDataPostingService;

public class LocationScreen extends AppCompatActivity {
    LocationManager locationManager;
    private static final int GPS_TIME_INTERVAL = 900000;    //Change 15 minutes in milli seconds
    private static final int GPS_DISTANCE= 0;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission
                    .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.
                        GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, locationListener);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_screen);

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

        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.
                        ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        0, locationListener);

            }

        }

    }


    public String[] extractRawCoordinates(Location location){
            String[] coordinates=new String[2];
            TextView textView3=findViewById(R.id.textView3);
            textView3.setText(location.toString());
            coordinates[0]=""+location.getLongitude();
            coordinates[1]=""+location.getLatitude();
            Log.i("xcoordinate: ",coordinates[0]);
            Log.i("ycoordinate: ",coordinates[1]);
            return coordinates;
    }

    public void startService(View view){
        Intent service=new Intent(this, UnboundDataPostingService.class);
        startService(service);
    }

}
