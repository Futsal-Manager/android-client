package com.futsal.manager.CameraModule;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 2. 9..
 */

public class VideoRecordManager extends Activity {
    String videoRecordManagerLogCatTag;
    ToggleButton toggleRecordVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface);
        videoRecordManagerLogCatTag = getString(R.string.videoRecordManagerLogCatTag);
        Log.d(videoRecordManagerLogCatTag, "onCreate");

        toggleRecordVideo = (ToggleButton)findViewById(R.id.toggleRecordVideo);

        toggleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkStatus) {
                if(checkStatus) {
                    Log.d(videoRecordManagerLogCatTag, "true");
                }
                else {
                    Log.d(videoRecordManagerLogCatTag, "false");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(videoRecordManagerLogCatTag, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(videoRecordManagerLogCatTag, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(videoRecordManagerLogCatTag, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(videoRecordManagerLogCatTag, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(videoRecordManagerLogCatTag, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(videoRecordManagerLogCatTag, "onDestroy");
    }
}
