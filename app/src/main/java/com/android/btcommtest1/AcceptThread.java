package com.android.btcommtest1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Gowtham on 10-10-2016.
 */
public class AcceptThread extends Thread{

    private final boolean socketType;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothServerSocket bluetoothServerSocket;

    public AcceptThread(BluetoothAdapter bluetoothAdapter, boolean socketType) {

        this.bluetoothAdapter = bluetoothAdapter;
        this.socketType = socketType;

        BluetoothServerSocket temp = null;

        try{
            if(socketType)
                temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BTCommTest1_Secured", MyGlobals.getUUID());
            else
                temp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("BTCommTest1_InSecured", MyGlobals.getUUID());
        } catch (IOException e) {
            e.printStackTrace();
        }

        bluetoothServerSocket = temp;

    }


    @Override
    public void run() {

        BluetoothSocket bluetoothSocket = null;

        while (true){

            try{
                bluetoothSocket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(bluetoothSocket!=null){

                Log.i("AcceptThread", "Connected to "+bluetoothSocket.getRemoteDevice().getName());


                ConnectedThread connectedThread=new ConnectedThread(bluetoothSocket, socketType);
                MyGlobals.connectedThread = connectedThread;
                connectedThread.start();

            }

        }

    }
}
