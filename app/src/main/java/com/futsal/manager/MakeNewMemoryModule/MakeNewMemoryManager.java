package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.EmbeddedCommunicationModule.EmbeddedSystemFinderVer2;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE_SOCKET;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;

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
    boolean isSettingShowed, isRecording, isCameraResolutionSettingOpen;
    SurfaceView surfaceRecordVideo;
    MakeNewMemoryManagerProcesser makeNewMemoryManagerProcesser;
    MakeNewMemorySettingManager makeNewMemorySettingManager;
    TextView txtRecordingTime;
    RelativeLayout layoutCameraSetting;
    ListView listOfCameraResolution;
    List<String> listOfAvailableCameraResolution;
    ArrayAdapter<String> cameraResolutionListAdapter;
    View layoutCameraSettingView;
    EmbeddedSystemFinderVer2 embeddedSystemFinderVer2;
    Activity makeNewMemoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_memory_manager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        InitLayout();

        btnImageRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EMBEDDED_SYSTEM_DEVICE_SOCKET != null) {
                    isRecording = !isRecording;
                    if(isRecording) {
                        //btnImageRecord.setBackgroundResource(R.drawable.shape_2_copy_1);
                        btnImageRecord.setImageResource(R.drawable.shape_2_copy_1);
                        txtRecordingTime.setTextColor(getResources().getColor(R.color.red));
                        LogManager.PrintLog("MakeNewMemoryManager", "onCreate", "start recording", DefineManager.LOG_LEVEL_INFO);
                        makeNewMemoryManagerProcesser.StartRecording();
                        UselessDelay(1000);
                    }
                    else {
                        //btnImageRecord.setBackgroundResource(R.drawable.shape_2_copy_3);
                        txtRecordingTime.setTextColor(getResources().getColor(R.color.white));
                        btnImageRecord.setImageResource(R.drawable.shape_2_copy_3);
                        makeNewMemoryManagerProcesser.StopRecording();
                    }
                }
                else {
                    embeddedSystemFinderVer2.show();
                    LogManager.PrintLog("MakeNewMemoryManager", "onCreate", "Embedded system not connected", LOG_LEVEL_WARN);
                }
            }
        });

        btnImagePictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenVideoListGallery();
            }
        });

        btnImageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCameraResolutionSettingOpen != true) {
                    isCameraResolutionSettingOpen = !isCameraResolutionSettingOpen;
                    layoutCameraSetting.setVisibility(View.VISIBLE);
                }
            }
        });

        btnImageSetting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!isRecording) {
                    //btnImageSetting.setBackgroundResource(R.drawable.after_setting);
                    CloseSettingMenuChecker();
                    makeNewMemorySettingManager.show();
                }
                return false;
            }
        });

        layoutCameraSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCameraResolutionSettingOpen) {
                    isCameraResolutionSettingOpen = !isCameraResolutionSettingOpen;
                    layoutCameraSetting.setVisibility(View.INVISIBLE);
                }
            }
        });

        layoutCameraSettingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    void CloseSettingMenuChecker() {
        if(makeNewMemorySettingManager.isShowing()) {
            makeNewMemorySettingManager.dismiss();
        }
    }

    void OpenVideoListGallery() {
        try {
            String openPath = makeNewMemoryManagerProcesser.GetFilePath();
/*
            Intent videoGalleryOpen =new Intent();
            //videoGalleryOpen.setType("video/mp4");
            videoGalleryOpen.setAction(Intent.ACTION_VIEW);
            videoGalleryOpen.setDataAndType(Uri.fromFile(new File(openPath)), "image/*");
            startActivity(videoGalleryOpen);*/

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(Uri.fromFile(new File(openPath)), "video/mp4");
            startActivity(Intent.createChooser(intent, "Open Video"));
        }
        catch (Exception err) {
            LogManager.PrintLog("MakeNewMemoryManager", "OpenVideoListGallery", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    void InitLayout() {
        isSettingShowed = false;
        isRecording = false;
        isCameraResolutionSettingOpen = false;
        makeNewMemoryManager = this;

        btnImageRecord = (ImageButton) findViewById(R.id.btnImageRecord);
        btnImagePictures = (ImageButton) findViewById(R.id.btnImagePictures);
        btnImageSetting = (ImageButton) findViewById(R.id.btnImageSetting);
        surfaceRecordVideo = (SurfaceView)findViewById(R.id.surfaceRecordVideo);
        txtRecordingTime = (TextView) findViewById(R.id.txtRecordingTime);
        layoutCameraSetting = (RelativeLayout)findViewById(R.id.layoutCameraSetting);
        listOfCameraResolution = (ListView)findViewById(R.id.listOfCameraResolution);
        layoutCameraSettingView = (View) findViewById(R.id.layoutCameraSettingView);

        layoutCameraSetting.setVisibility(View.INVISIBLE);

        makeNewMemoryManagerProcesser = new MakeNewMemoryManagerProcesser(this, surfaceRecordVideo, txtRecordingTime);
        makeNewMemorySettingManager = new MakeNewMemorySettingManager(this, btnImageSetting, this);
        listOfAvailableCameraResolution = new ArrayList<String>();
        embeddedSystemFinderVer2 = new EmbeddedSystemFinderVer2(makeNewMemoryManager);

        listOfAvailableCameraResolution = makeNewMemoryManagerProcesser.GetAvailableCameraResolution();

        cameraResolutionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listOfAvailableCameraResolution);
        listOfCameraResolution.setAdapter(cameraResolutionListAdapter);
    }

    @Override
    protected void onDestroy() {
        makeNewMemoryManagerProcesser.StopProcess();
        super.onDestroy();
    }

    void UselessDelay(int delayTime) {
        try {
            Thread.sleep(delayTime);
        }
        catch (Exception err) {
            LogManager.PrintLog("MakeNewMemoryManager", "UselessDelay", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }
}
