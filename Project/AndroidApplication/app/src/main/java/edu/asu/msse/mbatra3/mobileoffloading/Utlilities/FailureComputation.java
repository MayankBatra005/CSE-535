package edu.asu.msse.mbatra3.mobileoffloading.Utlilities;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import edu.asu.msse.mbatra3.mobileoffloading.MainActivity;

public class FailureComputation {

    /**
     *
     *     Invoked only by periodicMonitoring. If a device seems disconnected due to given threshold of battery level less than 0,
     *     or fails to respond to periodicMonitoring then failureRecovery is initiated. The data assigned to the failed Slave, is
     *     reassigned to the next avaialble slave device. The device may be idle after completing its job or it may be idle since the
     *     beginning. We have not implemented any algorithm to choose which slave to choose in case of failure recovery.
     *     * */
    public static void failureRecovery(String macID, Context context, MainActivity obj) throws InterruptedException {
        Toast.makeText(context, macID + " is at critical battery level!", Toast.LENGTH_SHORT).show();
        HashMap<String, Object> deviceMap = (HashMap<String, Object>) obj.sendReceiveRegister.get(macID);
        Set keySet = obj.sendReceiveRegister.keySet();
        for(int i=0; i<obj.addressMap.size(); i++) {
            if(!keySet.contains(obj.addressMap.get(i))) { // There is some idle Slave, has connected but not been assigned a job or has completed its job
                obj.connectIndex(i);
                obj.sendReceiveRegister.remove(macID);
                obj.sendReceiveRegister.put(obj.addressMap.get(i), deviceMap);
                HashMap<String, String> dataMap = obj.generateMap(i);
                JSONObject jsonObject = new JSONObject(dataMap);
                String jsonString = jsonObject.toString();
//                obj.sendReceive.write(jsonString.getBytes());
                Thread.sleep(50);
            }
        }
    }
}
