package com.futsal.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.futsal.manager.CameraModule.VideoRecordBasedOnOpenCV;

import org.opencv.android.OpenCVLoader;

public class FutsalManagerMain extends AppCompatActivity {

    static final String logCatTag = "MainActivity";

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

        Intent recordVideoLayout = new Intent(FutsalManagerMain.this, VideoRecordBasedOnOpenCV.class);
        startActivity(recordVideoLayout);
    }
}
