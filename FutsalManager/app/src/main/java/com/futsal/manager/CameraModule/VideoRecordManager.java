package com.futsal.manager.CameraModule;

import android.app.Activity;
import android.os.Bundle;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 2. 9..
 */

public class VideoRecordManager extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface);
    }
}
