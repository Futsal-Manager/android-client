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
import android.widget.TextView;

import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import java.util.Set;

import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST;
import static com.futsal.manager.DefineManager.ENABLE_BLUETOOTH_MODULE_USER_ACCESS_ACCEPT;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class EmbeddedSystemFinderProcesser {

    Activity embeddedSystemFinder;
    TextView txtWaitStatus;

    public EmbeddedSystemFinderProcesser(Activity embeddedSystemFinder) {
        this.embeddedSystemFinder = embeddedSystemFinder;
    }

    public EmbeddedSystemFinderProcesser(Activity embeddedSystemFinder, TextView txtWaitStatus) {
        this.embeddedSystemFinder = embeddedSystemFinder;
        this.txtWaitStatus = txtWaitStatus;
    }

    void InitBluetoothFinder() {

        EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();

        if(EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER != null) {
            if(EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.isEnabled()) {
                UpdateTextOfProcessStatus("Getting Paired Device.");
                GetPairedDevice();
                UpdateTextOfProcessStatus("Searching Devices.");
                SearchBluetoothDevice();
            }
            else {
                EnableBluetoothModule();
            }
        }
        else {
            ShowWarningDialog();
        }
    }

    public void UpdateTextOfProcessStatus(String message) {
        txtWaitStatus.setText(message);
    }

    void SearchBluetoothDevice() {
        LogManager.PrintLog("EmbeddedSystemFinderProcesser", "SearchBluetoothDevice", "Try search ble", LOG_LEVEL_DEBUG);
        IntentFilter bluetoothDeviceSearcherRegister = new IntentFilter();

        bluetoothDeviceSearcherRegister.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothDeviceSearcherRegister.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        bluetoothDeviceSearcherRegister.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        embeddedSystemFinder.registerReceiver(bluetoothDeviceSearcher, bluetoothDeviceSearcherRegister);
        EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.startDiscovery();
    }

    void EnableBluetoothModule() {
        LogManager.PrintLog("EmbeddedSystemFinderProcesser", "EnableBluetoothModule", "Try enable ble", LOG_LEVEL_DEBUG);
        Intent enableBluetoothModuleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //enableBluetoothModuleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        embeddedSystemFinder.startActivityForResult(enableBluetoothModuleIntent, ENABLE_BLUETOOTH_MODULE_USER_ACCESS_ACCEPT);
    }

    void ShowWarningDialog() {
        try {
            AlertDialog.Builder bluetoothNotAvailableDialogBuilder = new AlertDialog.Builder(embeddedSystemFinder);
            bluetoothNotAvailableDialogBuilder.setMessage(embeddedSystemFinder.getString(R.string.notAvailalbeMessage));
            bluetoothNotAvailableDialogBuilder.setPositiveButton(R.string.okIwillBuyNewProduct, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    embeddedSystemFinder.finish();
                }
            });
            AlertDialog bluetoothModuleInitFailMessage = bluetoothNotAvailableDialogBuilder.create();
            bluetoothModuleInitFailMessage.show();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "ShowWarningDialog", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public BroadcastReceiver bluetoothDeviceSearcher = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String bluetoothSearchAction = intent.getAction();
            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "onReceive", "Action: " + bluetoothSearchAction, LOG_LEVEL_INFO);

            if(BluetoothDevice.ACTION_FOUND.equals(bluetoothSearchAction)) {
                BluetoothDevice eachBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothDeviceItemModel bluetoothDeviceItemModel;

                String deviceName, deviceAddress;
                deviceName = eachBluetoothDevice.getName();
                deviceAddress = eachBluetoothDevice.getAddress();

                LogManager.PrintLog("EmbeddedSystemFinderProcesser", "onReceive", "name: " + deviceName + " address: " + deviceAddress, LOG_LEVEL_DEBUG);

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
                EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.add(bluetoothDeviceItemModel);
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(bluetoothSearchAction)) {
                if(EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.isEmpty()) {
                    LogManager.PrintLog("EmbeddedSystemFinderProcesser", "onReceive", "Cannot found any bluetooth device", LOG_LEVEL_WARN);
                }
                else {
                    LogManager.PrintLog("EmbeddedSystemFinderProcesser", "onReceive", "Ok we found some devices", LOG_LEVEL_DEBUG);
                }
                UpdateTextOfProcessStatus("Ok, just wait a sec.");
                UselessDelay(1000);
                MoveToDeviceSelectManager();
            }
        }
    };

    void MoveToDeviceSelectManager() {
        LogManager.PrintLog("EmbeddedSystemFinderProcesser", "MoveToDeviceSelectManager", "am i moving?", LOG_LEVEL_DEBUG);
        Intent serverSavedVideoListLayout = new Intent(embeddedSystemFinder, BluetoothDeviceSelector.class);
        serverSavedVideoListLayout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        embeddedSystemFinder.startActivity(serverSavedVideoListLayout);
        embeddedSystemFinder.finish();
    }

    void UselessDelay(int delyTime) {
        try {
            Thread.sleep(delyTime);
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "UselessDelay", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public void StopSearchDevices() {
        try {
            embeddedSystemFinder.unregisterReceiver(bluetoothDeviceSearcher);
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "StopSearchDevices", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    void GetPairedDevice() {
        Set<BluetoothDevice> pairedDeviceList = EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.getBondedDevices();
        for(BluetoothDevice eachPairedDevice : pairedDeviceList) {

            String deviceName, deviceAddress;
            deviceName = eachPairedDevice.getName();
            deviceAddress = eachPairedDevice.getAddress();

            BluetoothDeviceItemModel bluetoothDeviceItemModel = new BluetoothDeviceItemModel(deviceName, deviceAddress, true);

            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "GetPairedDevice", "name: " + deviceName + " address: " + deviceAddress, LOG_LEVEL_DEBUG);
            EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.add(bluetoothDeviceItemModel);
        }
    }
}
