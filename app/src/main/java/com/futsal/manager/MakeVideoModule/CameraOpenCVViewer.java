package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.view.SurfaceHolder;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

/**
 * Created by stories2 on 2017. 4. 6..
 */

public class CameraOpenCVViewer implements SurfaceHolder.Callback2 {

    public CameraOpenCVViewer(Activity cameraRecordManagerActivity) {

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
}
