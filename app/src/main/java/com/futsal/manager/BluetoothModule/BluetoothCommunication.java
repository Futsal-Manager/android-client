package com.futsal.manager.BluetoothModule;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by stories2 on 2017. 2. 22..
 */

public class BluetoothCommunication extends Thread{

    final String bluetoothCommunicationLogCatTag = "bluetooth communication";
    final UUID bluetoothUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    BluetoothDevice targetBluetoothDevice;
    BluetoothSocket bluetoothSocket;
    Thread bluetoothCommunicationThread;

    public BluetoothCommunication() {
        bluetoothCommunicationThread = new Thread(this);
    }

    @Override
    public void run() {
        super.run();
        InitConnection();
        CloseConnection();
    }

    public void CloseConnection() {
        try {
            if(bluetoothSocket.isConnected()) {
                bluetoothSocket.close();
            }
        }
        catch (Exception err) {
            Log.d(bluetoothCommunicationLogCatTag, "Error in CloseConnection: " + err.getMessage());
        }
    }

    public void InitConnection() {
        try {
            bluetoothSocket.connect();
        }
        catch (Exception err) {
            Log.d(bluetoothCommunicationLogCatTag, "Error in InitConnection: " + err.getMessage());
            try {
                bluetoothSocket =(BluetoothSocket) targetBluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(targetBluetoothDevice,1);
                bluetoothSocket.connect();
                Log.d(bluetoothCommunicationLogCatTag, "ok");
            }
            catch (Exception err2) {
                Log.d(bluetoothCommunicationLogCatTag, "Error in InitConnection2: " + err.getMessage());
            }
        }
    }

    public void ConnectToTargetBluetoothDevice(BluetoothDevice targetBluetoothDevice) {
        this.targetBluetoothDevice = targetBluetoothDevice;
        try {
            bluetoothSocket = createBluetoothSocket(targetBluetoothDevice);
            bluetoothCommunicationThread.start();
        }
        catch (Exception err) {
            Log.d(bluetoothCommunicationLogCatTag, "Error in ConnectToTargetBluetoothDevice: " + err.getMessage());
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
            throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, bluetoothUUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(bluetoothUUID);
    }
}
