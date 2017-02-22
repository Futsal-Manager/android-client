package com.futsal.manager.CameraModule;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.R;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.AndroidFrameConverter;
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
import org.opencv.core.Size;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;

/**
 * Created by stories2 on 2017. 2. 19..
 */

public class VideoRecordBasedOnOpenCV extends Activity implements CameraBridgeViewBase.CvCameraViewListener2,
                                                                    MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener{

    static final String videoRecordBasedOnOpencvTag = "video with opencv";
    /*static {
        if(OpenCVLoader.initDebug()) {
            System.loadLibrary("avcore");
            System.loadLibrary("avformat");
            System.loadLibrary("avcodec");
            System.loadLibrary("avdevice");
            System.loadLibrary("avfilter");
            System.loadLibrary("avutil");
            System.loadLibrary("swscale");
            //System.loadLibrary("test_jni");
            Log.d(videoRecordBasedOnOpencvTag, "opencv module loaded");
        }
        else {
            Log.d(videoRecordBasedOnOpencvTag, "opencv module not loaded");
        }
    }*/

    JavaCameraView opencvCameraView;
    BaseLoaderCallback opencvBaseLoaderCallback;
    Mat eachCameraFrameImage;
    ToggleButton toogleRecordVideo;
    //MediaRecorder deviceVideoFrameRecorder;
    CameraBridgeViewBase opencvCameraViewBase;
    FFmpegFrameRecorder deviceVideoFrameRecorder;
    boolean isVideoRecording;
    opencv_core.IplImage eachVideoFrame;
    int frames, imageFrameWidth = 320, imageFrameHeight = 240;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_video_surface_based_on_opencv);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        opencvCameraView = (JavaCameraView) findViewById(R.id.opencvCameraView);
        toogleRecordVideo = (ToggleButton) findViewById(R.id.toogleRecordVideo);

        deviceVideoFrameRecorder = InitVideoRecorder(deviceVideoFrameRecorder, "testVideo");
        //deviceVideoFrameRecorder = InitMediaRecorder(deviceVideoFrameRecorder, "testVideo");

        isVideoRecording = false;

        toogleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isRecording) {
                isVideoRecording = !isVideoRecording;
                if(isRecording) {
                    StartRecordVideo();
                    Log.d(videoRecordBasedOnOpencvTag, "Start");
                    //StorePictureToStorage(MatToBitmap(eachCameraFrameImage), "testImage");
                }
                else {
                    StopRecordVideo();
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
            /*System.loadLibrary("avcore");
            System.loadLibrary("avformat");
            System.loadLibrary("avcodec");
            System.loadLibrary("avdevice");
            System.loadLibrary("avfilter");
            System.loadLibrary("avutil");
            System.loadLibrary("swscale");*/
            //System.loadLibrary("avutil");
            //ReLinker.loadLibrary(getApplicationContext(), "avutil");
            //ReLinker.loadLibrary(getApplicationContext(), "avformat");
            //ReLinker.loadLibrary(getApplicationContext(), "avcodec");
            //ReLinker.loadLibrary(getApplicationContext(), "swresample");
            //System.loadLibrary("test_jni");
            Log.d(videoRecordBasedOnOpencvTag, "opencv module loaded");
            //test();
            return true;
        }
        else {
            Log.d(videoRecordBasedOnOpencvTag, "opencv module not loaded");
            return false;
        }
    }

    public void test() {
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(getPackageName(),0);
            Log.i(videoRecordBasedOnOpencvTag, "app info sourceDir: " + info.sourceDir);
            Log.i(videoRecordBasedOnOpencvTag, "app info dataDir: " + info.dataDir);
            Log.i(videoRecordBasedOnOpencvTag, "app info nativeLibraryDir: " + info.nativeLibraryDir);

        } catch (Exception e){
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
        Size eachFrameSize = eachCameraFrameImage.size();
        if(isVideoRecording) {
            try {
                byte[] byteFrame = new byte[(int) (eachCameraFrameImage.total() * eachCameraFrameImage.channels())];
                eachCameraFrameImage.get(0, 0, byteFrame);
                onFrame(byteFrame, eachFrameSize);
                //deviceVideoFrameRecorder.record(new AndroidFrameConverter().convert(byteFrame, (int) eachFrameSize.width, (int) eachFrameSize.height));
            }
            catch (Exception err) {
                Log.d(videoRecordBasedOnOpencvTag, "Error in onCameraFrame: " + err.getMessage());
            }
        }
        return eachCameraFrameImage;
    }
    private void onFrame(byte[] data, Size eachFrameSize){

        long videoTimestamp = 1000 * (System.currentTimeMillis() - startTime);

        // Put the camera preview frame right into the yuvIplimage object
        //eachVideoFrame.getByteBuffer().put(data);

        try {

            // Get the correct time
            deviceVideoFrameRecorder.setTimestamp(videoTimestamp);

            // Record the image into FFmpegFrameRecorder
            //deviceVideoFrameRecorder.record(eachVideoFrame);
            deviceVideoFrameRecorder.record(new AndroidFrameConverter().convert(data, (int) eachFrameSize.width, (int) eachFrameSize.height));
            frames++;

            Log.i(videoRecordBasedOnOpencvTag, "Wrote Frame: " + frames);

        }
        catch (Exception e) {
            Log.v(videoRecordBasedOnOpencvTag,"Error in OnFrame: " + e.getMessage());
        }
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
            int depth = IPL_DEPTH_8U;
            int channels = 4;
            String savePath = Environment.getExternalStorageDirectory().toString();
            File videoFile = new File(savePath, saveVideoName + ".mp4");
            savePath = savePath + "/" + saveVideoName + ".mp4";
            Log.d(videoRecordBasedOnOpencvTag, "video save path: " + savePath);
            //FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(savePath);
            videoFrameRecorder = new FFmpegFrameRecorder(savePath, imageFrameWidth, imageFrameHeight, 1);
            videoFrameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            videoFrameRecorder.setFormat("mp4");
            videoFrameRecorder.setFrameRate(24);
            videoFrameRecorder.setVideoBitrate(16384);
            videoFrameRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            /*videoFrameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_HUFFYUV);
            videoFrameRecorder.setAudioCodec(avcodec.AV_CODEC_ID_NONE);
            videoFrameRecorder.setFormat("avi");
            //videoFrameRecorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            videoFrameRecorder.setFrameRate(24);
            //videoFrameRecorder.setPixelFormat(AV_PIX_FMT_RGB24);
            videoFrameRecorder.setPixelFormat(avutil.AV_PIX_FMT_RGB32);*/
            eachVideoFrame = opencv_core.IplImage.create(imageFrameWidth, imageFrameHeight, depth, channels);

        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in InitVideoRecorder: " + err.getMessage());
        }
        return videoFrameRecorder;
    }

    public MediaRecorder InitMediaRecorder(MediaRecorder deviceMediaRecorder, String saveVideoName) {
        try {
            CamcorderProfile captureImageQuailty = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);

            deviceMediaRecorder = new MediaRecorder();
            deviceMediaRecorder.setProfile(captureImageQuailty);
            deviceMediaRecorder.setOutputFile(saveVideoName + ".mp4");
            deviceMediaRecorder.setOnInfoListener(this);
            deviceMediaRecorder.setOnErrorListener(this);
            deviceMediaRecorder.prepare();
        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in InitMediaRecorder: " + err.getMessage());
        }
        return deviceMediaRecorder;
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onError(MediaRecorder mediaRecorder, int i, int i1) {

    }

    public void StartRecordVideo() {
        try {
            frames = 0;
            startTime = System.currentTimeMillis();
            deviceVideoFrameRecorder.start();
        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in StartRecordVideo: " + err.getMessage());
        }
    }

    public void StopRecordVideo() {
        try {
            deviceVideoFrameRecorder.stop();
            deviceVideoFrameRecorder.release();
        }
        catch (Exception err) {
            Log.d(videoRecordBasedOnOpencvTag, "Error in StopRecordVideo: " + err.getMessage());
        }
    }
}
