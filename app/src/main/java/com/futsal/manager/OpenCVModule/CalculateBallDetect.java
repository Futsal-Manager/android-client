package com.futsal.manager.OpenCVModule;

import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by stories2 on 2017. 3. 7..
 */

public class CalculateBallDetect {

    Context applicationContext;
    Mat originImage, originGrayImage;

    public CalculateBallDetect(Context context) {
        applicationContext = context;
    }

    public Mat DetectBallPosition(Mat originImage) {
        if(originImage == null) {
            return null;
        }
        originGrayImage = new Mat(originImage.width(), originImage.height(), CvType.CV_8UC1);

        Imgproc.cvtColor(originImage, originGrayImage, Imgproc.COLOR_BGRA2GRAY);

        Imgproc.GaussianBlur(originGrayImage, originGrayImage, new Size(9, 9), 2, 2);

        return originGrayImage;
    }
}
