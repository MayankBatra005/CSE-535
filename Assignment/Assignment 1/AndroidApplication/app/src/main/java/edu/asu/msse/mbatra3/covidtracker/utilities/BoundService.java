package edu.asu.msse.mbatra3.covidtracker.utilities;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BoundService extends Service {

    private final IBinder myBinder = new MyLocalBinder();

    public BoundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;
    }

    public String getCurrentText() {
        return ("Eshan = Baap");
    }

    public class MyLocalBinder extends Binder {
        public BoundService getService() {

            return BoundService.this;
        }
    }
}