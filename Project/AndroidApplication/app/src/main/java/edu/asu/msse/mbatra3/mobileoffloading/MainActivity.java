package edu.asu.msse.mbatra3.mobileoffloading;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import edu.asu.msse.mbatra3.mobileoffloading.Model.Data;
import edu.asu.msse.mbatra3.mobileoffloading.Utlilities.FailureComputation;
import edu.asu.msse.mbatra3.mobileoffloading.Utlilities.Helper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_TEXT = "com.example.mobileoffloading.EXTRA_TEXT";
    WifiManager wifiManager;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    Button discoveryButton;
    ListView lv;
    TextView conxnStatus;
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    ArrayList<Parcelable> dummy = new ArrayList<>();
    String[] devicesNam;
    WifiP2pDevice[] p2pDevices;
    TextView battery, msgBox;
    Button locationButton, btBattery, sendButton, computeButton, disconnectionButton;
    TextView tv_1, tv_2, tv_3, tv_4, tv_5;
    FusedLocationProviderClient locationClient;
    EditText mtrx_1, mtrx_2;
    public long globlInit;
    public ArrayList<String> addressMap = new ArrayList<String>();
    public HashMap<String, Object> sndRcvReg = new HashMap<>();
    public HashMap<String, String> batteryLevels = new HashMap<>();
    public String OWNER = "SLAVE";
    public int[][] A_1 = new int[4][4];
    public int[][] A_2 = new int[4][4];
    public int[][] Out = new int[4][4];
    public static int counter = 0;
    String data = null;
    static final int MESSAGE_READ = 1;
    String uniqueID = UUID.randomUUID().toString();
    ServerCls serverCls;
    ClientClass clientClass;
    ReceiveAndSend receiveAndSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgBox = findViewById(R.id.readMsg);
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        globlInit -=800;
        init();
        callListener();
    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    if (tempMsg.contains("\"time\"") && tempMsg.contains("\"i\"")) {
                        try {
                            slaveComputation(tempMsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (tempMsg.contains("offloadDone")) {
                        try {
                            uponReceiving(tempMsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (tempMsg.contains("periodicResponse")) {
                        sendPeriodicResponse();
                    } else {
                        try {
                            Log.i("Temp Message",tempMsg);
                            initialConnect(tempMsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            return true;
        }
    });
    BroadcastReceiver bttryInformation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Data.getInstance().getSlaveInformationMap().put("battery", batteryLevel + "");
        }
    };
    private void getLocation() {
        locationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(MainActivity.this,
                                        Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.
                                        getLatitude(), location.getLongitude(), 1);
                                data = (new StringBuilder()).append(addresses.get(0).getLatitude()).
                                        append("\n").append(addresses.get(0).getLongitude()).append
                                        ("\n").toString();
                                Data.getInstance().getSlaveInformationMap().put("lattitude",
                                        addresses.get(0).getLatitude() + "");
                                Data.getInstance().getSlaveInformationMap().put("longitude",
                                        addresses.get(0).getLongitude() + "");
                                Data.getInstance().viewMapContents();
                                createNotes(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    public void disconnectWifiPeers(int position) {
        final WifiP2pDevice device = p2pDevices[findPos(addressMap.get(position))];
        System.out.println("Device Name is - " + device.deviceName);
        if (device.status == WifiP2pDevice.CONNECTED) {
            manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), device.deviceName +
                            " disconnected ", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int reason) {
                    Toast.makeText(getApplicationContext(), device.deviceName +
                            " not disconnected ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void createNotes(String content) {
        try {
            File path = getExternalFilesDir(null);
            File file = new File(path, "Location.txt");
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }
    private void callListener() {

        discoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("Discovery Started","XY Found");
                        conxnStatus.setText("Discovery Started");
                    }
                    @Override
                    public void onFailure(int reason) {
                        Log.i("Discovery Started","Peer not Found");
                        conxnStatus.setText("Discovery Failed");
                    }
                });
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connxn(position);
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
                Log.i("Get info button clicked","Getting Battery information in b" +
                        "tLocation");
                Data.getInstance().viewMapContents();
                MainActivity.this.registerReceiver(MainActivity.this.bttryInformation,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }

        });
        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper help=new Helper();
                A_1 = help.convertStringToArray(String.valueOf(mtrx_1.getText()));
                A_2 = help.convertStringToArray(String.valueOf(mtrx_2.getText()));
                try {
                    globlInit = System.currentTimeMillis();
                    masterCompute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObj = new JSONObject(Data.getInstance().getSlaveInformationMap());
                String msgString = jsonObj.toString();
                String msg = msgString;
                Data.getInstance().viewMapContents();
                receiveAndSend.write(msg.getBytes());
            }
        });
    }
    private void connxn(int position) {
        final WifiP2pDevice device = p2pDevices[position];
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Connected to " +
                        device.deviceName, Toast.LENGTH_SHORT).show();
                addressMap.add(device.deviceAddress);
            }
            @Override
            public void onFailure(int reason) {
                Toast.makeText(getApplicationContext(), "Not Connected to "
                        + device.deviceName, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init() {
        discoveryButton = findViewById(R.id.buttonDiscover);
        lv = findViewById(R.id.peerListView);
        conxnStatus = findViewById(R.id.connectionStatus);
        sendButton = findViewById(R.id.sendButton);
        computeButton = findViewById(R.id.btnCompute);
        locationButton = findViewById(R.id.getButton);
        mtrx_1 = findViewById(R.id.Matrix1Text);
        mtrx_2 = findViewById(R.id.Matrix2Text);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        assert manager != null;
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new NetworkRecver(manager, channel, this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
    WifiP2pManager.PeerListListener wifiPeersListnr = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());
                devicesNam = new String[peerList.getDeviceList().size()];
                p2pDevices = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;
                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    devicesNam[index] = device.deviceName;
                    p2pDevices[index] = device;
                    index++;
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, devicesNam);
                lv.setAdapter(arrayAdapter);
            }
            if (peers.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
    WifiP2pManager.ConnectionInfoListener conxnInformationListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if (info.groupFormed && info.isGroupOwner) {
                conxnStatus.setText("Host");
                serverCls = new ServerCls();
                serverCls.start();
            } else if (info.groupFormed) {
                conxnStatus.setText("Client");
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    public class ServerCls extends Thread {
        Socket socket;
        ServerSocket serverSocket;
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                receiveAndSend = new ReceiveAndSend(socket);
                receiveAndSend.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class ReceiveAndSend extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;
        public ReceiveAndSend(Socket skt) {
            socket = skt;
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (socket != null) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class ClientClass extends Thread {
        Socket socket;
        String hostAdd;
        public ClientClass(InetAddress hostAddress) {
            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }
        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd, 8888), 500);
                receiveAndSend = new ReceiveAndSend(socket);
                receiveAndSend.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void connectIndex(int deviceIndex) {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(int reason) {
            }
        });
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WifiP2pDevice device = p2pDevices[findPos(addressMap.get(deviceIndex))];
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(int reason) {
            }
        });
    }
    public int findPos(String mac) {
        for (int i = 0; i < p2pDevices.length; i++) {
            if (p2pDevices[i].deviceAddress.equalsIgnoreCase(mac)) {
                return i;
            }
        }
        return 0;
    }
    public void masterCompute() throws InterruptedException {
        Toast.makeText(getApplicationContext(), "Master side tasks distributed",
                Toast.LENGTH_SHORT).show();
        conxnStatus.setText("Master");
        startPeriodicMonitoring();
        for (int i = 0; i < addressMap.size(); i++) {
            Log.i("Inside Master Compute"," for addressMap for i - " + i);
            try {
                HashMap<String, String> generatedMap = offloading(i);
                HashMap<String, Object> offloadRegister = new HashMap<>();
                long time = System.currentTimeMillis() + Integer.parseInt(generatedMap.get("time")
                        .toString());
                offloadRegister.put("recoverBy", time);
                offloadRegister.put("i", generatedMap.get("i"));
                offloadRegister.put("j", generatedMap.get("j"));
                sndRcvReg.put(addressMap.get(i), offloadRegister);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public HashMap<String, String> generateMap(int index) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("index", index + "");
        for (int i = index * 2; i < index * 2 + 4; i++) {
            dataMap.put(0 + "", Helper.arrayToString(A_1[0]));
            dataMap.put(1 + "", Helper.arrayToString(A_1[1]));
            dataMap.put(2 + "", Helper.arrayToString(A_1[2]));
            dataMap.put(3 + "", Helper.arrayToString(A_1[3]));
            dataMap.put((i + 4) + "", Helper.arrayToString(Helper.getTranspose(A_2)[i]));
        }
        dataMap.put("i", index + "");
        dataMap.put("j", index + "");
        dataMap.put("time", (((index + 1) * 50)) + "");
        dataMap.put("mac", addressMap.get(index));
        return dataMap;
    }
    public HashMap<String, String> offloading(int index) throws JSONException {
        connectIndex(index);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap dataMap = generateMap(0);
        JSONObject jsonObj = new JSONObject(dataMap);
        String jsonString = jsonObj.toString();
        String msg = jsonString;
        receiveAndSend.write(msg.getBytes());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataMap;
    }
    public void slaveComputation(String jsonString) throws JSONException {
        Toast.makeText(getApplicationContext(), "Offload computation started",
                Toast.LENGTH_SHORT).show();
        conxnStatus.setText("Slave");
        long start = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject(jsonString);
        String mac = jsonObject.getString("mac");
        addressMap.add(mac);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] iValues = {"0", "1", "2", "3"};
        String[] jValues = {};
        if (jsonObject.get("index").equals("0")) {
            jValues = new String[]{"4", "5", "6", "7"};
        } else if (jsonObject.get("index").equals("1")) {
            jValues = new String[]{"6", "7"};
        }
        HashMap<String, String> dataMap = new HashMap<>();
        for (int i = 0; i < iValues.length; i++) {
            for (int j = 0; j < jValues.length; j++) {
                dataMap.put(iValues[i] + "," + (((int) jValues[j].charAt(0)) - 4 - 48),
                        Helper.arrayRCMult(jsonObject.get(iValues[i]) + "", jsonObject.get(
                                jValues[j]) + ""));
            }
        }
        int time = Integer.parseInt(jsonObject.get("time") + "");
        dataMap.put("offloadDone", "true");
        dataMap.put("mac", mac);
        long end = System.currentTimeMillis();
        long difference = end - start;
        if (difference < time) {
            try {
                Thread.sleep(time - (difference));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        connectIndex(0);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject(dataMap);
        String returnString = jsonObj.toString();
        String msg = returnString;
        Toast.makeText(getApplicationContext(), "Slave side computation completed",
                Toast.LENGTH_SHORT).show();
        receiveAndSend.write(msg.getBytes());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void uponReceiving(String jsonString) throws JSONException {
        Toast.makeText(getApplicationContext(), "Tasks from slave devices received",
                Toast.LENGTH_SHORT).show();
        JSONObject jsonObject = new JSONObject(jsonString);
        boolean status = Boolean.parseBoolean(jsonObject.get("offloadDone") + "");
        String macAddress = jsonObject.getString("mac");
        jsonObject.remove("offloadDone");
        jsonObject.remove("mac");
        Iterator<String> keysToCopyIterator = jsonObject.keys();
        List<String> keysList = new ArrayList<String>();
        while (keysToCopyIterator.hasNext()) {
            String key = (String) keysToCopyIterator.next();
            keysList.add(key);
        }
        for (int i = 0; i < keysList.size(); i++) {
            int iVal = (int) keysList.get(i).toString().charAt(0) - 48;
            int jVal = (int) keysList.get(i).toString().charAt(2) - 48;
            Out[iVal][jVal] = Integer.parseInt(jsonObject.get(keysList.get(i)).toString());
            System.out.println("Out Value - " + Out[iVal][jVal]);
        }
        sndRcvReg.remove(macAddress);
        generateOutputs();
    }
    public void generateOutputs() {
        String row1, row2, row3, row4, est1, est2;
        row1 = Helper.ar2String(Out[0]);
        row2 = Helper.ar2String(Out[1]);
        row3 = Helper.ar2String(Out[2]);
        row4 = Helper.ar2String(Out[3]);
        est1 = "Estimated Offloading Time: " + (System.currentTimeMillis() -
                globlInit) + "ms \n\n Failure requires additional 100 ms, Fail" +
                "ure calculation depends on battery and proximity ";
        long currentT = System.currentTimeMillis();
        computeMatrix();
        est2 = "Time without Offloading: " + (System.currentTimeMillis() - currentT + 1) + "m" +
                "s"+"\n\n Estimate power consum" +
                "ption is calculated by % fall in bat" +
                "tery due to the app: -0.02%";
        startActivity(new Helper().navigateResultScreen(
                row1,row2,row3,row4,est1,est2,MainActivity.this));
    }
       public void computeMatrix() {
        int[][] outMat = new int[4][4];
        for(int i = 0; i< A_1.length; i++) {
            for(int j = 0; j< A_2.length; j++) {
                outMat[i][j] = 0;
            }
        }
        for(int i = 0; i< A_1.length; i++) {
            for(int j = 0; j< A_2.length; j++) {
                for(int k = 0; k< A_2.length; k++) {
                    outMat[i][j] = outMat[i][j] + A_1[i][k]* A_2[k][j];
                }
            }
        }
    }
    public void initialConnect(String tempMsg) throws JSONException {
        msgBox.setText(tempMsg);
        JSONObject jsonObject = new JSONObject(tempMsg);
    }
    public void startPeriodicMonitoring() throws InterruptedException {
        Thread periodMonitoring = new Thread();
        periodMonitoring.start();
        while (sndRcvReg.size() > 0) {
            Thread.sleep(5000);
            Set<String> keysToCopyIterator = sndRcvReg.keySet();
            Object[] keyList = keysToCopyIterator.toArray();
            for(int i=0; i<keyList.length; i++) {
                connectIndex(addressMap.indexOf(keyList[i]));
                Thread.sleep(50);
                receiveAndSend.write("periodicMonitor".getBytes());
                Thread.sleep(50);
                HashMap<String, Object> deviceMap = (HashMap<String, Object>)
                        sndRcvReg.get(String.valueOf(keyList[i]));
                if(Integer.parseInt(deviceMap.get("battery").toString()) < 20) {
                    FailureComputation recovery=new FailureComputation();
                    recovery.failureRecovery(String.valueOf(keyList[i]),
                            this,new MainActivity());
                }
            }
        }
    }
    public void failureRecovery(String macID) throws InterruptedException {
        Toast.makeText(getApplicationContext(), macID + " is at critical battery level!",
                Toast.LENGTH_SHORT).show();
        HashMap<String, Object> deviceMap = (HashMap<String, Object>) sndRcvReg.get(macID);
        Set keySet = sndRcvReg.keySet();
        for(int i=0; i<addressMap.size(); i++) {
            if(!keySet.contains(addressMap.get(i))) {
                connectIndex(i);
                sndRcvReg.remove(macID);
                sndRcvReg.put(addressMap.get(i), deviceMap);
                HashMap<String, String> dataMap = generateMap(i);
                JSONObject jsonObject = new JSONObject(dataMap);
                String jsonString = jsonObject.toString();
                receiveAndSend.write(jsonString.getBytes());
                Thread.sleep(50);
            }
        }
    }
    public void sendPeriodicResponse() {
        getLocation();
        JSONObject jsonObj = new JSONObject(Data.getInstance().getSlaveInformationMap());
        String msg = jsonObj.toString();
        receiveAndSend.write(msg.getBytes());
    }
}