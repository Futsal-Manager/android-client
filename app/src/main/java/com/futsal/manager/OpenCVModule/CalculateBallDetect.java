package com.futsal.manager.OpenCVModule;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.futsal.manager.DefineManager.BALL_DETECT_CUTLINE_Y;
import static com.futsal.manager.DefineManager.BALL_POSITION_DATA;
import static com.futsal.manager.DefineManager.BLUR_MODE_OPTION;
import static com.futsal.manager.DefineManager.MAXIMUM_CIRCLE_RADIUS;
import static com.futsal.manager.DefineManager.MAXIMUM_DETECT_COLOR_H;
import static com.futsal.manager.DefineManager.MAXIMUM_DETECT_COLOR_S;
import static com.futsal.manager.DefineManager.MAXIMUM_DETECT_COLOR_V;
import static com.futsal.manager.DefineManager.MINIMUM_CIRCLE_RADIUS;
import static com.futsal.manager.DefineManager.MINIMUM_DETECT_COLOR_H;
import static com.futsal.manager.DefineManager.MINIMUM_DETECT_COLOR_S;
import static com.futsal.manager.DefineManager.MINIMUM_DETECT_COLOR_V;
import static com.futsal.manager.DefineManager.NOT_AVAILABLE;

/**
 * Created by stories2 on 2017. 3. 7..
 */

public class CalculateBallDetect {

    Context applicationContext;
    Mat originImage, originGrayImage, circleDetectedImage;
    Mat blurred, hsv, mask, hierarchy;
    Scalar orangeLower, orangeUpper;
    List<MatOfPoint> listOfContour;
    int i, maxPoint;
    double max, contourArea;
    Point xy, center, xyTemp;
    float[] radius;
    Moments M;
    MatOfPoint2f c;
    Size resizeResolution;
    Handler ballDetectionData;
    Message ballPositionMessage;

    public CalculateBallDetect(Context context) {
        applicationContext = context;

        blurred = new Mat();
        hsv = new Mat();
        mask = new Mat();
        hierarchy = new Mat();

        c = new MatOfPoint2f();

        xy = new Point();
        center = new Point();
        xyTemp = new Point();

        M = new Moments();

        orangeLower = new Scalar(MINIMUM_DETECT_COLOR_H, MINIMUM_DETECT_COLOR_S, MINIMUM_DETECT_COLOR_V);
        orangeUpper = new Scalar(MAXIMUM_DETECT_COLOR_H, MAXIMUM_DETECT_COLOR_S, MAXIMUM_DETECT_COLOR_V);

        listOfContour = new ArrayList<MatOfPoint>();

        resizeResolution = new Size(640, 480);
    }

    public CalculateBallDetect(Context context, Handler ballDetectionData) {
        applicationContext = context;
        this.ballDetectionData = ballDetectionData;

        blurred = new Mat();
        hsv = new Mat();
        mask = new Mat();
        hierarchy = new Mat();

        c = new MatOfPoint2f();

        xy = new Point();
        center = new Point();
        xyTemp = new Point();

        M = new Moments();

        orangeLower = new Scalar(MINIMUM_DETECT_COLOR_H, MINIMUM_DETECT_COLOR_S, MINIMUM_DETECT_COLOR_V);
        orangeUpper = new Scalar(MAXIMUM_DETECT_COLOR_H, MAXIMUM_DETECT_COLOR_S, MAXIMUM_DETECT_COLOR_V);

        listOfContour = new ArrayList<MatOfPoint>();

        resizeResolution = new Size(640, 480);

        ballPositionMessage = ballDetectionData.obtainMessage();
        ballPositionMessage.what = BALL_POSITION_DATA;
    }

    public Mat DetectBallPositionVer2(Mat frame) {
        if(frame == null) {
            return null;
        }
        try {

            // Imgproc.GaussianBlur(frame, blurred, new Size(EACH_BLUR_BLOCK_SIZE, EACH_BLUR_BLOCK_SIZE), 0); #Todo: 이거 문제
            //
            if(BLUR_MODE_OPTION) {
                Imgproc.blur(frame, blurred, new Size(7,7));
            }

            //Imgproc.resize(frame, frame, resizeResolution);
            //LogManager.PrintLog("OpenCVModuleProcesser", "DetectCircleFromFrameImage", "resolution: " + frame.cols() + " " + frame.rows(), LOG_LEVEL_INFO);
            Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);

            Core.inRange(hsv, orangeLower, orangeUpper, mask);
            Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
            Imgproc.dilate(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));

