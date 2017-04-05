package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.OpenCVModule.CalculateBallDetect;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by stories2 on 2017. 4. 6..
 */

public class CameraOpenCVViewer implements SurfaceHolder.Callback2, Runnable {

    Mat eachCameraPreviewFrameImage;
    CalculateBallDetect calculateBallDetect;
    SurfaceHolder opencvSurfaceHolder;
    Thread opencvDrawingThread;
    boolean running;
    Bitmap opencvFrameImage;
    int eachFrameImageWith, eachFrameImageHeight;

    public CameraOpenCVViewer(Activity cameraRecordManagerActivity) {
        eachCameraPreviewFrameImage = new Mat();
        running = true;

        opencvDrawingThread = new Thread(this);
        opencvDrawingThread.start();
    }

    public void SetOpencvSurfaceHolder(SurfaceHolder opencvSurfaceHolder) {
        this.opencvSurfaceHolder = opencvSurfaceHolder;
    }

    public void SetCalculateBallDetect(CalculateBallDetect calculateBallDetect) {
        this.calculateBallDetect = calculateBallDetect;
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    public void DrawSurfaceView() {
        Canvas surfaceViewCanvas = null;
        try {
            surfaceViewCanvas = opencvSurfaceHolder.lockCanvas(null);
            if(opencvFrameImage != null) {
                surfaceViewCanvas.drawBitmap(opencvFrameImage, 0, 0, null);
            }
            else {
                LogManager.PrintLog("CameraOpenCVViewer", "DrawSurfaceView", "opencvFrameImage == null", DefineManager.LOG_LEVEL_WARN);
            }
        }
        catch (Exception err) {
            LogManager.PrintLog("CameraOpenCVViewer", "DrawSurfaceView", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
        finally {
            if(surfaceViewCanvas != null) {
                opencvSurfaceHolder.unlockCanvasAndPost(surfaceViewCanvas);
            }
            else {
                LogManager.PrintLog("CameraOpenCVViewer", "DrawSurfaceView", "surfaceViewCanvas = null", DefineManager.LOG_LEVEL_WARN);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogManager.PrintLog("CameraOpenCVViewer", "surfaceCreated", "surfaceCreated", DefineManager.LOG_LEVEL_INFO);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogManager.PrintLog("CameraOpenCVViewer", "surfaceChanged", "surfaceChanged", DefineManager.LOG_LEVEL_INFO);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogManager.PrintLog("CameraOpenCVViewer", "surfaceDestroyed", "surfaceDestroyed", DefineManager.LOG_LEVEL_INFO);
    }

    public void SetProcessingMatData(Mat eachCameraPreviewFrameImage, int eachFrameImageWith, int eachFrameImageHeight) {
        //this.eachCameraPreviewFrameImage = eachCameraPreviewFrameImage;
        this.eachFrameImageWith = eachFrameImageWith;
        this.eachFrameImageHeight = eachFrameImageHeight;

        eachCameraPreviewFrameImage.copyTo(this.eachCameraPreviewFrameImage);

        if(eachCameraPreviewFrameImage == null) {
            LogManager.PrintLog("CameraOpenCVViewer", "SetProcessingMatData", "eachCameraPreviewFrameImage = null", DefineManager.LOG_LEVEL_WARN);
        }
        else {
            LogManager.PrintLog("CameraOpenCVViewer", "SetProcessingMatData", "eachCameraPreviewFrameImage ok", DefineManager.LOG_LEVEL_INFO);
        }
    }

    Bitmap MatToBitmap(Mat eachCameraPreviewFrameImage) {
        Bitmap eachCameraPreviewFrameBitmap = null;
        Mat convertTempImage = null;
        try {
            if(eachCameraPreviewFrameImage == null) {
                LogManager.PrintLog("CameraOpenCVViewer", "MatToBitmap", "eachCameraPreviewFrameImage = null", DefineManager.LOG_LEVEL_WARN);
            }
            else {
                convertTempImage = new Mat(eachFrameImageHeight, eachFrameImageWith, CvType.CV_8U, new Scalar(4));
                Imgproc.cvtColor(eachCameraPreviewFrameImage, convertTempImage, Imgproc.COLOR_GRAY2RGBA, 4);
                eachCameraPreviewFrameBitmap = Bitmap.createBitmap(convertTempImage.cols(), convertTempImage.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(convertTempImage, eachCameraPreviewFrameBitmap);
                convertTempImage.release();
                LogManager.PrintLog("CameraOpenCVViewer", "MatToBitmap", "Convert ok", DefineManager.LOG_LEVEL_INFO);
                return eachCameraPreviewFrameBitmap;
            }
        }
        catch (Exception err) {
            LogManager.PrintLog("CameraOpenCVViewer", "MatToBitmap", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
        return eachCameraPreviewFrameBitmap;
    }

    @Override
    public void run() {
        while(running) {
            try {
                if(eachCameraPreviewFrameImage == null) {
                    LogManager.PrintLog("CameraOpenCVViewer", "run", "eachCameraPreviewFrameImage == null", DefineManager.LOG_LEVEL_WARN);
                }
                else {
                    opencvFrameImage = MatToBitmap(eachCameraPreviewFrameImage);
                    DrawSurfaceView();
                }
                Thread.sleep(1);
            }
            catch (Exception err) {
                LogManager.PrintLog("CameraOpenCVViewer", "run", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
        }
    }

    public void StopProcessing() {
        try {
            running = false;

            eachCameraPreviewFrameImage.release();
        }
        catch (Exception err) {

        }
    }
}
