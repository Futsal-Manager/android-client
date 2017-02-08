package com.futsal.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.futsal.manager.CameraModule.VideoRecordManager;

public class FutsalManagerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futsal_manager_main);

        Intent recordVideoLayout = new Intent(FutsalManagerMain.this, VideoRecordManager.class);
        startActivity(recordVideoLayout);
    }
}
