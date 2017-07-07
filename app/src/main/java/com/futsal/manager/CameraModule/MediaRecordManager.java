package com.futsal.manager.CameraModule;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.R;

/**
 * Created by stori on 2017-02-24.
 */

public class MediaRecordManager extends Activity implements SurfaceHolder.Callback{

    final String mediaRecordLogCatTag = "media Record";

    ToggleButton toggleRecordVideo;
    SurfaceView surfaceRecordVideo;
    SurfaceHolder surfaceHolderRecordVideo;
    MediaRecorder mediaRecording;

    @Override

    // Todo: 퍼포먼스 개선은 paging
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_media_surface);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        toggleRecordVideo = (ToggleButton) findViewById(R.id.toogleRecordVideo);
        surfaceRecordVideo = (SurfaceView) findViewById(R.id.surfaceRecordVideo);

        surfaceHolderRecordVideo = surfaceRecordVideo.getHolder();
        surfaceHolderRecordVideo.addCallback(this);
        surfaceHolderRecordVideo.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        toggleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(mediaRecording == null) {
                        String savePath = Environment.getExternalStorageDirectory().toString();
                        mediaRecording = new MediaRecorder();
                        mediaRecording.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecording.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        mediaRecording.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        mediaRecording.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mediaRecording.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

                        mediaRecording.setPreviewDisplay(surfaceHolderRecordVideo.getSurface());
                        mediaRecording.setOutputFile(savePath + "/testVideo2.mp4");

                        try {
                            mediaRecording.prepare();
                            mediaRecording.start();
                        }
                        catch (Exception err) {
                            Log.d(mediaRecordLogCatTag, "Error in setOnCheckedChangeListener: " + err.getMessage());
                            mediaRecording.release();
                            mediaRecording = null;
                        }
                    }
                    Log.d(mediaRecordLogCatTag, "checked");
                }
                else {
                    if(mediaRecording == null) {
                        return;
                    }
                    mediaRecording.stop();
                    mediaRecording.reset();
                    mediaRecording.release();
                    mediaRecording = null;
                    Log.d(mediaRecordLogCatTag, "unchecked");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(getString(R.string.app_name), "Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
