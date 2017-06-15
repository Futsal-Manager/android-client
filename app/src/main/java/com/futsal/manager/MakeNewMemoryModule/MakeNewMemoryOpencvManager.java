package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.OpenCVModule.CalculateBallDetect;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.ByteArrayOutputStream;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class MakeNewMemoryOpencvManager{

    Activity makeNewMemoryManager;
    CalculateBallDetect calculateBallDetect;
    Camera.Parameters phoneDeviceCameraParameters;
    Point lastBallDetectPosition;

    public MakeNewMemoryOpencvManager(Activity makeNewMemoryManager, Camera.Parameters phoneDeviceCameraParameters) {
        this.makeNewMemoryManager = makeNewMemoryManager;
        this.calculateBallDetect = new CalculateBallDetect(makeNewMemoryManager.getApplicationContext());
        this.phoneDeviceCameraParameters = phoneDeviceCameraParameters;

        lastBallDetectPosition = null;
    }

    public void EachFrameImageData(byte[] data, Camera camera) {
        try {
            //LogManager.PrintLog("MakeNewMemoryOpencvManager", "EachFrameImageData", "Before ball detecting", DefineManager.LOG_LEVEL_INFO);
            int width, height;
            width = phoneDeviceCameraParameters.getPreviewSize().width;
            height = phoneDeviceCameraParameters.getPreviewSize().height;

            YuvImage yuvImage = new YuvImage(data, phoneDeviceCameraParameters.getPreviewFormat(), width, height, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, width, height), 70, byteArrayOutputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            Mat frameImage = new Mat();
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(bitmap, frameImage);
            if(frameImage == null) {
                LogManager.PrintLog("MakeNewMemoryOpencvManager", "EachFrameImageData", "frameImage == null", DefineManager.LOG_LEVEL_WARN);
            }
            //cameraOpenCVViewer.SetProcessingMatData(frameImage, width, height);
            calculateBallDetect.DetectBallPositionVer2(frameImage);
            lastBallDetectPosition = calculateBallDetect.GetBallPosition();
            frameImage.release();
            //LogManager.PrintLog("MakeNewMemoryOpencvManager", "EachFrameImageData",
            //        "Getting Video Frame Image Data " + width + " X " + height, DefineManager.LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("MakeNewMemoryOpencvManager", "EachFrameImageData", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    public Point GetLastBallDetectedPosition() {
        if(lastBallDetectPosition == null) {
            LogManager.PrintLog("MakeNewMemoryOpencvManager", "GetLastBallDetectedPosition", "last ball position = null", DefineManager.LOG_LEVEL_WARN);
            return new Point(0, 0);
        }
        else {
            return lastBallDetectPosition;
        }
    }
}
