package com.futsal.manager.BluetoothModule;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by stories2 on 2017. 2. 22..
 */

public class BluetoothCommunication extends Thread{

    final String bluetoothCommunicationLogCatTag = "bluetooth communication";
    final UUID bluetoothUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothDevice targetBluetoothDevice;
    BluetoothSocket bluetoothSocket;
    Thread bluetoothCommunicationThread;

    public BluetoothCommunication() {
        bluetoothCommunicationThread = new Thread(this);
    }
//주석 추가해 두기
    //android design pattern
    //mvc
    @Override
    public void run() {
        super.run();
        InitConnection();
        ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
        connectedThread.start();
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

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null; // BluetoothSocket의 inputstream 과 outputstream을 얻는다.
             try {
                 tmpIn = socket.getInputStream();
                 tmpOut = socket.getOutputStream();
             }
             catch (IOException e) {
                 Log.e(TAG, "temp sockets not created", e);
             }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes; // Keep listening to the InputStream while connected
            String testCode = "Hello World";
            while (true) {
                try { // InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
                     //bytes = mmInStream.read(buffer);
                    write(testCode.getBytes());
                    //Log.d(TAG, new String(bytes));
                    break;
                }
                catch (Exception e)
                {
                    Log.e(TAG, "disconnected", e);
                    //connectionLost();
                    break;
                }
            }
        } /** * Write to the connected OutStream. * @param buffer The bytes to write */
        public void write(byte[] buffer) {
            try { // 값을 쓰는 부분(값을 보낸다
                 mmOutStream.write(buffer);
            }
            catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            }
            catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }


}
