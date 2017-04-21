package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.OpenCVModule.CalculateBallDetect;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.futsal.manager.DefineManager.CAMERA_HEIGHT_RESOLUTION;
import static com.futsal.manager.DefineManager.CAMERA_WIDTH_RESOLUTION;

/**
 * Created by stories2 on 2017. 3. 27..
 */

public class CameraRecordProcess implements CameraBridgeViewBase.CvCameraViewListener2,
                                            MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener,
                                            SurfaceHolder.Callback, Camera.PreviewCallback{
    Activity cameraRecordActivity;
    JavaCameraView opencvCameraView;
    MediaRecorder mediaRecording;
    Camera phoneDeviceCamera;
    Mat eachCameraFrameImage;
    CalculateBallDetect calculateBallDetect;
    SurfaceHolder surfaceHolderRecordVideo;
    SurfaceView videoRecordSurfaceView;
    CameraOpenCVViewer cameraOpenCVViewer;
    Camera.Parameters parameters;

    public CameraRecordProcess(Activity cameraRecordActivity) {
        this.cameraRecordActivity = cameraRecordActivity;
    }

    public void SetJavaCameraView(JavaCameraView opencvCameraView) {
        this.opencvCameraView = opencvCameraView;
    }

    public void SetMediaRecording(MediaRecorder mediaRecording) {
        this.mediaRecording = mediaRecording;
    }

    public void SetPhoneDeviceCamera(Camera phoneDeviceCamera) {
        this.phoneDeviceCamera = phoneDeviceCamera;
    }

    public void SetCalculateBallDetect(CalculateBallDetect calculateBallDetect) {
        this.calculateBallDetect = calculateBallDetect;
    }

    public void SetSurfaceHolderRecordVideo(SurfaceHolder surfaceHolderRecordVideo) {
        this.surfaceHolderRecordVideo = surfaceHolderRecordVideo;
    }

    public void SetVideoRecordSurfaceView(SurfaceView videoRecordSurfaceView) {
        this.videoRecordSurfaceView = videoRecordSurfaceView;
    }

    public void SetCameraOpenCVViewer(CameraOpenCVViewer cameraOpenCVViewer) {
        this.cameraOpenCVViewer = cameraOpenCVViewer;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        //LogManager.PrintLog("CameraRecordProcess", "onPreviewFrame", "Getting Video Frame Image Data", DefineManager.LOG_LEVEL_INFO);
        try {
            //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            /*int eachPreviewFrameWith, eachPreviewFrameHeight;
            eachPreviewFrameWith = videoRecordSurfaceView.getWidth();
            eachPreviewFrameHeight = videoRecordSurfaceView.getHeight();
            Mat eacPreviewFrameImage = new Mat(eachPreviewFrameHeight + eachPreviewFrameHeight / 2, eachPreviewFrameWith, CvType.CV_8UC1);
            eacPreviewFrameImage.put(0, 0, bytes);
            cameraOpenCVViewer.SetProcessingMatData(eacPreviewFrameImage, eachPreviewFrameWith, eachPreviewFrameHeight);
            eacPreviewFrameImage.release();
            LogManager.PrintLog("CameraRecordProcess", "onPreviewFrame",
                    "Getting Video Frame Image Data " + eachPreviewFrameWith + " X " + eachPreviewFrameHeight, DefineManager.LOG_LEVEL_INFO);*/


            int width, height;
            width = parameters.getPreviewSize().width;
            height = parameters.getPreviewSize().height;

            YuvImage yuvImage = new YuvImage(bytes, parameters.getPreviewFormat(), width, height, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, width, height), 70, byteArrayOutputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            Mat frameImage = new Mat();
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bitmap, frameImage);
            if(frameImage == null) {
                LogManager.PrintLog("CameraRecordProcess", "onPreviewFrame", "frameImage == null", DefineManager.LOG_LEVEL_WARN);
            }
            cameraOpenCVViewer.SetProcessingMatData(frameImage, width, height);
            frameImage.release();
            LogManager.PrintLog("CameraRecordProcess", "onPreviewFrame",
                    "Getting Video Frame Image Data " + width + " X " + height, DefineManager.LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("CameraRecordProcess", "onPreviewFrame", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    @Override
    public void onError(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(phoneDeviceCamera == null) {
            phoneDeviceCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            parameters = phoneDeviceCamera.getParameters();
            List<Camera.Size> screenResolution = parameters.getSupportedPictureSizes();
            for(Camera.Size sizes : screenResolution) {
                LogManager.PrintLog("CameraRecordProcess", "onPreviewFrame", "support resolution: " + sizes.width + " " + sizes.height, DefineManager.LOG_LEVEL_INFO);
            }
            parameters.setPictureSize(CAMERA_WIDTH_RESOLUTION, CAMERA_HEIGHT_RESOLUTION);
            parameters.setPreviewSize(CAMERA_WIDTH_RESOLUTION, CAMERA_HEIGHT_RESOLUTION);
            phoneDeviceCamera.setParameters(parameters);
            parameters = phoneDeviceCamera.getParameters();
            try {
                //opencvCameraView.enableView();
            }
            catch (Exception err) {
                LogManager.PrintLog("CameraRecordProcess", "surfaceCreated", "opencvCameraView Enable Fail: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //Log.d(getString(R.string.app_name), "Surface Changed");
        LogManager.PrintLog("CameraRecordProcess", "surfaceChanged", "Surface Changed Method Called", DefineManager.LOG_LEVEL_INFO);
        try {
            phoneDeviceCamera.stopPreview();
            phoneDeviceCamera.setPreviewCallback(this);
            phoneDeviceCamera.setPreviewDisplay(surfaceHolderRecordVideo);
            phoneDeviceCamera.startPreview();
            //opencvCameraView.enableView();
        }
        catch (Exception err) {
            LogManager.PrintLog("CameraRecordProcess", "surfaceChanged", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        eachCameraFrameImage = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        eachCameraFrameImage.release();
        calculateBallDetect.ReleaseImages();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        eachCameraFrameImage = inputFrame.rgba();

        //Log.d(getString(R.string.app_name), "image info: " + eachCameraFrameImage.width() + ", " + eachCameraFrameImage.height() + ", " + eachCameraFrameImage.channels());
        //960, 720, 4
        eachCameraFrameImage = calculateBallDetect.DetectBallPosition(eachCameraFrameImage);
        /*Size eachFrameSize = eachCameraFrameImage.size();
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
        }*/
        return eachCameraFrameImage;
    }

    public void StartRecordMedia() {

        //opencvCameraView.disableView();

        if(mediaRecording == null) {
            String savePath = Environment.getExternalStorageDirectory().toString();
            try {

                mediaRecording = new MediaRecorder();//media 객체 생성 확인을 할 것
                //Log.e("test", "asdf: " + mediaRecording);
                mediaRecording.setCamera(phoneDeviceCamera);
                mediaRecording.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecording.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mediaRecording.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecording.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecording.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

                //mediaRecording.setPreviewDisplay(surfaceHolderRecordVideo.getSurface());
                mediaRecording.setOutputFile(savePath + "/testVideo3.mp4");

            }
            catch (Exception err) {
                LogManager.PrintLog("CameraRecordProcess", "StartRecordMedia", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            try{
                phoneDeviceCamera.startPreview();
                phoneDeviceCamera.unlock();
                phoneDeviceCamera.setPreviewCallback(this);
            }
            catch (Exception err) {
                //Log.d(getString(R.string.app_name), "camera init failed");
                LogManager.PrintLog("CameraRecordProcess", "StartRecordMedia", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            try {
                //phoneDeviceCamera.reconnect();
                mediaRecording.prepare();
                //Thread.sleep(1000);
                mediaRecording.start();
            }
            catch (Exception err) {
                //Log.d(videoRecordBasedOnOpencvTag, "Error in setOnCheckedChangeListener: " + err.getMessage());
                LogManager.PrintLog("CameraRecordProcess", "StartRecordMedia", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                mediaRecording.release();
                mediaRecording = null;
            }
        }
    }

    public void StopRecordMedia() {

        if(phoneDeviceCamera != null) {
            phoneDeviceCamera.stopPreview();
            phoneDeviceCamera.lock();
        }
        if(mediaRecording == null) {
            //opencvCameraView.enableView();
            return;
        }
        mediaRecording.stop();
        mediaRecording.reset();
        mediaRecording.release();
        mediaRecording = null;
        //opencvCameraView.enableView();
    }
}
