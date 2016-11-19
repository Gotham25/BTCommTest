package com.android.btcommtest1;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Gowtham on 10-10-2016.
 */
public class ConnectedThread extends Thread{

    private Handler bluetoothActivityHandler;
    private OutputStream oStream;
    private InputStream iStream;
    private BluetoothSocket bluetoothSocket;
    private boolean socketType;

    public ConnectedThread(BluetoothSocket bluetoothSocket, boolean socketType) {

        this.bluetoothSocket = bluetoothSocket;
        this.socketType = socketType;
        this.bluetoothActivityHandler = MyGlobals.bluetoothActivityHandler;

        InputStream tmpInputStream = null;
        OutputStream tmpOutputStream = null;
        try {

            tmpInputStream = bluetoothSocket.getInputStream();
            tmpOutputStream = bluetoothSocket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.iStream = tmpInputStream;
        this.oStream = tmpOutputStream;


        Log.i("ConnectedThread", "iStream : "+iStream);
        Log.i("ConnectedThread", "oStream : "+oStream);

    }


    @Override
    public void run() {

        byte[] msgBuffer = new byte[1024];
        int bytesRead;
        Log.i("ConnectedThread", "Inside run() in ConnectedThread");


        while (true){
            
            
            try{

                Log.i("ConnectedThread", "Stream check\n\niStream==null : "+( iStream==null )+"\n\n");

                Log.i("ConnectedThread", "Waiting for Connection..............");

                bytesRead = iStream.read(msgBuffer);

                bluetoothActivityHandler.obtainMessage(MyGlobals.MESSAGE_READ, bytesRead, -1, msgBuffer).sendToTarget();
                
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public void sendMessage(byte[] msgBuffer) {


        try {

            Log.i("ConnectedThread", "Stream check\n\noStream==null : "+( iStream==null )+"\n\n");

            oStream.write(msgBuffer);

            bluetoothActivityHandler.obtainMessage(MyGlobals.MESSAGE_WRITE, -1, -1, msgBuffer).sendToTarget();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
