package com.futsal.manager.EmbeddedCommunicationModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.MakeNewMemoryModule.MakeNewMemoryManager;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE_SOCKET;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.SERIAL_UUID;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class BluetoothDeviceSelectorProcesser {

    Activity bluetoothDeviceSelector;

    public BluetoothDeviceSelectorProcesser(Activity bluetoothDeviceSelector) {
        this.bluetoothDeviceSelector = bluetoothDeviceSelector;
    }

    public boolean TryConnectToTargetDevice(BluetoothDeviceItemModel targetBluetoothDeviceItemModel) {
        LogManager.PrintLog("BluetoothDeviceSelectorProcesser", "TryConnectToTargetDevice", "name: " + targetBluetoothDeviceItemModel.GetDeviceName()
        + " address: " + targetBluetoothDeviceItemModel.GetDeviceMacAddress(), LOG_LEVEL_DEBUG);
        BluetoothDeviceConnectionInit bluetoothDeviceConnectionInit = new BluetoothDeviceConnectionInit(bluetoothDeviceSelector, targetBluetoothDeviceItemModel);
        bluetoothDeviceConnectionInit.execute();
        return false;
    }

    public class BluetoothDeviceConnectionInit extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog bluetoothDeviceConnectionInitProgress;
        Activity bluetoothDeviceSelector;
        BluetoothDeviceItemModel targetBluetoothDeviceItemModel;


        public BluetoothDeviceConnectionInit(Activity bluetoothDeviceSelector, BluetoothDeviceItemModel targetBluetoothDeviceItemModel) {
            super();
            this.bluetoothDeviceSelector = bluetoothDeviceSelector;
            this.targetBluetoothDeviceItemModel = targetBluetoothDeviceItemModel;
            bluetoothDeviceConnectionInitProgress = new ProgressDialog(bluetoothDeviceSelector);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                EMBEDDED_SYSTEM_DEVICE = EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.getRemoteDevice(targetBluetoothDeviceItemModel.GetDeviceMacAddress());
                EMBEDDED_SYSTEM_DEVICE_SOCKET = EMBEDDED_SYSTEM_DEVICE.createInsecureRfcommSocketToServiceRecord(SERIAL_UUID);
                EMBEDDED_SYSTEM_DEVICE_SOCKET.connect();
                return true;
            }
            catch (Exception err) {
                LogManager.PrintLog("BluetoothDeviceConnectionInit", "doInBackground", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            try {
                bluetoothDeviceConnectionInitProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                bluetoothDeviceConnectionInitProgress.setMessage(bluetoothDeviceSelector.getString(R.string.waitWhileConnecting));

                bluetoothDeviceConnectionInitProgress.show();
            }
            catch (Exception err) {
                LogManager.PrintLog("BluetoothDeviceConnectionInit", "onPreExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean isProcessSuccessfullyEnded) {
            try {
                bluetoothDeviceConnectionInitProgress.dismiss();

                if(isProcessSuccessfullyEnded) {
                    LogManager.PrintLog("BluetoothDeviceConnectionInit", "onPostExecute", "Ok device is ready", LOG_LEVEL_INFO);

                    Intent startMakeNewMemory = new Intent(bluetoothDeviceSelector, MakeNewMemoryManager.class);
                    bluetoothDeviceSelector.startActivity(startMakeNewMemory);
                    bluetoothDeviceSelector.finish();
                }
                else {
                    final AlertDialog.Builder bluetoothConnectionWarningDialog  = new AlertDialog.Builder(bluetoothDeviceSelector);
                    bluetoothConnectionWarningDialog.setMessage(bluetoothDeviceSelector.getString(R.string.cannotConnectDeviceTitle));
                    bluetoothConnectionWarningDialog.setTitle(bluetoothDeviceSelector.getString(R.string.cannotConnectDeviceMessage));
                    bluetoothConnectionWarningDialog.setPositiveButton(bluetoothDeviceSelector.getString(R.string.okJesus), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    bluetoothConnectionWarningDialog.setCancelable(true);
                    bluetoothConnectionWarningDialog.create().show();
                }
            }
            catch (Exception err) {
                LogManager.PrintLog("BluetoothDeviceConnectionInit", "onPostExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPostExecute(isProcessSuccessfullyEnded);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
