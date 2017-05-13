package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.futsal.manager.BluetoothModule.BluetoothCommunication;
import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.MakeNewMemoryModule.MakeNewMemoryManager;
import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 3. 27..
 */

public class BluetoothDeviceFinder extends Activity {

    ListView listOfBluetoothDevices;
    ArrayAdapter listViewArrayAdapter;
    BluetoothAdapter deviceBluetoothAdapter;
    BluetoothCommunication bluetoothCommunication;
    BluetoothDeviceFinderProcesser bluetoothDeviceFinderProcesser;
    Button btnRefresh;
    final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_finder);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        listOfBluetoothDevices = (ListView) findViewById(R.id.listOfBluetoothDevices);

        listViewArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        bluetoothDeviceFinderProcesser = new BluetoothDeviceFinderProcesser(this);

        listOfBluetoothDevices.setAdapter(listViewArrayAdapter);

        bluetoothDeviceFinderProcesser.SetListViewArrayAdapter(listViewArrayAdapter);
        bluetoothDeviceFinderProcesser.InitBluetooth();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogManager.PrintLog("BluetoothDeviceFinder", "onClick", "Refresh Button Pressed", DefineManager.LOG_LEVEL_INFO);
                Snackbar.make(view, "Developing~ ^^", Snackbar.LENGTH_SHORT).show();
            }
        });

        listOfBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int clickedItemPosition, long l) {

                String selectedDeviceAddress = adapterView.getItemAtPosition(clickedItemPosition).toString().split("\n")[1];
                //Log.d(bluetoothManagerLogCatTag, "item: " + selectedDeviceAddress);
                LogManager.PrintLog("BluetoothDeviceFinder", "onItemClick", "Selected Device Address: " + selectedDeviceAddress, DefineManager.LOG_LEVEL_INFO);
                //deviceBluetoothAdapter.cancelDiscovery();
                bluetoothCommunication = new BluetoothCommunication(null, selectedDeviceAddress);
                Intent startRealService = new Intent(getApplicationContext(), MakeNewMemoryManager.class);
                startRealService.putExtra("bluetoothDeviceData", bluetoothCommunication);
                //BLUETOOTH_COMMUNICATION_TEMP = bluetoothCommunication;
                startActivity(startRealService);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK) {
                    //Log.d(bluetoothManagerLogCatTag, "ok rdy to use bluetooth");
                    LogManager.PrintLog("BluetoothDeviceFinder", "onActivityResult", "ok rdy to use bluetooth", DefineManager.LOG_LEVEL_INFO);
                    //ScanBluetoothDevices();
                }
                else {
                    //Log.d(bluetoothManagerLogCatTag, "god damn, go away!");
                    LogManager.PrintLog("BluetoothDeviceFinder", "onActivityResult", "god damn, go away!", DefineManager.LOG_LEVEL_ERROR);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
