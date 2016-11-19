package com.android.btcommtest1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private int TURN_ON_REQUEST = 0x1011;
    private int DISCOVERABLE_REQUEST = 0x1012;
    private int ACCESS_COARSE_AND_FINE_LOCATION = 0x1013;

    @BindView(R.id.findDevices) Button findDevices;
    @BindView(R.id.deviceList) Spinner deviceList;
    @BindView(R.id.connectDevice) Button connectDevice;
    @BindView(R.id.msgContent) EditText msgContent;
    @BindView(R.id.sendMsg) Button sendMsg;
    @BindView(R.id.chatLogs) ListView chatLogs;


    private BluetoothBroadcastReceiver bluetoothBroadcastReceiver;
    private IntentFilter intentFilter;
    private BluetoothAdapter bluetoothAdapter;
    private AcceptThread acceptThread = null;
    private ConnectThread connectThread;
    private List<ChatMessage> chatMsgList;
    private ArrayAdapter<ChatMessage> chatMessageArrayAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver(this);
        registerReceiver(bluetoothBroadcastReceiver, intentFilter);

    }


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(bluetoothBroadcastReceiver);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter != null){

            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, TURN_ON_REQUEST);

        }

        /*connectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = (TextView) v;
                Toast.makeText(getApplicationContext(), "Sel Item : "+textView.getText(), Toast.LENGTH_SHORT).show();

            }
        });*/

    }


    //Log.i("onActivityResult", "");
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.i("onActivityResult", "Res code : "+resultCode);

        if(requestCode == TURN_ON_REQUEST){

            //if(resultCode == 1){
                Log.i("onActivityResult", "Bluetooth Turned On");

                Intent makeDiscoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                makeDiscoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(makeDiscoverableIntent, DISCOVERABLE_REQUEST);

            //}else{
                //Bluetooth Turned On failed");
            //}

        }

        else if (requestCode == DISCOVERABLE_REQUEST){

            /*
            if(resultCode == 0){
                Log.i("onActivityResult", "Bluetooth device is discoverable");
            }else{
                Log.i("onActivityResult", "Bluetooth device discoverable failed");
            }
            */

            findDevices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getApplicationContext(), "Btn Test O.K", Toast.LENGTH_SHORT).show();

                    int currentApiVersion = Build.VERSION.SDK_INT; // android.os.Build.VERSION
                    if( currentApiVersion >= Build.VERSION_CODES.M ){
                        MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_AND_FINE_LOCATION);
                    }else {
                        bluetoothAdapter.startDiscovery();
                    }

                }
            });

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == ACCESS_COARSE_AND_FINE_LOCATION){

            if (permissions.length!=0 && grantResults.length!=0){
                bluetoothAdapter.startDiscovery();
            }else
                Toast.makeText(MainActivity.this, "Permission is denied", Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(MainActivity.this, "Access is rejected", Toast.LENGTH_SHORT).show();

    }

    public void enableDeviceListUI(final Set<String> deviceAddressList) {

        deviceList.setVisibility(View.VISIBLE);
        connectDevice.setVisibility(View.VISIBLE);

        List<String> nameList = new ArrayList<String>();

        for (String deviceAddress : deviceAddressList)
            nameList.add( bluetoothAdapter.getRemoteDevice(deviceAddress).getName() );

        ArrayAdapter<String> deviceListAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, nameList);
        deviceList.setAdapter(deviceListAdapter);


        if(acceptThread == null)
            acceptThread = new AcceptThread(bluetoothAdapter, true);
        acceptThread.start();

        connectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deviceName = deviceList.getSelectedItem().toString();

                BluetoothDevice device = getBluetoothDevice(deviceName, deviceAddressList);

                connectThread = new ConnectThread(device, true, bluetoothActivityHandler);
                connectThread.start();

            }

            private BluetoothDevice getBluetoothDevice(String deviceName, Set<String> deviceAddressList) {

                for(String deviceAddress : deviceAddressList)
                    if(bluetoothAdapter.getRemoteDevice(deviceAddress).getName().equals(deviceName))
                        return bluetoothAdapter.getRemoteDevice(deviceAddress);

                return null;
            }

        });

    }


    public void enableChatUI() {

        msgContent.setVisibility(View.VISIBLE);
        sendMsg.setVisibility(View.VISIBLE);
        chatLogs.setVisibility(View.VISIBLE);

        chatMsgList = new ArrayList<ChatMessage>();
        chatMessageArrayAdapter = new ChatArrayAdapter(this, 0, chatMsgList);
        chatLogs.setAdapter(chatMessageArrayAdapter);

        MyGlobals.bluetoothActivityHandler = bluetoothActivityHandler;

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                byte[] msgBuffer = msgContent.getText().toString().getBytes();

                ConnectedThread connectedThread = MyGlobals.connectedThread;

                connectedThread.sendMessage(msgBuffer);

                msgContent.setText("");


            }
        });


    }


    private Handler bluetoothActivityHandler = new Handler(){


        @Override
        public void handleMessage(Message msg) {

            String message;
            byte[] msgBuffer;

            switch (msg.what){


                case MyGlobals.MESSAGE_READ :

                    msgBuffer = (byte[])msg.obj;

                    message = new String(msgBuffer, 0, msg.arg1);

                    Log.i("MainActivity", "Message Received : "+message);

                    addMsgToList(message, false);

                    scrollMyListViewToBottom();

                    break;


                case MyGlobals.MESSAGE_WRITE :

                    msgBuffer = (byte[])msg.obj;

                    message = new String(msgBuffer);

                    Log.i("MainActivity", "Message Sent : "+message);

                    addMsgToList(message, true);

                    scrollMyListViewToBottom();

                    break;

            }

        }
    };

    private void scrollMyListViewToBottom() {

        chatLogs.post(new Runnable() {
            @Override
            public void run() {
                chatLogs.setSelection(chatMessageArrayAdapter.getCount()-1);
            }
        });

    }

    private void addMsgToList(String message, boolean isSender) {

        ChatMessage chatMessage = new ChatMessage(message, isSender, false);
        chatMsgList.add(chatMessage);
        chatMessageArrayAdapter.notifyDataSetChanged();

    }

}
