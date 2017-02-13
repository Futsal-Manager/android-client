package com.futsal.manager.CameraModule;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 2. 9..
 */

public class VideoRecordManager extends Activity implements SurfaceHolder.Callback{
    final String videoSavePath = "/storage/emulated/DCIM/test.mp4";

    String videoRecordManagerLogCatTag;
    ToggleButton toggleRecordVideo;
    SurfaceView surfaceRecordVideo;
    SurfaceHolder surfaceHolderRecordVideo;
    Camera phoneDeviceCamera;
    MediaRecorder deviceMediaRecorderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface);
        videoRecordManagerLogCatTag = getString(R.string.videoRecordManagerLogCatTag);
        Log.d(videoRecordManagerLogCatTag, "onCreate");

        toggleRecordVideo = (ToggleButton)findViewById(R.id.toggleRecordVideo);
        surfaceRecordVideo = (SurfaceView)findViewById(R.id.surfaceRecordVideo);

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
                        InitRecordVideo(surfaceHolderRecordVideo.getSurface(), phoneDeviceCamera, deviceMediaRecorderManager, videoSavePath);
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
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(videoRecordManagerLogCatTag, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(videoRecordManagerLogCatTag, "surfaceDestroyed");
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

    public boolean InitRecordVideo(Surface cameraViewSurface, Camera phoneCamera, MediaRecorder mediaRecorderManager, String pathOfStoreVideoFile) {
        try {
            if(phoneCamera == null) {
                phoneCamera = Camera.open();
                phoneCamera.unlock();
            }

            if(mediaRecorderManager == null) {
                mediaRecorderManager = new MediaRecorder();
            }
            mediaRecorderManager.setPreviewDisplay(cameraViewSurface);
            mediaRecorderManager.setCamera(phoneCamera);

            mediaRecorderManager.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            mediaRecorderManager.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorderManager.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorderManager.setVideoEncodingBitRate(512 * 1000);
            mediaRecorderManager.setVideoFrameRate(24);
            mediaRecorderManager.setVideoSize(1280, 720);
            mediaRecorderManager.setOutputFile(pathOfStoreVideoFile);

            mediaRecorderManager.prepare();
            return true;
        }
        catch (Exception err) {
            Log.d(videoRecordManagerLogCatTag, "Error in InitRecordVideo: " + err.getMessage());
        }
        return false;
    }
}
