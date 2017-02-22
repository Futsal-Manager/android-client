package com.futsal.manager.BluetoothModule;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 2. 22..
 */

public class BluetoothManager extends Activity{

    final String bluetoothManagerLogCatTag = "bluetoothManager";
    final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter deviceBluetoothAdapter;

    Handler bluetoothManagerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_manager);

        InitBluetooth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK) {
                    Log.d(bluetoothManagerLogCatTag, "ok rdy to use bluetooth");
                    ScanBluetoothDevices();
                }
                else {
                    Log.d(bluetoothManagerLogCatTag, "god damn, go away!");
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public void InitBluetooth() {
        deviceBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(IsThatDeviceCanUseBluetooth()) {
            EnableDeviceBluetooth();

            IntentFilter bluetoothScanIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadcastReceiver, bluetoothScanIntent);
        }
    }

    public boolean IsThatDeviceCanUseBluetooth() {
        if(deviceBluetoothAdapter != null) {
            Log.d(bluetoothManagerLogCatTag, "bluetooth init ok");
            ScanBluetoothDevices();
            return true;
        }
        else {
            Log.d(bluetoothManagerLogCatTag, "bluetooth init fail");
            finish();
            return false;
        }
    }

    public void EnableDeviceBluetooth() {
        if(deviceBluetoothAdapter.isEnabled()) {
            Log.d(bluetoothManagerLogCatTag, "ok rdy to use bluetooth");
        }
        else {
            Intent requestBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(requestBluetoothIntent, REQUEST_ENABLE_BT);
        }
    }

    public void ScanBluetoothDevices() {
        deviceBluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Log.d(bluetoothManagerLogCatTag, "name: " + device.getName() + " add: " + device.getAddress());
            }
        }
    };
}
