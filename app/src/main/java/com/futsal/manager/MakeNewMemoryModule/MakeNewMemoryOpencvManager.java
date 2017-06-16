package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.OpenCVModule.CalculateBallDetect;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.ByteArrayOutputStream;

import static com.futsal.manager.DefineManager.BALL_POSITION_DATA;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;

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
        this.calculateBallDetect = new CalculateBallDetect(makeNewMemoryManager.getApplicationContext(), ballDetectionData);
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
            //lastBallDetectPosition = calculateBallDetect.GetBallPosition();
            frameImage.release();
            if(lastBallDetectPosition != null) {
                LogManager.PrintLog("MakeNewMemoryOpencvManager", "EachFrameImageData", "last ball position: " + lastBallDetectPosition.x + " " + lastBallDetectPosition.y, DefineManager.LOG_LEVEL_INFO);
            }

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

    Handler ballDetectionData = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BALL_POSITION_DATA:
                    if(lastBallDetectPosition == null) {
                        lastBallDetectPosition = new Point();
                    }
                    lastBallDetectPosition.x = msg.arg1;
                    lastBallDetectPosition.y = msg.arg2;
                    LogManager.PrintLog("MakeNewMemoryOpencvManager", "handleMessage", "last ball position: " + lastBallDetectPosition.x + " " + lastBallDetectPosition.y, LOG_LEVEL_INFO);
                    break;
                default:
                    break;
            }
        }
    };
}
