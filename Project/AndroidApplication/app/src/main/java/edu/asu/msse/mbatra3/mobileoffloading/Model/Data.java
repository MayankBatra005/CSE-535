package edu.asu.msse.mbatra3.mobileoffloading.Model;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class Data {
    public static Data object;
    private Data(){}
    public HashMap<String, String> getSlaveInformationMap() {
        return slaveInformationMap;
    }
    public void setSlaveInformationMap(HashMap<String, String> slaveInformationMap) {
        this.slaveInformationMap = slaveInformationMap;
    }
    public HashMap<String, String> slaveInformationMap = new HashMap<>();
    public static Data getInstance(){
        if (object == null)
        {
            object=new Data();
        }
        return object;
    }
    public void viewMapContents(){
        Map<String,String> iter=slaveInformationMap;
        for (Map.Entry<String, String> entry : iter.entrySet()) {
            Log.i("Map values: ",(entry.getKey() + " = " + entry.getValue()));
        }
    }
}

