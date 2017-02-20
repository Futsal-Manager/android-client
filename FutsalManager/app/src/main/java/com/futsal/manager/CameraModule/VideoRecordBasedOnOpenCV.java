package com.futsal.manager.CameraModule;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.R;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_MPEG4;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_RGB24;

/**
 * Created by stories2 on 2017. 2. 19..
 */

public class VideoRecordBasedOnOpenCV extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{

    static final String videoRecordBasedOnOpencvTag = "video with opencv";

    JavaCameraView opencvCameraView;
    BaseLoaderCallback opencvBaseLoaderCallback;
    Mat eachCameraFrameImage;
    ToggleButton toogleRecordVideo;
    FFmpegFrameRecorder deviceVideoFrameRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface_based_on_opencv);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        opencvCameraView = (JavaCameraView) findViewById(R.id.opencvCameraView);
        toogleRecordVideo = (ToggleButton) findViewById(R.id.toogleRecordVideo);

        InitVideoRecorder(deviceVideoFrameRecorder, "testVideo");

        toogleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isRecording) {
                if(isRecording) {
                    Log.d(videoRecordBasedOnOpencvTag, "Start");
                    StorePictureToStorage(MatToBitmap(eachCameraFrameImage), "testImage");
                }
                else {
                    Log.d(videoRecordBasedOnOpencvTag, "Stopped");
                }
            }
        });

        opencvBaseLoaderCallback = new BaseLoaderCallback(getApplicationContext()) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        opencvCameraView.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {
                super.onPackageInstall(operation, callback);
            }
        };

        opencvCameraView.setVisibility(SurfaceView.VISIBLE);
        opencvCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(IsOpencvModuleLoaded()) {
            opencvBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, opencvBaseLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DisableCameraView(opencvCameraView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisableCameraView(opencvCameraView);
    }

    public void DisableCameraView(JavaCameraView targetCameraView) {
        if(targetCameraView != null) {
            targetCameraView.disableView();
        }
    }

    public boolean IsOpencvModuleLoaded() {
        if(OpenCVLoader.initDebug()) {
            Log.d(videoRecordBasedOnOpencvTag, "opencv module loaded");
            return true;
        }
        else {
            Log.d(videoRecordBasedOnOpencvTag, "opencv module not loaded");
            return false;
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        eachCameraFrameImage = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        eachCameraFrameImage.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        eachCameraFrameImage = inputFrame.rgba();
        return eachCameraFrameImage;
    }

    public Bitmap MatToBitmap(Mat targetMatImage) {
        Bitmap eachImageFrameBitmap = null;
        try {
            eachImageFrameBitmap = Bitmap.createBitmap(targetMatImage.cols(), targetMatImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(targetMatImage, eachImageFrameBitmap);
        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in MatToBitmap: " + err.getMessage());
        }
        return eachImageFrameBitmap;
    }

    public void StorePictureToStorage(Bitmap bitmapImage, String saveImageName) {
        String savePath = Environment.getExternalStorageDirectory().toString();
        OutputStream fileOutputStream;
        File imageFile = new File(savePath, saveImageName + ".jpg");
        try {
            Log.d(videoRecordBasedOnOpencvTag, "the image will save at : " + savePath + " name : " + saveImageName + ".jpg");
            fileOutputStream = new FileOutputStream(imageFile);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in StorePictureToStorage: " + err.getMessage());
        }
    }

    public FFmpegFrameRecorder InitVideoRecorder(FFmpegFrameRecorder videoFrameRecorder, String saveVideoName) {
        try {
            String savePath = Environment.getExternalStorageDirectory().toString();
            savePath = savePath + "/" + saveVideoName + ".mp4";
            Log.d(videoRecordBasedOnOpencvTag, "video save path: " + savePath);
            videoFrameRecorder = new FFmpegFrameRecorder(savePath, 200, 150);
            videoFrameRecorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            videoFrameRecorder.setFrameRate(24);
            videoFrameRecorder.setPixelFormat(AV_PIX_FMT_RGB24);

        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in InitVideoRecorder: " + err.getMessage());
        }
        return videoFrameRecorder;
    }
}
