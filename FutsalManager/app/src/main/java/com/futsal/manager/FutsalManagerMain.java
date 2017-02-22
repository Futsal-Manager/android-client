package com.futsal.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.futsal.manager.CameraModule.VideoRecordBasedOnOpenCV;

import org.opencv.android.OpenCVLoader;

public class FutsalManagerMain extends AppCompatActivity {

    static final String logCatTag = "MainActivity";
    Button btnGoToCamera, btnGoToBluetooth;

    static {
        if(OpenCVLoader.initDebug()) {
            Log.d(logCatTag, "OpenCV Loaded");
        }
        else {
            Log.d(logCatTag, "OpenCV not Loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futsal_manager_main);

        btnGoToCamera = (Button) findViewById(R.id.btnGoToCamera);
        btnGoToBluetooth = (Button) findViewById(R.id.btnGoToBluetooth);

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

            }
        });


    }
}
