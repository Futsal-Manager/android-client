package com.futsal.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.futsal.manager.BluetoothModule.BluetoothManager;
import com.futsal.manager.CameraModule.VideoRecordBasedOnOpenCV;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationTester;

import org.opencv.android.OpenCVLoader;

public class FutsalManagerMain extends AppCompatActivity {

    Button btnGoToCamera, btnGoToBluetooth, btnGoToNetwork;

    static {
        if(OpenCVLoader.initDebug()) {
            //Log.d(logCatTag, "OpenCV Loaded");
            LogManager.PrintLog("FutsalManagerMain", "static", "OpenCV Loaded", DefineManager.LOG_LEVEL_INFO);
        }
        else {
            //Log.d(logCatTag, "OpenCV not Loaded");
            LogManager.PrintLog("FutsalManagerMain", "static", "OpenCV not Loaded", DefineManager.LOG_LEVEL_WARN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futsal_manager_main);

        btnGoToCamera = (Button) findViewById(R.id.btnGoToCamera);
        btnGoToBluetooth = (Button) findViewById(R.id.btnGoToBluetooth);
        btnGoToNetwork = (Button) findViewById(R.id.btnGoToNetwork);

        btnGoToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recordVideoLayout = new Intent(FutsalManagerMain.this, VideoRecordBasedOnOpenCV.class);
                startActivity(recordVideoLayout);
            }
        });

        btnGoToBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bluetoothManagerLayout = new Intent(FutsalManagerMain.this, BluetoothManager.class);
                startActivity(bluetoothManagerLayout);
            }
        });

        btnGoToNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent networkManagerLayout = new Intent(FutsalManagerMain.this, CommunicationTester.class);
                startActivity(networkManagerLayout);
            }
        });
    }
}
