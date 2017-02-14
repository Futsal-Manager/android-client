package com.futsal.manager.CameraModule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.R;

import java.io.File;

/**
 * Created by stories2 on 2017. 2. 9..
 */

public class VideoRecordManager extends Activity implements SurfaceHolder.Callback{
    String videoSavePath;
    final int CAMERA_PERMISSION_REQUEST_CODE = 1, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2,
            WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;

    String videoRecordManagerLogCatTag;
    ToggleButton toggleRecordVideo;
    SurfaceView surfaceRecordVideo;
    SurfaceHolder surfaceHolderRecordVideo;
    Camera phoneDeviceCamera;
    MediaRecorder deviceMediaRecorderManager;
    File mediaSaveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface);
        videoRecordManagerLogCatTag = getString(R.string.videoRecordManagerLogCatTag);
        Log.d(videoRecordManagerLogCatTag, "onCreate");

        toggleRecordVideo = (ToggleButton)findViewById(R.id.toggleRecordVideo);
        surfaceRecordVideo = (SurfaceView)findViewById(R.id.surfaceRecordVideo);

        mediaSaveFile = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        videoSavePath = mediaSaveFile.getPath() + "/test.mp4";
        //videoSavePath = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "test.mp4";
        Log.d(videoRecordManagerLogCatTag, "video save path: " + videoSavePath);

        RotateScreenLANDSCAPE();
        surfaceHolderRecordVideo = InitSurfaceView(surfaceRecordVideo, surfaceHolderRecordVideo);

        toggleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkStatus) {
                if(checkStatus) {
                    Log.d(videoRecordManagerLogCatTag, "true");
                    if(deviceMediaRecorderManager != null) {
                        deviceMediaRecorderManager.start();
                    }
                }
                else {
                    Log.d(videoRecordManagerLogCatTag, "false");
                    if(deviceMediaRecorderManager != null) {
                        deviceMediaRecorderManager.stop();
                        deviceMediaRecorderManager.reset();
                    }
                    try {
                        InitRecordVideo(surfaceHolderRecordVideo.getSurface(), phoneDeviceCamera, videoSavePath);
                    }
                    catch (Exception err) {
                        Log.d(videoRecordManagerLogCatTag, "Error in setOnCheckedChangeListener: " + err.getMessage());
                    }
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(videoRecordManagerLogCatTag, "surfaceCreated");
        try {
            InitDeviceCamera();
            InitRecordVideo(surfaceHolderRecordVideo.getSurface(), phoneDeviceCamera, videoSavePath);
        }
        catch (Exception err) {
            Log.d(videoRecordManagerLogCatTag, "Error in surfaceCreated: " + err.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(videoRecordManagerLogCatTag, "surfaceChanged");
        if(surfaceHolderRecordVideo.getSurface() == null) {
            return;
        }

        try {
            phoneDeviceCamera.stopPreview();

            // 프리뷰 변경, 처리 등을 여기서 해준다.
            Camera.Parameters parameters = phoneDeviceCamera.getParameters();
            Camera.Size size = GetBestPreviewSize(width, height);
            parameters.setPreviewSize(size.width, size.height);
            phoneDeviceCamera.setParameters(parameters);

            phoneDeviceCamera.setPreviewDisplay(surfaceHolderRecordVideo);
            phoneDeviceCamera.startPreview();
        }
        catch (Exception err) {
            Log.d(videoRecordManagerLogCatTag, "Error in surfaceChanged: " + err.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(videoRecordManagerLogCatTag, "surfaceDestroyed");
        ShutdownProcess();
    }

    public void RotateScreenLANDSCAPE() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public SurfaceHolder InitSurfaceView(SurfaceView targetSurfaceView, SurfaceHolder targetSurfaceHolder) {
        targetSurfaceHolder = targetSurfaceView.getHolder();
        targetSurfaceHolder.addCallback(this);
        targetSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return targetSurfaceHolder;
    }

    public boolean InitRecordVideo(Surface cameraViewSurface, Camera phoneCamera, String pathOfStoreVideoFile) {
        try {
            if(phoneCamera == null) {
                if(IsCameraPermissionAvailable()) {// && IsStorageReadPermissionAvailable() && IsStorageWritePermissionAvailable()
                    //phoneCamera = Camera.open(0);
                    phoneCamera.unlock();
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                }
            }

            if(deviceMediaRecorderManager == null) {
                deviceMediaRecorderManager = new MediaRecorder();
            }
            deviceMediaRecorderManager.setPreviewDisplay(cameraViewSurface);
            deviceMediaRecorderManager.setCamera(phoneCamera);

            deviceMediaRecorderManager.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            deviceMediaRecorderManager.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            deviceMediaRecorderManager.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            deviceMediaRecorderManager.setVideoEncodingBitRate(512 * 1000);
            deviceMediaRecorderManager.setVideoFrameRate(24);
            deviceMediaRecorderManager.setVideoSize(1280, 720);
            deviceMediaRecorderManager.setOutputFile(pathOfStoreVideoFile);

            deviceMediaRecorderManager.prepare();
            return true;
        }
        catch (Exception err) {
            Log.d(videoRecordManagerLogCatTag, "Error in InitRecordVideo: " + err.getMessage());
        }
        return false;
    }

    public boolean IsCameraPermissionAvailable() {
        int permissionStatusResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return permissionStatusResult == PackageManager.PERMISSION_GRANTED;
    }

    public boolean IsStorageReadPermissionAvailable() {
        int permissionStatusResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionStatusResult == PackageManager.PERMISSION_GRANTED;
    }

    public boolean IsStorageWritePermissionAvailable() {
        int permissionStatusResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionStatusResult == PackageManager.PERMISSION_GRANTED;
    }

    public void InitDeviceCamera() {
        try {
            if(IsCameraPermissionAvailable()) {
                Log.d(videoRecordManagerLogCatTag, "how many camera can i use?: " + Camera.getNumberOfCameras());
                phoneDeviceCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }

            Camera.Parameters deviceCamerParameters = phoneDeviceCamera.getParameters();
            deviceCamerParameters.set("orientation", "landscape");
            phoneDeviceCamera.setDisplayOrientation(0);
            deviceCamerParameters.setRotation(0);

            phoneDeviceCamera.setParameters(deviceCamerParameters);
            phoneDeviceCamera.setPreviewDisplay(surfaceHolderRecordVideo);
            phoneDeviceCamera.startPreview();
        }
        catch (Exception err) {
            Log.d(videoRecordManagerLogCatTag, "Error in InitDeviceCamera: " + err.getMessage());
        }
    }

    private Camera.Size GetBestPreviewSize(int width, int height)
    {
        Camera.Size result=null;
        Camera.Parameters p = phoneDeviceCamera.getParameters();
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                } else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return result;
    }

    public void ShutdownProcess() {
        try {
            deviceMediaRecorderManager.reset();
            deviceMediaRecorderManager.release();
            phoneDeviceCamera.release();
        }
        catch (Exception err) {
            Log.d(videoRecordManagerLogCatTag, "Error in ShutdownProcess: " + err.getMessage());
        }
    }
}
