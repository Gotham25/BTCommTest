package com.android.btcommtest1;

import android.app.Application;
import android.os.Handler;

import java.util.UUID;

/**
 * Created by Gowtham on 09-10-2016.
 */

public class MyGlobals extends Application {

    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;

    public static Handler bluetoothActivityHandler;
    public static ConnectedThread connectedThread;


    public static UUID getUUID() {
        return UUID.fromString("494ad2cf-057f-484f-bbd0-54b5ec7b92a3");
    }
}
