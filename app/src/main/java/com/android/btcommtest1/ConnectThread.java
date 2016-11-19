package com.android.btcommtest1;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Gowtham on 10-10-2016.
 */
public class ConnectThread extends Thread{

    private final Handler bluetoothActivityHandler;
    private BluetoothDevice remoteBluetoothDevice;
    private boolean socketType;
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;

    public ConnectThread(BluetoothDevice remoteBluetoothDevice, boolean socketType, Handler bluetoothActivityHandler) {

        this.remoteBluetoothDevice = remoteBluetoothDevice;
        this.socketType = socketType;
        this.bluetoothActivityHandler = bluetoothActivityHandler;

        BluetoothSocket tmp = null;

        try {

            if (socketType)
                tmp = remoteBluetoothDevice.createRfcommSocketToServiceRecord(MyGlobals.getUUID());
            else
                tmp = remoteBluetoothDevice.createInsecureRfcommSocketToServiceRecord(MyGlobals.getUUID());

        } catch (IOException e) {
            e.printStackTrace();
        }

        bluetoothSocket = tmp;

    }


    @Override
    public void run() {


        try{

            bluetoothSocket.connect();

        } catch (IOException e) {
            e.printStackTrace();

            Log.e("ConnectThread", "Error in Connecting to the device");

        }

        connected( bluetoothSocket, socketType );

    }

    private synchronized void connected(BluetoothSocket bluetoothSocket, boolean socketType) {

        Log.i("ConnectThread", "Connected.... Socket Type :- secured");

        Log.i("ConnectThread", "Connected to "+remoteBluetoothDevice.getName());

        ConnectedThread connectedThread=new ConnectedThread(bluetoothSocket, socketType);
        MyGlobals.connectedThread = connectedThread;
        connectedThread.start();

    }

}
