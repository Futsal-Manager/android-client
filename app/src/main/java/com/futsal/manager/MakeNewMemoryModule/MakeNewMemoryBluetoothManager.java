package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.os.AsyncTask;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

import java.io.OutputStream;

import static com.futsal.manager.DefineManager.BLUETOOTH_CONNECTION_FAILURE;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE_SOCKET;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class MakeNewMemoryBluetoothManager {

    Activity makeNewMemoryManager;
    BluetoothOrderSender bluetoothOrderSender;

    public MakeNewMemoryBluetoothManager(Activity makeNewMemoryManager) {
        this.makeNewMemoryManager = makeNewMemoryManager;
    }

    public void SendBluetoothOrder(String ballPositionDataOrder) {
        if(EMBEDDED_SYSTEM_DEVICE_SOCKET != null) {
            if(ballPositionDataOrder != null) {
                LogManager.PrintLog("MakeNewMemoryBluetoothManager", "SendBluetoothOrder", "Starting sending process", LOG_LEVEL_INFO);
                bluetoothOrderSender = new BluetoothOrderSender();
                bluetoothOrderSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ballPositionDataOrder);
            }
            else {
                LogManager.PrintLog("MakeNewMemoryBluetoothManager", "SendBluetoothOrder", "ball position data = null", LOG_LEVEL_WARN);
            }
        }
        else {
            LogManager.PrintLog("MakeNewMemoryBluetoothManager", "SendBluetoothOrder", "EMBEDDED_SYSTEM_DEVICE_SOCKET = null", LOG_LEVEL_WARN);
        }
    }

    public class BluetoothOrderSender extends AsyncTask<String, Void, Void> {
        public BluetoothOrderSender() {
            super();
        }

        @Override
        protected Void doInBackground(String... params) {
            LogManager.PrintLog("MakeNewMemoryBluetoothManager", "doInBackground", "Message: " + params[0], DefineManager.LOG_LEVEL_DEBUG);
            try {
                OutputStream bluetoothSendMessage = EMBEDDED_SYSTEM_DEVICE_SOCKET.getOutputStream();
                bluetoothSendMessage.write(params[0].getBytes());
                bluetoothSendMessage.flush();
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewMemoryBluetoothManager", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                BLUETOOTH_CONNECTION_FAILURE = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
