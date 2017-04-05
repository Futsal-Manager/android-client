package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.view.SurfaceHolder;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.OpenCVModule.CalculateBallDetect;

import org.opencv.core.Mat;

/**
 * Created by stories2 on 2017. 4. 6..
 */

public class CameraOpenCVViewer implements SurfaceHolder.Callback2 {

    Mat eachCameraPreviewFrameImage;
    CalculateBallDetect calculateBallDetect;

    public CameraOpenCVViewer(Activity cameraRecordManagerActivity) {

    }

    public void SetCalculateBallDetect(CalculateBallDetect calculateBallDetect) {
        this.calculateBallDetect = calculateBallDetect;
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

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

    public void SetProcessingMatData(Mat eachCameraPreviewFrameImage) {
        this.eachCameraPreviewFrameImage = eachCameraPreviewFrameImage;
        if(eachCameraPreviewFrameImage == null) {
            LogManager.PrintLog("CameraOpenCVViewer", "SetProcessingMatData", "eachCameraPreviewFrameImage = null", DefineManager.LOG_LEVEL_WARN);
        }
        else {
            LogManager.PrintLog("CameraOpenCVViewer", "SetProcessingMatData", "eachCameraPreviewFrameImage ok", DefineManager.LOG_LEVEL_INFO);
        }
    }
}
