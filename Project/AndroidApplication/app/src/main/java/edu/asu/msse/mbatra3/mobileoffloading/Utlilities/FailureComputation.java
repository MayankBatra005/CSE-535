package edu.asu.msse.mbatra3.mobileoffloading.Utlilities;

import android.content.Context;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Set;
import edu.asu.msse.mbatra3.mobileoffloading.MainActivity;

public class FailureComputation {
    public static void failureRecovery(String macID, Context context, MainActivity obj)
            throws InterruptedException {
        Toast.makeText(context, macID + " is at critical battery level!",
                Toast.LENGTH_SHORT).show();
        HashMap<String, Object> deviceMap = (HashMap<String, Object>)
                obj.sndRcvReg.get(macID);
        Set keySet = obj.sndRcvReg.keySet();
        for(int i=0; i<obj.addressMap.size(); i++) {
            if(!keySet.contains(obj.addressMap.get(i))) {
                obj.connectIndex(i);
                obj.sndRcvReg.remove(macID);
                obj.sndRcvReg.put(obj.addressMap.get(i), deviceMap);
                HashMap<String, String> dataMap = obj.generateMap(i);
                JSONObject jsonObject = new JSONObject(dataMap);
                String jsonString = jsonObject.toString();
                Thread.sleep(50);
            }
        }
    }
}
