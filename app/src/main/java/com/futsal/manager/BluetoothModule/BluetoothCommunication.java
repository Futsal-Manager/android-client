package com.futsal.manager.BluetoothModule;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import com.futsal.manager.LogModule.LogManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.futsal.manager.DefineManager.BLUETOOTH_CONNECTION_FAILURE;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;

/**
 * Created by stories2 on 2017. 2. 22..
 */

public class BluetoothCommunication extends Thread implements Serializable {

    final static int BLE_RECEIVE_MODE = 0, BLE_SEND_MODE = 1;

    final String bluetoothCommunicationLogCatTag = "bluetooth communication";
    final UUID bluetoothUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothDevice targetBluetoothDevice;
    BluetoothSocket bluetoothSocket;
    Thread bluetoothCommunicationThread;
    BluetoothServerSocket bluetoothServerSocket;
    BluetoothAdapter bluetoothAdapter;

    String recentlyOrder, selectedDeviceAddress;

    int mode;

    public BluetoothCommunication() {
        bluetoothCommunicationThread = new Thread(this);
        mode = 0;
        recentlyOrder = "A";
    }

    public BluetoothCommunication(BluetoothAdapter targetBluetoothAdapter, String selectedDeviceAddress) {
        this.bluetoothAdapter = targetBluetoothAdapter;
        this.selectedDeviceAddress = selectedDeviceAddress;
    }

    public void SetBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public BluetoothAdapter GetBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public String GetSelectedDeviceAddress() {
        return selectedDeviceAddress;
    }
//주석 추가해 두기
    //android design pattern
    //mvc
    @Override
    public void run() {
        super.run();
        InitConnection();
        //CloseConnection();
    }

    public void TryToCommunication(int mode) {
        this.mode = mode;
        ConnectedThread connectedThread;
        switch (mode) {/*
            case BLE_RECEIVE_MODE:
                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
                break;
            case BLE_SEND_MODE:
                connectedThread = new ConnectedThread(bluetoothAdapter);
                connectedThread.start();
                break;*/
            default:
                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
        }
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
            if(!bluetoothSocket.isConnected())
                bluetoothSocket.connect();
            else {
                Log.d(bluetoothCommunicationLogCatTag, "already connected");
            }
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

    public boolean ConnectToTargetBluetoothDevice(BluetoothAdapter targetBluetoothAdapter, String selectedDeviceAddress) {
        this.bluetoothAdapter = targetBluetoothAdapter;
        this.targetBluetoothDevice = targetBluetoothAdapter.getRemoteDevice(selectedDeviceAddress);;
        try {
            ParcelUuid list[] = targetBluetoothDevice.getUuids();
            for(ParcelUuid uuid : list) {
                //Log.d("ble", "connect uuid: " + uuid);
                LogManager.PrintLog("BluetoothCommunication", "ConnectToTargetBluetoothDevice", "Support UUID: " + uuid, LOG_LEVEL_DEBUG);
            }
            //bluetoothSocket = createBluetoothSocket(targetBluetoothDevice);
            bluetoothSocket = createBluetoothSocketBasedOnStackoverflow(targetBluetoothDevice);
            bluetoothCommunicationThread.start();
            return true;
        }
        catch (Exception err) {
            LogManager.PrintLog("BluetoothCommunication", "ConnectToTargetBluetoothDevice", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            //Log.d(bluetoothCommunicationLogCatTag, "Error in ConnectToTargetBluetoothDevice: " + err.getMessage());
        }
        return false;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
            throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, bluetoothUUID);
            } catch (Exception e) {
                Log.e(bluetoothCommunicationLogCatTag, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(bluetoothUUID);
    }

    private BluetoothSocket createBluetoothSocketBasedOnStackoverflow(BluetoothDevice device) {

        UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service
        //UUID SERIAL_UUID = device.getUuids()[0].getUuid(); //if you don't know the UUID of the bluetooth device service, you can get it like this from android cache

        BluetoothSocket socket = null;

        try {
            socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
        } catch (Exception e) {Log.e(bluetoothCommunicationLogCatTag,"Error creating socket");}

        try {
            socket.connect();
            Log.e(bluetoothCommunicationLogCatTag,"Connected");
            return socket;
        } catch (IOException e) {
            Log.e(bluetoothCommunicationLogCatTag,e.getMessage());
            try {
                Log.e(bluetoothCommunicationLogCatTag,"trying fallback...");

                socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                socket.connect();

                Log.e(bluetoothCommunicationLogCatTag,"Connected");
                return socket;
            }
            catch (Exception e2) {
                Log.e(bluetoothCommunicationLogCatTag, "Couldn't establish Bluetooth connection!");
            }
        }
        return socket;
    }

    public BluetoothSocket createBluetoothSocketBasedOnStackoverflow2(BluetoothSocket device) {
        try
        {
            Class<?> clazz = device.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
            Method m = clazz.getMethod("createRfcommSocket", paramTypes);
            Object[] params = new Object[] {Integer.valueOf(1)};
            return (BluetoothSocket) m.invoke(device.getRemoteDevice(), params);
        }
        catch (Exception e)
        {
            Log.d(bluetoothCommunicationLogCatTag, "Error in createBluetoothSocketBasedOnStackoverflow2: " + e.getMessage());
        }
        return null;
    }

    public void SetOrder(String recentlyOrder) {
        this.recentlyOrder = recentlyOrder;
    }

    private class ConnectedThread extends Thread {
        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;
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

        public ConnectedThread(BluetoothAdapter bleAdapter) {
            UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            try {
                bluetoothServerSocket = bleAdapter.listenUsingRfcommWithServiceRecord("BT_SERVER", SERIAL_UUID);
                Log.d(bluetoothCommunicationLogCatTag, "i am listening: " + bluetoothServerSocket);
                bluetoothSocket = bluetoothServerSocket.accept();
                Log.d(bluetoothCommunicationLogCatTag, "someone came to me: " + bluetoothSocket);
                InputStream tmpIn = null;
                OutputStream tmpOut = null; // BluetoothSocket의 inputstream 과 outputstream을 얻는다.
                try {
                    tmpIn = bluetoothSocket.getInputStream();
                    tmpOut = bluetoothSocket.getOutputStream();
                }
                catch (IOException e) {
                    Log.e(TAG, "temp sockets not created", e);
                }
                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }
            catch (Exception err) {
                Log.d(bluetoothCommunicationLogCatTag, "Error in ConnectedThread: " + err.getMessage());
            }
        }
        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes; // Keep listening to the InputStream while connected
            String testCode = recentlyOrder;
            Log.d(bluetoothCommunicationLogCatTag, "Sending Order: " + recentlyOrder);
            while (true) {
                try { // InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
                    switch (mode) {
                        case BLE_RECEIVE_MODE:
                            bytes = mmInStream.read(buffer);
                            Log.d("ble", new String(buffer));
                            break;
                        case BLE_SEND_MODE:
                            Log.d("ble", "sending");
                            write(testCode.getBytes());
                            break;
                        default:
                            break;
                    }
                    //
                    //
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
                BLUETOOTH_CONNECTION_FAILURE = true;
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