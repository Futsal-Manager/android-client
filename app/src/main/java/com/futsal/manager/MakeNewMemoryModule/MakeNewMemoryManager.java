package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import org.opencv.android.OpenCVLoader;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class MakeNewMemoryManager extends Activity {

    static {
        if(OpenCVLoader.initDebug()) {
            //Log.d(logCatTag, "OpenCV Loaded");
            LogManager.PrintLog("MakeNewMemoryManager", "static", "OpenCV Loaded", DefineManager.LOG_LEVEL_INFO);
        }
        else {
            //Log.d(logCatTag, "OpenCV not Loaded");
            LogManager.PrintLog("MakeNewMemoryManager", "static", "OpenCV not Loaded", DefineManager.LOG_LEVEL_WARN);
        }
    }

    ImageButton btnImageRecord, btnImagePictures, btnImageSetting;
    boolean isSettingShowed, isRecording;
    SurfaceView surfaceRecordVideo;
    MakeNewMemoryManagerProcesser makeNewMemoryManagerProcesser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_memory_manager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        InitLayout();

        btnImageRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                if(isRecording) {
                    btnImageRecord.setBackgroundResource(R.drawable.after_record);
                    makeNewMemoryManagerProcesser.StartRecording();
                }
                else {
                    btnImageRecord.setBackgroundResource(R.drawable.before_record);
                    makeNewMemoryManagerProcesser.StopRecording();
                }
            }
        });

        btnImagePictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnImageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSettingShowed = !isSettingShowed;
                if(isSettingShowed) {
                    btnImageSetting.setBackgroundResource(R.drawable.after_setting);
                }
                else {
                    btnImageSetting.setBackgroundResource(R.drawable.before_setting);
                }
            }
        });
    }

    void InitLayout() {
        isSettingShowed = false;
        isRecording = false;

        btnImageRecord = (ImageButton) findViewById(R.id.btnImageRecord);
        btnImagePictures = (ImageButton) findViewById(R.id.btnImagePictures);
        btnImageSetting = (ImageButton) findViewById(R.id.btnImageSetting);
        surfaceRecordVideo = (SurfaceView)findViewById(R.id.surfaceRecordVideo);

        makeNewMemoryManagerProcesser = new MakeNewMemoryManagerProcesser(this, surfaceRecordVideo);
    }

    @Override
    protected void onDestroy() {
        makeNewMemoryManagerProcesser.StopProcess();
        super.onDestroy();
    }

}
