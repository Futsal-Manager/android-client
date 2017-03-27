package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

import java.util.Set;

/**
 * Created by stories2 on 2017. 3. 27..
 */

public class BluetoothDeviceFinderProcesser {

    BluetoothAdapter deviceBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter listViewArrayAdapter;
    Activity bluetoothDeviceFinder;
    final int REQUEST_ENABLE_BT = 1;

    public BluetoothDeviceFinderProcesser(Activity bluetoothDeviceFinder) {
        this.bluetoothDeviceFinder = bluetoothDeviceFinder;
    }

    public void SetListViewArrayAdapter(ArrayAdapter listViewArrayAdapter) {
        this.listViewArrayAdapter = listViewArrayAdapter;
    }

    public BluetoothAdapter GetDeviceBluetoothAdapter() {
        return deviceBluetoothAdapter;
    }

    public void InitBluetooth() {
        deviceBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(IsThatDeviceCanUseBluetooth()) {
            EnableDeviceBluetooth();

            //IntentFilter bluetoothScanIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            //registerReceiver(broadcastReceiver, bluetoothScanIntent);

            ScanPairedDevicesList();
        }
    }

    public void ScanPairedDevicesList() {
        //Log.d(bluetoothManagerLogCatTag, "scan paired devices list");
        LogManager.PrintLog("BluetoothDeviceFinder", "ScanPairedDevicesList", "scan paired devices list", DefineManager.LOG_LEVEL_INFO);
        pairedDevices = deviceBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for (BluetoothDevice eachDeviceInfo : pairedDevices) {
                String deviceInfo = eachDeviceInfo.getName().toString() + "\n" + eachDeviceInfo.getAddress().toString();
                //Log.d(bluetoothManagerLogCatTag, deviceInfo);
                LogManager.PrintLog("BluetoothDeviceFinder", "ScanPairedDevicesList", "Device: " + deviceInfo, DefineManager.LOG_LEVEL_INFO);
                listViewArrayAdapter.add(deviceInfo);
            }
        }
    }

    public boolean IsThatDeviceCanUseBluetooth() {
        if(deviceBluetoothAdapter != null) {
            //Log.d(bluetoothManagerLogCatTag, "bluetooth init ok");
            LogManager.PrintLog("BluetoothDeviceFinder", "IsThatDeviceCanUseBluetooth", "bluetooth init ok", DefineManager.LOG_LEVEL_INFO);
            return true;
        }
        else {
            //Log.d(bluetoothManagerLogCatTag, "bluetooth init fail");
            LogManager.PrintLog("BluetoothDeviceFinder", "IsThatDeviceCanUseBluetooth", "bluetooth init fail", DefineManager.LOG_LEVEL_ERROR);
            bluetoothDeviceFinder.finish();
            return false;
        }
    }

    public void EnableDeviceBluetooth() {
        if(deviceBluetoothAdapter.isEnabled()) {
            //Log.d(bluetoothManagerLogCatTag, "ok rdy to use bluetooth");
            LogManager.PrintLog("BluetoothDeviceFinder", "EnableDeviceBluetooth", "ok rdy to use bluetooth", DefineManager.LOG_LEVEL_INFO);
            //ScanBluetoothDevices();
        }
        else {
            Intent requestBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            requestBluetoothIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bluetoothDeviceFinder.startActivityForResult(requestBluetoothIntent, REQUEST_ENABLE_BT);
        }
    }
}