            Imgproc.findContours(mask, listOfContour, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            if(!listOfContour.isEmpty()) {

                Collections.sort(listOfContour, contourAreaCompare);
                Collections.reverse(listOfContour);

                for(MatOfPoint c2 : listOfContour) {
                    c = MatOfPointToMatOfPoint2f(c2);
                    radius = new float[listOfContour.size()];

                    Imgproc.minEnclosingCircle(c, xyTemp, radius);

                    M = Imgproc.moments(c);

                    center.x = M.m10 / M.m00;
                    center.y = M.m01 / M.m00;

                    if(radius[0] > MINIMUM_CIRCLE_RADIUS && radius[0] < MAXIMUM_CIRCLE_RADIUS && xyTemp.y >= BALL_DETECT_CUTLINE_Y) {

                        xy = xyTemp;

                        if(ballDetectionData != null) {
                            ballPositionMessage = null;
                            ballPositionMessage = ballDetectionData.obtainMessage();
                            ballPositionMessage.what = BALL_POSITION_DATA;
                            ballPositionMessage.arg1 = (int)xyTemp.x;
                            ballPositionMessage.arg2 = (int)xyTemp.y;
                            ballDetectionData.sendMessage(ballPositionMessage);
                        }

                        Imgproc.circle(frame, xy, (int)radius[0], new Scalar(0, 255, 255), 2);
                        LogManager.PrintLog("CalculateBallDetect", "DetectCircleFromFrameImage", "Ball Position: " + xy.x + " " + xy.y, DefineManager.LOG_LEVEL_DEBUG);
                        LogManager.PrintLog("CalculateBallDetect", "DetectCircleFromFrameImage", "Temp Position: " + xyTemp.x + " " + xyTemp.y, DefineManager.LOG_LEVEL_DEBUG);
                        break;
                    }
                }

                /*c = Max(listOfContour);

                if(c != null) {

                    radius = new float[listOfContour.size()];

                    Imgproc.minEnclosingCircle(c, xy, radius);

                    M = Imgproc.moments(c);

                    center.x = M.m10 / M.m00;
                    center.y = M.m01 / M.m00;

                    if(radius[0] > MINIMUM_CIRCLE_RADIUS) {
                        Imgproc.circle(frame, xy, (int)radius[0], new Scalar(0, 255, 255), 2);
                    }

                    LogManager.PrintLog("OpenCVModuleProcesser", "DetectCircleFromFrameImage", "Ball Position: " + xy.x + " " + xy.y, DefineManager.LOG_LEVEL_DEBUG);
                }*/

                listOfContour.clear();
            }
        }
        catch (Exception err) {
            LogManager.PrintLog("OpenCVModuleProcesser", "DetectCircleFromFrameImage", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
        return frame;
    }

    public Point GetBallPosition() {
        return xy;
    }

    public MatOfPoint2f MatOfPointToMatOfPoint2f(MatOfPoint matOfPoint) {
        if(matOfPoint == null) {
            return null;
        }
        return new MatOfPoint2f(matOfPoint.toArray());
    }

    Comparator<MatOfPoint> contourAreaCompare = new Comparator<MatOfPoint>() {
        @Override
        public int compare(MatOfPoint o1, MatOfPoint o2) {
            double contourAreaSizeO1 = Imgproc.contourArea(o1),
                    contourAreaSizeO2 = Imgproc.contourArea(o2);
            if(contourAreaSizeO1 > contourAreaSizeO2) {
                return 1;
            }
            else if(contourAreaSizeO1 < contourAreaSizeO2) {
                return -1;
            }
            return 0;
        }
    };

    public MatOfPoint2f Max(List<MatOfPoint> listOfContour) {
        max = NOT_AVAILABLE;
        maxPoint = NOT_AVAILABLE;
        for(i = 0; i < listOfContour.size(); i += 1) {
            contourArea = Imgproc.contourArea(listOfContour.get(i));

            if(contourArea > max) {
                max = contourArea;
                maxPoint = i;
            }
        }
        if(maxPoint == NOT_AVAILABLE) {
            return null;
        }
        return MatOfPointToMatOfPoint2f(listOfContour.get(maxPoint));
    }

    public void ReleaseMats() {
        try {
            if(blurred != null) {
                blurred.release();
            }
            if(hsv != null) {
                hsv.release();
            }
            if(mask != null) {
                mask.release();
            }
            if(hierarchy != null) {
                hierarchy.release();
            }
            if(c != null) {
                c.release();
            }
        }
        catch (Exception err) {
            LogManager.PrintLog("OpenCVModuleProcesser", "ReleaseMats", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
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
/*
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
                    Imgproc.CV_HOUGH_GRADIENT, 1, 75, 50, 13, 35, 40);//파라미터가 의미하는 것을 찾아볼 것
            Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow,
                    Imgproc.COLOR_GRAY2BGRA, 4);

            for (int x = 0; x < circles.cols(); x++) {
                double vCircle[] = circles.get(0, x);
                if (vCircle == null)
                    break;
                Point pt = new Point(Math.round(vCircle[0]),
                        Math.round(vCircle[1]));
                int radius = (int) Math.round(vCircle[2]);
                //Log.d("cv", pt + " radius " + radius);
                LogManager.PrintLog("CalculateBallDetect", "DetectBallPosition", "position: " + pt + " radius: " + radius, DefineManager.LOG_LEVEL_INFO);
                Imgproc.circle(rgbaInnerWindow, pt, 3, new Scalar(0, 0, 255), 5);
                Imgproc.circle(rgbaInnerWindow, pt, radius, new Scalar(255, 0, 0),
                        5);
            }

            circles.release();
            mIntermediateMat.release();
            //originImage = rgbaInnerWindow;
            //rgbaInnerWindow.release();
            rgbaInnerWindow.copyTo(originImage);
            rgbaInnerWindow.release();
*/
            //Imgproc.cvtColor(originImage, originImage, Imgproc.COLOR_BGRA2GRAY);

            return originImage;
        }
        catch (Exception err) {
            //Log.d(applicationContext.getString(R.string.app_name), "Error in DetectBallPosition: " + err.getMessage());
            LogManager.PrintLog("CalculateBallDetect", "DetectBallPosition", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
        return originImage;
    }

    public void ReleaseImages() {

    }
}
