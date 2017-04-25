package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.futsal.manager.BluetoothModule.BluetoothCommunication;
import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.OpenCVModule.CalculateBallDetect;
import com.futsal.manager.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;

import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;
import static com.futsal.manager.DefineManager.NOT_WORKING;

/**
 * Created by stories2 on 2017. 3. 26..
 */

public class CameraRecordManager extends Activity{

    CameraRecordProcess cameraRecordProcess;
    JavaCameraView opencvCameraView;
    ToggleButton toogleRecordVideo;
    SurfaceView surfaceRecordVideo, opencvSurfaceView;
    SurfaceHolder surfaceHolderRecordVideo, opencvSurfaceHolder;
    CalculateBallDetect calculateBallDetect;
    BaseLoaderCallback opencvBaseLoaderCallback;
    boolean isVideoRecording;
    Button btnVideoUpload, btnSendA, btnSendB;
    BluetoothCommunication bluetoothCommunication;
    Intent passedData;
    VideoUploadProcess videoUploadProcess;
    Activity cameraRecordManager;
    BluetoothDeviceControlProcesser bluetoothDeviceControlProcesser;
    CameraOpenCVViewer cameraOpenCVViewer;
    MakeNewHistoryViewInit makeNewHistoryViewInit;

    static {
        if(OpenCVLoader.initDebug()) {
            //Log.d(logCatTag, "OpenCV Loaded");
            LogManager.PrintLog("FutsalManagerMain", "static", "OpenCV Loaded", DefineManager.LOG_LEVEL_INFO);
        }
        else {
            //Log.d(logCatTag, "OpenCV not Loaded");
            LogManager.PrintLog("FutsalManagerMain", "static", "OpenCV not Loaded", DefineManager.LOG_LEVEL_WARN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_record_manager);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            cameraRecordProcess = new CameraRecordProcess(this);
            calculateBallDetect = new CalculateBallDetect(getApplicationContext());
            cameraOpenCVViewer = new CameraOpenCVViewer(this);
            makeNewHistoryViewInit = new MakeNewHistoryViewInit(this);

            opencvCameraView = (JavaCameraView) findViewById(R.id.opencvCameraView);
            toogleRecordVideo = (ToggleButton) findViewById(R.id.toogleRecordVideo);
            surfaceRecordVideo = (SurfaceView) findViewById(R.id.surfaceRecordVideo);
            opencvSurfaceView = (SurfaceView) findViewById(R.id.opencvSurfaceView);
            btnVideoUpload = (Button) findViewById(R.id.btnVideoUpload);
            btnSendA = (Button) findViewById(R.id.btnSendA);
            btnSendB = (Button)findViewById(R.id.btnSendB);

            surfaceHolderRecordVideo = surfaceRecordVideo.getHolder();
            surfaceHolderRecordVideo.addCallback(cameraRecordProcess);
            surfaceHolderRecordVideo.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            opencvSurfaceHolder = opencvSurfaceView.getHolder();
            opencvSurfaceHolder.addCallback(cameraOpenCVViewer);
            opencvSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            opencvCameraView.setVisibility(SurfaceView.VISIBLE);
            opencvCameraView.setCvCameraViewListener(cameraRecordProcess);

            cameraRecordProcess.SetJavaCameraView(opencvCameraView);
            cameraRecordProcess.SetCalculateBallDetect(calculateBallDetect);
            cameraRecordProcess.SetSurfaceHolderRecordVideo(surfaceHolderRecordVideo);
            cameraRecordProcess.SetVideoRecordSurfaceView(surfaceRecordVideo);
            cameraRecordProcess.SetCameraOpenCVViewer(cameraOpenCVViewer);

            cameraOpenCVViewer.SetCalculateBallDetect(calculateBallDetect);
            cameraOpenCVViewer.SetOpencvSurfaceHolder(opencvSurfaceHolder);

            isVideoRecording = false;
            cameraRecordManager = this;

            makeNewHistoryViewInit.execute();
        }
        catch (Exception err) {
            LogManager.PrintLog("CameraRecordManager", "onCreate", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }


        toogleRecordVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isRecording) {
                isVideoRecording = !isVideoRecording;
                if(isRecording) {
                    //StartRecordVideo();
                    cameraRecordProcess.StartRecordMedia();
                    LogManager.PrintLog("CameraRecordManager", "onCheckedChanged", "Toggle Changed to Start Record Video", DefineManager.LOG_LEVEL_INFO);
                    //Log.d(videoRecordBasedOnOpencvTag, "Start");
                    //StorePictureToStorage(MatToBitmap(eachCameraFrameImage), "testImage");
                }
                else {
                    //StopRecordVideo();
                    cameraRecordProcess.StopRecordMedia();
                    LogManager.PrintLog("CameraRecordManager", "onCheckedChanged", "Toggle Changed to Stop Record Video", DefineManager.LOG_LEVEL_INFO);
                    //Log.d(videoRecordBasedOnOpencvTag, "Stopped");
                }
            }
        });

        opencvBaseLoaderCallback = new BaseLoaderCallback(getApplicationContext()) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        opencvCameraView.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }

            @Override
            public void onPackageInstall(int operation, InstallCallbackInterface callback) {
                super.onPackageInstall(operation, callback);
            }
        };

        btnVideoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Upload Process", Snackbar.LENGTH_SHORT).show();
                LogManager.PrintLog("CameraRecordManager", "onClick", "Upload button clicked", DefineManager.LOG_LEVEL_INFO);
                if(!isVideoRecording) {
                    videoUploadProcess = new VideoUploadProcess(cameraRecordManager);
                }
            }
        });
