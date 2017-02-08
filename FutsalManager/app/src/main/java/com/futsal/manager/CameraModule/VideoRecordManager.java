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

public class VideoRecordManager extends Activity{
    ToggleButton toggleRecordVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface);

        toggleRecordVideo = (ToggleButton)findViewById(R.id.toggleRecordVideo);

        toggleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkStatus) {
                if(checkStatus) {
                    Log.d(getString(R.string.videoRecordManagerLogCatTag), "true");
                }
                else {
                    Log.d(getString(R.string.videoRecordManagerLogCatTag), "false");
                }
            }
        });
    }
}
