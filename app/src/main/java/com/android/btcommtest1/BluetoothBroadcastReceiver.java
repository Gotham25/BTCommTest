package com.android.btcommtest1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by Gowtham on 02-10-2016.
 */

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private final MainActivity mainActivity;
    private Set<String> deviceAddressList;

    public BluetoothBroadcastReceiver(MainActivity mainActivity){

        deviceAddressList = new HashSet<String>();

        this.mainActivity = mainActivity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();

        if(action.equals(BluetoothAdapter.ACTION_REQUEST_ENABLE)){
            Log.i("BB Receiver", "turn on Bluetooth Requested");
        }else if(action.equals(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)){
            Log.i("BB Receiver", "device is made discoverable");
        }else if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)){
            Log.i("BB Receiver", "connection state changed");
        }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
            Log.i("BB Receiver", "discovery started");
        }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
            Log.i("BB Receiver", "discovery finished");

            Log.i("BB Receiver", "Devices List Size : "+deviceAddressList.size());
            mainActivity.enableDeviceListUI(deviceAddressList);

        }else if(action.equals(BluetoothDevice.ACTION_FOUND)){

            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            deviceAddressList.add(bluetoothDevice.getAddress());

            Log.i("BB Receiver", "device found");
            Log.i("BB Receiver", "Device Name : "+bluetoothDevice.getName()+"  Device Address : "+bluetoothDevice.getAddress());


        }else if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
            Log.i("BB Receiver", "bond state changed");
        }else if(action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)){
            Log.i("BB Receiver", "pairing requested");
        }else if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)){

            Log.i("BB Receiver", "ACL Connected");

                mainActivity.enableChatUI();

        }else if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)){
            Log.i("BB Receiver", "ACL Dis-connection requested");
        }else if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
            Log.i("BB Receiver", "ACL Disconnected");
        }

    }
}
