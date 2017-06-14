package com.futsal.manager.EmbeddedCommunicationModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.Set;

import static com.futsal.manager.DefineManager.AVAILABLE_BLUETOOTH_NAME;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST;
import static com.futsal.manager.DefineManager.ENABLE_BLUETOOTH_MODULE_USER_ACCESS_ACCEPT;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;
import static com.futsal.manager.DefineManager.NEW_DEVICE_FOUNDED;

/**
 * Created by stories2 on 2017. 6. 14..
 */

public class EmbeddedSystemFinderProcesserVer2 {

    Activity embeddedSystemFinderVer2;
    Handler bluetoothDeviceFinderLayoutHandler;

    public EmbeddedSystemFinderProcesserVer2(Activity embeddedSystemFinderVer2, Handler bluetoothDeviceFinderLayoutHandler) {
        if(embeddedSystemFinderVer2 == null) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "EmbeddedSystemFinderProcesserVer2", "activity is null", LOG_LEVEL_WARN);
        }
        this.embeddedSystemFinderVer2 = embeddedSystemFinderVer2;
        this.bluetoothDeviceFinderLayoutHandler = bluetoothDeviceFinderLayoutHandler;
    }

    public BroadcastReceiver bluetoothDeviceSearcher = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String bluetoothSearchAction = intent.getAction();
            LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "onReceive", "Action: " + bluetoothSearchAction, LOG_LEVEL_INFO);

            if(BluetoothDevice.ACTION_FOUND.equals(bluetoothSearchAction)) {
                BluetoothDevice eachBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothDeviceItemModel bluetoothDeviceItemModel;

                String deviceName, deviceAddress;
                deviceName = eachBluetoothDevice.getName();
                deviceAddress = eachBluetoothDevice.getAddress();

                LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "onReceive", "name: " + deviceName + " address: " + deviceAddress, LOG_LEVEL_DEBUG);

                if(eachBluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    bluetoothDeviceItemModel = new BluetoothDeviceItemModel(
                            deviceName, deviceAddress, true
                    );
                }
                else {
                    bluetoothDeviceItemModel = new BluetoothDeviceItemModel(
                            deviceName, deviceAddress, false
                    );
                }
                if(deviceName != null) {
                    if(deviceName.contains(AVAILABLE_BLUETOOTH_NAME)) {
                        EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.add(bluetoothDeviceItemModel);
                        bluetoothDeviceFinderLayoutHandler.sendEmptyMessage(NEW_DEVICE_FOUNDED);
                    }
                }
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(bluetoothSearchAction)) {
                if(EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.isEmpty()) {
                    LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "onReceive", "Cannot found any bluetooth device", LOG_LEVEL_WARN);
                }
                else {
                    LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "onReceive", "Ok we found some devices", LOG_LEVEL_DEBUG);
                }/*
                UpdateTextOfProcessStatus("Ok, just wait a sec.");
                UselessDelay(1);
                MoveToDeviceSelectManager();*/
            }
        }
    };

    void InitBluetoothFinder() {

        EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();

        if(EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST == null) {
            EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST = new ArrayList<BluetoothDeviceItemModel>();
        }

        if(EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER != null) {
            EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.clear();
            BluetoothSearchingProcess();
        }
        else {
            ShowWarningDialog();
        }
    }

    void BluetoothSearchingProcess() {
        if(EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.isEnabled()) {
            GetPairedDevice();
            SearchBluetoothDevice();
        }
        else {
            EnableBluetoothModule();
        }
    }

    public void ShowWarningDialog() {
        try {
            AlertDialog.Builder bluetoothNotAvailableDialogBuilder = new AlertDialog.Builder(embeddedSystemFinderVer2);
            bluetoothNotAvailableDialogBuilder.setMessage(embeddedSystemFinderVer2.getString(R.string.notAvailalbeMessage));
            bluetoothNotAvailableDialogBuilder.setPositiveButton(R.string.okIwillBuyNewProduct, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    embeddedSystemFinderVer2.finish();
                }
            });
            AlertDialog bluetoothModuleInitFailMessage = bluetoothNotAvailableDialogBuilder.create();
            bluetoothModuleInitFailMessage.show();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "ShowWarningDialog", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    void SearchBluetoothDevice() {
        LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "SearchBluetoothDevice", "Try search ble", LOG_LEVEL_DEBUG);
        IntentFilter bluetoothDeviceSearcherRegister = new IntentFilter();

        bluetoothDeviceSearcherRegister.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothDeviceSearcherRegister.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        bluetoothDeviceSearcherRegister.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        embeddedSystemFinderVer2.registerReceiver(bluetoothDeviceSearcher, bluetoothDeviceSearcherRegister);
        EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.startDiscovery();
    }

    void EnableBluetoothModule() {
        LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "EnableBluetoothModule", "Try enable ble", LOG_LEVEL_DEBUG);
        Intent enableBluetoothModuleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //enableBluetoothModuleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        embeddedSystemFinderVer2.startActivityForResult(enableBluetoothModuleIntent, ENABLE_BLUETOOTH_MODULE_USER_ACCESS_ACCEPT);
    }

    void GetPairedDevice() {
        Set<BluetoothDevice> pairedDeviceList = EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.getBondedDevices();
        for(BluetoothDevice eachPairedDevice : pairedDeviceList) {

            String deviceName, deviceAddress;
            deviceName = eachPairedDevice.getName();
            deviceAddress = eachPairedDevice.getAddress();

            BluetoothDeviceItemModel bluetoothDeviceItemModel = new BluetoothDeviceItemModel(deviceName, deviceAddress, true);

            LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "GetPairedDevice", "name: " + deviceName + " address: " + deviceAddress, LOG_LEVEL_DEBUG);

            if(deviceName != null) {
                if(deviceName.contains(AVAILABLE_BLUETOOTH_NAME)) {
                    EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.add(bluetoothDeviceItemModel);
                }
            }
        }
    }

    public void StopSearchDevices() {
        try {
            if(EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.isDiscovering()) {
                EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.cancelDiscovery();
            }
            embeddedSystemFinderVer2.unregisterReceiver(bluetoothDeviceSearcher);
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesserVer2", "StopSearchDevices", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }
}
