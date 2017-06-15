package com.futsal.manager.EmbeddedCommunicationModule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.DEVICE_CONNECTED;
import static com.futsal.manager.DefineManager.DEVICE_NOT_CONNECTED;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE_SOCKET;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.NEW_DEVICE_FOUNDED;

/**
 * Created by stories2 on 2017. 6. 14..
 */

public class EmbeddedSystemFinderVer2 extends Dialog {

    TextView txtBluetoothStatus;
    ListView listOfBluetoothDevices;
    EmbeddedSystemFinderProcesserVer2 embeddedSystemFinderProcesserVer2;
    Activity makeNewMemoryManager;
    ArrayAdapter<String> listOfEmbeddedSystem;
    String[] listOfEmbeddedSystemArray;
    BluetoothDeviceSelectorProcesser bluetoothDeviceSelectorProcesser;

    public EmbeddedSystemFinderVer2(Activity makeNewMemoryManager) {
        super(makeNewMemoryManager);
        this.makeNewMemoryManager = makeNewMemoryManager;
    }

    public EmbeddedSystemFinderVer2(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EmbeddedSystemFinderVer2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        try {
            embeddedSystemFinderProcesserVer2.StopSearchDevices();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderVer2", "dismiss", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.embedded_system_finder_ver2);

        InitLayout();

        LogManager.PrintLog("EmbeddedSystemFinderVer2", "onCreate", "EmbeddedSystemFinderVer2 opened", LOG_LEVEL_INFO);

        listOfBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDeviceItemModel bluetoothDeviceItemModel = EMBEDDED_SYSTEM_BLUETOOTH_DEVICE_LIST.get(position);
                LogManager.PrintLog("EmbeddedSystemFinderVer2", "onItemClick", "selected item info: " + bluetoothDeviceItemModel.GetDeviceName() + " " + bluetoothDeviceItemModel.GetDeviceMacAddress(), LOG_LEVEL_INFO);
                bluetoothDeviceSelectorProcesser.TryConnectToTargetDevice(bluetoothDeviceItemModel);
            }
        });
    }

    void InitLayout() {

        txtBluetoothStatus = (TextView)findViewById(R.id.txtBluetoothStatus);
        listOfBluetoothDevices = (ListView) findViewById(R.id.listOfBluetoothDevices);

        embeddedSystemFinderProcesserVer2 = new EmbeddedSystemFinderProcesserVer2(makeNewMemoryManager, bluetoothDeviceFinderLayoutHandler);
        bluetoothDeviceSelectorProcesser = new BluetoothDeviceSelectorProcesser(makeNewMemoryManager, bluetoothDeviceFinderLayoutHandler);

        if(EMBEDDED_SYSTEM_DEVICE_SOCKET != null) {
            try {
                EMBEDDED_SYSTEM_DEVICE_SOCKET.close();
            }
            catch (Exception err) {
                LogManager.PrintLog("EmbeddedSystemFinderVer2", "InitLayout", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
            EMBEDDED_SYSTEM_DEVICE_SOCKET = null;
        }

        embeddedSystemFinderProcesserVer2.InitBluetoothFinder();
    }

    Handler bluetoothDeviceFinderLayoutHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEW_DEVICE_FOUNDED:
                    listOfEmbeddedSystemArray = embeddedSystemFinderProcesserVer2.GetFoundDeviceStringArray();
                    SetListView();
                    LogManager.PrintLog("EmbeddedSystemFinderVer2", "handleMessage", "new device founded", LOG_LEVEL_INFO);
                    break;
                case DEVICE_CONNECTED:
                    dismiss();
                    break;
                case DEVICE_NOT_CONNECTED:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void SetListView() {
        listOfEmbeddedSystem = new ArrayAdapter<String>(makeNewMemoryManager, android.R.layout.simple_list_item_1, android.R.id.text1, listOfEmbeddedSystemArray);
        listOfBluetoothDevices.setAdapter(listOfEmbeddedSystem);
    }

    public void BluetoothSearchingProcess() {
        try {
            embeddedSystemFinderProcesserVer2.SearchBluetoothDevice();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderVer2", "BluetoothSearchingProcess", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public void ShowWarningDialog() {
        try {
            embeddedSystemFinderProcesserVer2.ShowWarningDialog();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderVer2", "ShowWarningDialog", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }
}