/*
        btnSendA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMove();
            }
        });

        btnSendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendStop();
            }
        });*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        DisableCameraView(opencvCameraView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            DisableCameraView(opencvCameraView);
            bluetoothCommunication.CloseConnection();
            cameraOpenCVViewer.StopProcessing();
            if(bluetoothDeviceControlProcesser != null) {
                bluetoothDeviceControlProcesser.StopProcess();
            }
        }
        catch (Exception err) {
            LogManager.PrintLog("CameraRecordManager", "onDestroy", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
    }

    public void DisableCameraView(JavaCameraView targetCameraView) {
        if(targetCameraView != null) {
            targetCameraView.disableView();
        }
    }

    public class MakeNewHistoryViewInit extends AsyncTask<Void, Void, Void> {

        ProgressDialog makeNewHistoryViewInitProgressDialog;
        boolean bluetoothConnectionStatus;

        public MakeNewHistoryViewInit(Activity cameraRecordManager) {
            super();
            makeNewHistoryViewInitProgressDialog = new ProgressDialog(CameraRecordManager.this);
            bluetoothConnectionStatus = true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                passedData = getIntent();
                bluetoothCommunication = (BluetoothCommunication)passedData.getExtras().getSerializable("bluetoothDeviceData");
                bluetoothCommunication.SetBluetoothAdapter(BluetoothAdapter.getDefaultAdapter());

                LogManager.PrintLog("MakeNewHistoryViewInit", "doInBackground", "ble adapter: " + bluetoothCommunication.GetBluetoothAdapter() +
                        " ble address: " + bluetoothCommunication.GetSelectedDeviceAddress(), DefineManager.LOG_LEVEL_INFO);
                bluetoothConnectionStatus = bluetoothCommunication.ConnectToTargetBluetoothDevice(BluetoothAdapter.getDefaultAdapter(), bluetoothCommunication.GetSelectedDeviceAddress());

                if(bluetoothConnectionStatus == NOT_WORKING) {
                    LogManager.PrintLog("MakeNewHistoryViewInit", "doInBackground", "bluetooth connection failed", LOG_LEVEL_WARN);
                }
                else {
                    bluetoothDeviceControlProcesser = new BluetoothDeviceControlProcesser(bluetoothCommunication, cameraOpenCVViewer);
                }
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewHistoryViewInit", "doInBackground", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            try {
                makeNewHistoryViewInitProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                makeNewHistoryViewInitProgressDialog.setMessage("기기 초기화중");

                makeNewHistoryViewInitProgressDialog.show();
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewHistoryViewInit", "onPreExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                makeNewHistoryViewInitProgressDialog.dismiss();

                if(bluetoothConnectionStatus == NOT_WORKING) {
                    final AlertDialog.Builder bluetoothConnectionWarningDialog  = new AlertDialog.Builder(CameraRecordManager.this);
                    bluetoothConnectionWarningDialog.setMessage("블루투스 장비 연결을 확인하세요");
                    bluetoothConnectionWarningDialog.setTitle("장비 연결 문제 발생");
                    bluetoothConnectionWarningDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    bluetoothConnectionWarningDialog.setCancelable(false);
                    bluetoothConnectionWarningDialog.create().show();
                }
            }
            catch (Exception err) {
                LogManager.PrintLog("MakeNewHistoryViewInit", "onPostExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
