package com.futsal.manager.EmbeddedCommunicationModule;

import android.app.Activity;
import android.os.Bundle;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class BluetoothDeviceSelector extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_selector);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
