package com.futsal.manager.EmbeddedCommunicationModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.MakeNewMemoryModule.MakeNewMemoryManager;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class BluetoothDeviceSelector extends Activity {

    ListView listOfBluetoothDevices;
    ArrayAdapter<String> eachListOfBluetoothDevicesAdapter;
    String[] availableDeviceList;
    Button btnResearch, btnSkip;
    BluetoothDeviceSelectorProcesser bluetoothDeviceSelectorProcesser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_selector);

        InitLayout();

        listOfBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemInfo = (String) listOfBluetoothDevices.getItemAtPosition(position);
                LogManager.PrintLog("BluetoothDeviceSelector", "onItemClick", "Selected: " + selectedItemInfo, LOG_LEVEL_DEBUG);
                if(EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST != null) {
                    bluetoothDeviceSelectorProcesser.TryConnectToTargetDevice(EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.get(position));
                }
                else {
                    LogManager.PrintLog("BluetoothDeviceSelector", "onItemClick", "N/A Data", LOG_LEVEL_WARN);
                }
            }
        });

        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent researchBluetoothDevice = new Intent(getApplicationContext(), EmbeddedSystemFinder.class);
                startActivity(researchBluetoothDevice);
                finish();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMakeNewMemory = new Intent(getApplicationContext(), MakeNewMemoryManager.class);
                startActivity(startMakeNewMemory);
                finish();
            }
        });
    }

    void InitLayout() {
        availableDeviceList = GetFoundDeviceStringArray();

        listOfBluetoothDevices = (ListView)findViewById(R.id.listOfBluetoothDevices);
        btnResearch = (Button)findViewById(R.id.btnResearch);
        btnSkip = (Button)findViewById(R.id.btnSkip);
        eachListOfBluetoothDevicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, availableDeviceList);
        bluetoothDeviceSelectorProcesser = new BluetoothDeviceSelectorProcesser(this);

        listOfBluetoothDevices.setAdapter(eachListOfBluetoothDevicesAdapter);
    }

    String[] GetFoundDeviceStringArray() {
        if(EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST != null) {
            int length = EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.size(), i = 0;
            String[] foundedDeviceList = new String[length];
            for(BluetoothDeviceItemModel eachBluetoothDevice : EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST) {
                String eachDeviceInfo = eachBluetoothDevice.GetDeviceName() + "\n" + eachBluetoothDevice.GetDeviceMacAddress() + "\n";
                if(eachBluetoothDevice.GetIsAlreadyPaired()) {
                    eachDeviceInfo += "*";
                }
                foundedDeviceList[i] = eachDeviceInfo;
                i += 1;
            }
            return foundedDeviceList;

        }
        return new String[0];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
