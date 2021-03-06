package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import org.opencv.core.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.futsal.manager.DefineManager.AUDIO_RECORD_BIT_RATE;
import static com.futsal.manager.DefineManager.AUDIO_SAMPLING_RATE;
import static com.futsal.manager.DefineManager.AVAILABLE_SCREEN_RESOLUTION_LIST;
import static com.futsal.manager.DefineManager.BLUETOOTH_CONNECTION_FAILURE;
import static com.futsal.manager.DefineManager.BLUETOOTH_SEND_SPEED;
import static com.futsal.manager.DefineManager.CAMERA_HEIGHT_RESOLUTION;
import static com.futsal.manager.DefineManager.CAMERA_WIDTH_RESOLUTION;
import static com.futsal.manager.DefineManager.DEFAULT_SCREEN_HEIGHT;
import static com.futsal.manager.DefineManager.DEFAULT_SCREEN_WIDTH;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_BLUETOOTH_ADAPTER;
import static com.futsal.manager.DefineManager.EMBEDDED_SYSTEM_DEVICE_SOCKET;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;
import static com.futsal.manager.DefineManager.PICTURE_RESOLUTION_SETTING;
import static com.futsal.manager.DefineManager.SCREEN_HEIGHT;
import static com.futsal.manager.DefineManager.SCREEN_WIDTH;
import static com.futsal.manager.DefineManager.VIDEO_RECORD_BIT_RATE;
import static com.futsal.manager.DefineManager.VIDEO_RECORD_TIME_DATA;

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
    MediaRecorder mediaRecording;
    boolean running, showEmbeddedSystemWarningMessage, isRecording;

    long startHTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updatedTime = 0L;
    TextView txtRecordingTime;
    List<Camera.Size> cameraAvailableVideoResolution;
    int cameraResolutionWidth, cameraResolutionHeight;

    public MakeNewMemoryManagerProcesser(Activity makeNewMmeoryManager, SurfaceView cameraSurfaceView, TextView txtRecordingTime) {
        this.makeNewMmeoryManager = makeNewMmeoryManager;
        this.cameraSurfaceView = cameraSurfaceView;
        this.txtRecordingTime = txtRecordingTime;

        InitLayout();
    }

    void InitLayout() {

        running = true;
        mediaRecording = null;
        showEmbeddedSystemWarningMessage = true;
        cameraResolutionWidth = DEFAULT_SCREEN_WIDTH;
        cameraResolutionHeight = DEFAULT_SCREEN_HEIGHT;

        cameraSurfaceHolder = cameraSurfaceView.getHolder();
        cameraSurfaceHolder.addCallback(this);
        cameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraAvailableVideoResolution = new ArrayList<Camera.Size>();

        try {
            if(phoneDeviceCamera == null) {
                phoneDeviceCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                phoneDeviceCameraParameters = phoneDeviceCamera.getParameters();

                phoneDeviceCameraParameters.setPictureSize(AVAILABLE_SCREEN_RESOLUTION_LIST[PICTURE_RESOLUTION_SETTING][SCREEN_WIDTH],
                        AVAILABLE_SCREEN_RESOLUTION_LIST[PICTURE_RESOLUTION_SETTING][SCREEN_HEIGHT]);
                phoneDeviceCameraParameters.setPreviewSize(CAMERA_WIDTH_RESOLUTION, CAMERA_HEIGHT_RESOLUTION);
                //phoneDeviceCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
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

    public List<String> GetAvailableCameraResolution() {
        List<String> listOfScreenResolution = new ArrayList<String>();
        if(phoneDeviceCameraParameters != null) {
            cameraAvailableVideoResolution = phoneDeviceCameraParameters.getSupportedVideoSizes();
            for(Camera.Size indexOfPictureSize : cameraAvailableVideoResolution) {
                LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetAvailableCameraResolution", "Available size: " + indexOfPictureSize.width + " X " + indexOfPictureSize.height, LOG_LEVEL_INFO);
                listOfScreenResolution.add(indexOfPictureSize.width + " X " + indexOfPictureSize.height);
            }
        }
        else {
            LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetAvailableCameraResolution", "Phone parameters null", LOG_LEVEL_WARN);
        }
        return listOfScreenResolution;
    }

    public void SetCameraResolution(int cameraResolutionWidth, int cameraResolutionHeight) {
        this.cameraResolutionWidth = cameraResolutionWidth;
        this.cameraResolutionHeight = cameraResolutionHeight;
    }

    public ArrayList<MakeNewMemoryManagerCameraResolutionListItem> GetAvailableCameraResolutionVer2() {
        LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetAvailableCameraResolution", "updated resolution: " + cameraResolutionWidth + " X " + cameraResolutionHeight, LOG_LEVEL_INFO);
        ArrayList<MakeNewMemoryManagerCameraResolutionListItem> listOfScreenResolution = new ArrayList<MakeNewMemoryManagerCameraResolutionListItem>();
        if(phoneDeviceCameraParameters != null) {
            cameraAvailableVideoResolution = phoneDeviceCameraParameters.getSupportedVideoSizes();
            for(Camera.Size indexOfPictureSize : cameraAvailableVideoResolution) {
                LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetAvailableCameraResolution", "Available size: " + indexOfPictureSize.width + " X " + indexOfPictureSize.height, LOG_LEVEL_INFO);
                MakeNewMemoryManagerCameraResolutionListItem indexOfCameraSize;
                indexOfCameraSize = new MakeNewMemoryManagerCameraResolutionListItem();

                indexOfCameraSize.SetAvailableCameraVideoRecordResolution(indexOfPictureSize);
                if(indexOfPictureSize.width == cameraResolutionWidth && indexOfPictureSize.height == cameraResolutionHeight) {
                    LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetAvailableCameraResolution", "Selected resolution: " + indexOfPictureSize.width + " X " + indexOfPictureSize.height, LOG_LEVEL_INFO);
                    indexOfCameraSize.SetIsSelected(true);
                }
                else {
                    indexOfCameraSize.SetIsSelected(false);

                }

                listOfScreenResolution.add(indexOfCameraSize);
            }
        }
        else {
            LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetAvailableCameraResolution", "Phone parameters null", LOG_LEVEL_WARN);
        }
        return listOfScreenResolution;
    }



    public void SetIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
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

    Handler mainLoopMessageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            AlertDialog.Builder bluetoothNotAvailableDialogBuilder = new AlertDialog.Builder(makeNewMmeoryManager);
            bluetoothNotAvailableDialogBuilder.setMessage(makeNewMmeoryManager.getString(R.string.bluetoothDeviceConnectionRefused));
            bluetoothNotAvailableDialogBuilder.setPositiveButton(R.string.ohShit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    makeNewMmeoryManager.finish();
                }
            });
            AlertDialog bluetoothModuleInitFailMessage = bluetoothNotAvailableDialogBuilder.create();
            bluetoothModuleInitFailMessage.show();
        }
    };

    @Override
    public void run() {
        super.run();
        while(running) {
            try {
                CalculateRecordTime();
                if(isRecording) {
                    if(BLUETOOTH_CONNECTION_FAILURE != true && EMBEDDED_SYSTEM_DEVICE_SOCKET != null) {
                        String bluetoothSendMessageData = PointToString(makeNewMemoryOpencvManager.GetLastBallDetectedPosition());
                        makeNewMemoryBluetoothManager.SendBluetoothOrder(bluetoothSendMessageData);
                    }
                    else {
                        if(showEmbeddedSystemWarningMessage) {
                            if(EMBEDDED_SYSTEM_DEVICE_SOCKET != null) {
                                EMBEDDED_SYSTEM_DEVICE_SOCKET = null;
                            }
                            showEmbeddedSystemWarningMessage = false;
                            Message bluetoothCrashMessage = mainLoopMessageHandler.obtainMessage();
                            mainLoopMessageHandler.sendMessage(bluetoothCrashMessage);
                            LogManager.PrintLog("MakeNewMemoryManagerProcesser", "run", "print warning message", LOG_LEVEL_INFO);
                        }
                    }
                }
                /**/
                Thread.sleep(BLUETOOTH_SEND_SPEED);
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewMemoryManagerProcesser", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
        }
    }

    void CalculateRecordTime() {
        if(isRecording) {
            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = secs / 3600;
            secs = secs % 60;

            Message handlerMessage = videoRecordingTimeHandler.obtainMessage();
            handlerMessage.what = VIDEO_RECORD_TIME_DATA;
            handlerMessage.obj = "" + String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
            videoRecordingTimeHandler.sendMessage(handlerMessage);
            //LogManager.PrintLog("MakeNewMemoryManagerProcesser", "CalculateRecordTime", "Recording time: " + hours + ":" + mins + ":" + secs, LOG_LEVEL_INFO);
        }
    }

    public void StartRecording() {
        StartRecordMedia();
        isRecording = true;
        startHTime = SystemClock.uptimeMillis();
    }

    public void StopRecording() {
        StopRecordMedia();
        //timeSwapBuff += timeInMilliseconds;
        isRecording = false;
    }

    public void StopRecordMedia() {
        if(mediaRecording == null) {
            //opencvCameraView.enableView();
            return;
        }
        mediaRecording.stop();
        mediaRecording.reset();
        mediaRecording.release();
        mediaRecording = null;

        /*makeNewMmeoryManager.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                .parse("file://"
                        + GetFilePath())));
        //opencvCameraView.enableView();*/
        MediaScanning();
    }

    void MediaScanning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            File out = new File(GetFilePath());
            Uri contentUri = Uri.fromFile(out);
            mediaScanIntent.setData(contentUri);
            makeNewMmeoryManager.sendBroadcast(mediaScanIntent);
        } else {
            makeNewMmeoryManager.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + GetFilePath())));
        }
    }

    public void StartRecordMedia() {

        //opencvCameraView.disableView();

        if(mediaRecording == null) {

            try {
                try {
                    String savePath = GetFilePath() + "/FutsalManager" + GetVideoName() + ".mp4";
                    LogManager.PrintLog("MakeNewMemoryManagerProcesser", "StartRecordMedia", "Video save path: " + savePath, LOG_LEVEL_DEBUG);

                    mediaRecording = new MediaRecorder();//media 객체 생성 확인을 할 것
                    //Log.e("test", "asdf: " + mediaRecording);
                    mediaRecording.setCamera(phoneDeviceCamera);
                    mediaRecording.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecording.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mediaRecording.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecording.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mediaRecording.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    //mediaRecording.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                    mediaRecording.setVideoSize(cameraResolutionWidth, cameraResolutionHeight);
                    mediaRecording.setAudioEncodingBitRate(AUDIO_RECORD_BIT_RATE);
                    mediaRecording.setAudioSamplingRate(AUDIO_SAMPLING_RATE);
                    mediaRecording.setVideoEncodingBitRate(VIDEO_RECORD_BIT_RATE);
                    mediaRecording.setMaxFileSize(2048000000); // Set max file size 2G

                    mediaRecording.setOrientationHint(0);

                    //mediaRecording.setPreviewDisplay(surfaceHolderRecordVideo.getSurface());
                    mediaRecording.setOutputFile(savePath);// + GetVideoName());

                }
                catch (Exception err) {
                    LogManager.PrintLog("MakeNewMemoryManagerProcesser", "InitStartRecordMedia", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                }
                try{
                    phoneDeviceCamera.stopPreview();
                    phoneDeviceCamera.setPreviewCallback(this);
                    phoneDeviceCamera.setDisplayOrientation(0);
                    phoneDeviceCamera.startPreview();
                    phoneDeviceCamera.unlock();
                    phoneDeviceCamera.setPreviewCallback(this);
                }
                catch (Exception err) {
                    //Log.d(getString(R.string.app_name), "camera init failed");
                    LogManager.PrintLog("MakeNewMemoryManagerProcesser", "StartRecordMedia", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                }
                try {
                    //phoneDeviceCamera.reconnect();
                    mediaRecording.prepare();
                    //Thread.sleep(1000);
                    mediaRecording.start();
                }
                catch (Exception err) {
                    //Log.d(videoRecordBasedOnOpencvTag, "Error in setOnCheckedChangeListener: " + err.getMessage());
                    LogManager.PrintLog("MakeNewMemoryManagerProcesser", "StartRecordMedia", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                    mediaRecording.release();
                    mediaRecording = null;
                }
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewMemoryManagerProcesser", "StartRecordMedia", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
        }
    }

    String GetVideoName() {
        Date recordDateInfo = new Date();
        int year, month, day, hour, min, sec;
        year = recordDateInfo.getYear();
        month = recordDateInfo.getMonth();
        day = recordDateInfo.getDate();
        hour = recordDateInfo.getHours();
        min = recordDateInfo.getMinutes();
        sec = recordDateInfo.getSeconds();
        String dateInfo = "" + String.format("%02d", (year - 100)) + "" + String.format("%02d", (month + 1)) + "" + String.format("%02d", day)
                + "" + String.format("%02d", hour) + "" + String.format("%02d", min) + "" + String.format("%02d", sec);
        //dateInfo = "";
        return dateInfo;
    }

    public String GetFilePath() {
        File fileSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + "FutsalManager");
        String savePath = fileSavePath.getPath();
        LogManager.PrintLog("MakeNewMemoryManagerProcesser", "GetFilePath", "Path: " + savePath, LOG_LEVEL_DEBUG);
        if(!fileSavePath.exists()) {
            fileSavePath.mkdirs();
        }
        return savePath;
    }

    final Handler videoRecordingTimeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIDEO_RECORD_TIME_DATA:
                    if(txtRecordingTime != null) {
                        txtRecordingTime.setText(msg.obj.toString());
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}


