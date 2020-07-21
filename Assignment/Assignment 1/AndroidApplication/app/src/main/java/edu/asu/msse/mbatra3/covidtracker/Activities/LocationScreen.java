package edu.asu.msse.mbatra3.covidtracker.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Timestamp;
import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import edu.asu.msse.mbatra3.covidtracker.Model.Data;
import edu.asu.msse.mbatra3.covidtracker.R;

public class LocationScreen extends AppCompatActivity {
    LocationManager locationManager;
    private static final int GPS_TIME_INTERVAL = 300000;    // 5 minutes in milli seconds
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
               test(location);
                Log.i("Location", location.toString());
                String[] coordinates=extractRawCoordinates(location);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if(Data.getInstance().insertData(""+coordinates[0],""+coordinates[1],
                        ""+timestamp)){
                    Log.i("Insertion","Success");}

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



        // If device is running SDK < 23

        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.
                        ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        0, locationListener);

            }

        }

    }
    public void test(Location location){
//        Toast.makeText(this,location.toString(),Toast.LENGTH_SHORT).show();
    }

    public String[] extractRawCoordinates(Location location){
        String[] coordinates=new String[2];
        TextView textView3=findViewById(R.id.textView3);
        textView3.setText(location.toString());
        coordinates[0]=""+location.getLongitude();
        coordinates[1]=""+location.getLatitude();
        Toast.makeText(this,"xcordinate: "+ coordinates[0]+" & "+"ycoordinate: "+
                coordinates[1],Toast.LENGTH_SHORT).show();
        return coordinates;
    }

}
