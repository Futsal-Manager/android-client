package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

import org.opencv.core.Point;

import static com.futsal.manager.DefineManager.AVAILABLE_SCREEN_RESOLUTION_LIST;
import static com.futsal.manager.DefineManager.BLUETOOTH_SEND_SPEED;
import static com.futsal.manager.DefineManager.CAMERA_HEIGHT_RESOLUTION;
import static com.futsal.manager.DefineManager.CAMERA_WIDTH_RESOLUTION;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE_SOCKET;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.PICTURE_RESOLUTION_SETTING;
import static com.futsal.manager.DefineManager.SCREEN_HEIGHT;
import static com.futsal.manager.DefineManager.SCREEN_WIDTH;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class MakeNewMemoryManagerProcesser extends Thread implements SurfaceHolder.Callback, Camera.PreviewCallback {

    Activity makeNewMmeoryManager;
    SurfaceView cameraSurfaceView;
    SurfaceHolder cameraSurfaceHolder;
    Camera phoneDeviceCamera;
    Camera.Parameters phoneDeviceCameraParameters;
    MakeNewMemoryOpencvManager makeNewMemoryOpencvManager;
    MakeNewMemoryBluetoothManager makeNewMemoryBluetoothManager;
    Thread makeNewMemoryManagerProcesserMainLoop;
    boolean running;

    public MakeNewMemoryManagerProcesser(Activity makeNewMmeoryManager, SurfaceView cameraSurfaceView) {
        this.makeNewMmeoryManager = makeNewMmeoryManager;
        this.cameraSurfaceView = cameraSurfaceView;

        InitLayout();
    }

    void InitLayout() {

        running = true;

        cameraSurfaceHolder = cameraSurfaceView.getHolder();
        cameraSurfaceHolder.addCallback(this);
        cameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            if(phoneDeviceCamera == null) {
                phoneDeviceCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                phoneDeviceCameraParameters = phoneDeviceCamera.getParameters();

                phoneDeviceCameraParameters.setPictureSize(AVAILABLE_SCREEN_RESOLUTION_LIST[PICTURE_RESOLUTION_SETTING][SCREEN_WIDTH],
                        AVAILABLE_SCREEN_RESOLUTION_LIST[PICTURE_RESOLUTION_SETTING][SCREEN_HEIGHT]);
                phoneDeviceCameraParameters.setPreviewSize(CAMERA_WIDTH_RESOLUTION, CAMERA_HEIGHT_RESOLUTION);
                phoneDeviceCamera.setParameters(phoneDeviceCameraParameters);
            }
            makeNewMemoryOpencvManager = new MakeNewMemoryOpencvManager(makeNewMmeoryManager, phoneDeviceCameraParameters);
            makeNewMemoryBluetoothManager = new MakeNewMemoryBluetoothManager(makeNewMmeoryManager);
            makeNewMemoryManagerProcesserMainLoop = new Thread(this);
        }
        catch (Exception err) {
            LogManager.PrintLog("MakeNewMemoryManagerProcesser", "InitLayout", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        //Log.d(getString(R.string.app_name), "Surface Changed");
        //LogManager.PrintLog("MakeNewMemoryManagerProcesser", "surfaceChanged", "Surface Changed Method Called", DefineManager.LOG_LEVEL_INFO);
        try {
            phoneDeviceCamera.stopPreview();
            phoneDeviceCamera.setPreviewCallback(this);
            phoneDeviceCamera.setPreviewDisplay(cameraSurfaceHolder);
            phoneDeviceCamera.startPreview();
            //opencvCameraView.enableView();
            makeNewMemoryManagerProcesserMainLoop.start();
        }
        catch (Exception err) {
            LogManager.PrintLog("MakeNewMemoryManagerProcesser", "surfaceChanged", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        makeNewMemoryOpencvManager.EachFrameImageData(data, camera);
    }

    public void StopProcess() {
        try {
            running = false;
            makeNewMemoryManagerProcesserMainLoop.interrupt();
            makeNewMemoryManagerProcesserMainLoop.join();

            if(EMBEDDED_SYSTEM_DEVICE_SOCKET != null) {
                EMBEDDED_SYSTEM_DEVICE_SOCKET.close();
            }
            if(EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER != null) {
                EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER.disable();
                EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER = null;
            }
            if(phoneDeviceCamera != null) {
                phoneDeviceCamera.setPreviewCallback(null);
                phoneDeviceCamera.stopPreview();
                phoneDeviceCamera.release();
                phoneDeviceCamera = null;
            }
        }
        catch (Exception err) {
            LogManager.PrintLog("MakeNewMemoryManagerProcesser", "StopProcess", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    String PointToString(Point ballPosition) {
        String ballPositionDataOrder = "{(" + (int)ballPosition.x + ")[" + (int)ballPosition.y + "]}";
        return ballPositionDataOrder;
    }

    @Override
    public void run() {
        super.run();
        while(running) {
            try {
                String bluetoothSendMessageData = PointToString(makeNewMemoryOpencvManager.GetLastBallDetectedPosition());
                makeNewMemoryBluetoothManager.SendBluetoothOrder(bluetoothSendMessageData);
                Thread.sleep(BLUETOOTH_SEND_SPEED);
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewMemoryManagerProcesser", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
        }
    }
}
