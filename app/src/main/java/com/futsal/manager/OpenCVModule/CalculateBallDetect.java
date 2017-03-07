package com.futsal.manager.OpenCVModule;

import android.content.Context;
import android.util.Log;

import com.futsal.manager.R;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by stories2 on 2017. 3. 7..
 */

public class CalculateBallDetect {

    Context applicationContext;
    Mat originImage, originGrayImage, circleDetectedImage;

    public CalculateBallDetect(Context context) {
        applicationContext = context;
    }

    public Mat DetectBallPosition(Mat originImage) {
        if(originImage == null) {
            return null;
        }
        /*originGrayImage = new Mat(originImage.width(), originImage.height(), CvType.CV_8UC1);
        //circleDetectedImage = originGrayImage.clone();//originGrayImage;//new Mat(originImage.width(), originImage.height(), CvType.CV_8UC1);

        Imgproc.cvtColor(originImage, originGrayImage, Imgproc.COLOR_BGRA2GRAY);
        circleDetectedImage = originGrayImage.clone();

        Imgproc.GaussianBlur(originGrayImage, originGrayImage, new Size(9, 9), 2, 2);

        // accumulator value
        double dp = 1;
        // minimum distance between the center coordinates of detected circles in pixels
        double minDist = originGrayImage.rows() / 8;

        // min and max radii (set these values as you desire)
        int minRadius = 20, maxRadius = 400;

        // param1 = gradient value used to handle edge detection
        // param2 = Accumulator threshold value for the
        // cv2.CV_HOUGH_GRADIENT method.
        // The smaller the threshold is, the more circles will be
        // detected (including false circles).
        // The larger the threshold is, the more circles will
        // potentially be returned.
        double iCannyUpperThreshold = 100, iAccumulator = 300;

        Imgproc.HoughCircles(originGrayImage, circleDetectedImage, Imgproc.CV_HOUGH_GRADIENT, dp, minDist,
                iCannyUpperThreshold, iAccumulator, minRadius, maxRadius);

        return circleDetectedImage;*/
        try {
            Size sizeRgba = originImage.size(), resizeImage = new Size(320, 240);
            Mat rgbaInnerWindow = new Mat((int)sizeRgba.width, (int)sizeRgba.height, CvType.CV_8UC1);
            Mat mIntermediateMat = new Mat();
            //originImage.copyTo(rgbaInnerWindow);

            int rows = (int) sizeRgba.height;
            int cols = (int) sizeRgba.width;

            int left = cols / 8;
            int top = rows / 8;

            int width = cols * 3 / 4;
            int height = rows * 3 / 4;

            //Log.d(applicationContext.getString(R.string.app_name), "info: " + rows + " " + cols + " " + left + " " + top + " " + width + " " + height);

            //rgbaInnerWindow = originImage
            //        .submat(left, left + width, top, top + height);
            //Imgproc.cvtColor(rgbaInnerWindow, originImage,
            //        Imgproc.COLOR_RGBA2GRAY);
            Imgproc.cvtColor(originImage, rgbaInnerWindow, Imgproc.COLOR_BGRA2GRAY);
            //Imgproc.resize(rgbaInnerWindow, rgbaInnerWindow, resizeImage);
            Mat circles = rgbaInnerWindow.clone();
            Imgproc.GaussianBlur(rgbaInnerWindow, rgbaInnerWindow, new Size(9, 9), 2, 2);
            Imgproc.Canny(rgbaInnerWindow, mIntermediateMat, 10, 90);
            Imgproc.HoughCircles(mIntermediateMat, circles,
                    Imgproc.CV_HOUGH_GRADIENT, 1, 75, 50, 13, 35, 40);
            Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow,
                    Imgproc.COLOR_GRAY2BGRA, 4);

            for (int x = 0; x < circles.cols(); x++) {
                double vCircle[] = circles.get(0, x);
                if (vCircle == null)
                    break;
                Point pt = new Point(Math.round(vCircle[0]),
                        Math.round(vCircle[1]));
                int radius = (int) Math.round(vCircle[2]);
                Log.d("cv", pt + " radius " + radius);
                Imgproc.circle(rgbaInnerWindow, pt, 3, new Scalar(0, 0, 255), 5);
                Imgproc.circle(rgbaInnerWindow, pt, radius, new Scalar(255, 0, 0),
                        5);
            }
            circles.release();
            mIntermediateMat.release();
            originImage = rgbaInnerWindow;
            //rgbaInnerWindow.release();

            return originImage;
        }
        catch (Exception err) {
            Log.d(applicationContext.getString(R.string.app_name), "Error in DetectBallPosition: " + err.getMessage());
        }
        return originImage;
    }

    public void ReleaseImages() {

    }
}
