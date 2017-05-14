package com.futsal.manager.MakeVideoModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Environment;

import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationWithServer;

import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;
import static com.futsal.manager.DefineManager.WAIT_FOR_LOGIN;
import static com.futsal.manager.DefineManager.WAIT_FOR_UPLOAD_VIDEO;

/**
 * Created by stories2 on 2017. 3. 27..
 */

public class VideoUploadProcess implements Runnable {

    Thread loginThread, uploadThread;
    int networkOrderStatus;
    CommunicationWithServer communicationWithServer;
    Activity cameraRecordManagerActivity;
    ProgressDialog progressDialog;
    String videoSavedPath = null;
    boolean isProcessDone;

    public VideoUploadProcess(Activity cameraRecordManagerActivity) {
        loginThread = new Thread(this);
        uploadThread = new Thread(this);
        communicationWithServer = new CommunicationWithServer(cameraRecordManagerActivity);

        progressDialog = ProgressDialog.show(cameraRecordManagerActivity, "",
                "Video Uploading", false);
        progressDialog.setCancelable(true);

        TryLoginToServer();
    }

    public VideoUploadProcess(Activity cameraRecordManagerActivity, String videoSavedPath) {
        loginThread = new Thread(this);
        uploadThread = new Thread(this);

        this.videoSavedPath = videoSavedPath;
        isProcessDone = false;

        communicationWithServer = new CommunicationWithServer(cameraRecordManagerActivity);

        progressDialog = ProgressDialog.show(cameraRecordManagerActivity, "",
                "Video Uploading", false);
        progressDialog.setCancelable(true);

        TryLoginToServer();
    }

    public void TryLoginToServer() {
        LogManager.PrintLog("VideoUploadProcess", "TryLoginToServer", "Trying Login to Server", LOG_LEVEL_INFO);
        networkOrderStatus = WAIT_FOR_LOGIN;
        communicationWithServer.AuthLogin(TEST_ACCOUNT, TEST_ACCOUNT_PASSWORD);
        loginThread.start();
    }

    @Override
    public void run() {
        switch (networkOrderStatus) {
            case WAIT_FOR_LOGIN:
                while(!communicationWithServer.GetLoginStatus()) {
                    try {
                        Thread.sleep(1);
                    }
                    catch (Exception err) {
                        LogManager.PrintLog("VideoUploadProcess", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                LogManager.PrintLog("VideoUploadProcess", "run", "Login Process Ended", LOG_LEVEL_INFO);
                UploadVideo();
                break;
            case WAIT_FOR_UPLOAD_VIDEO:
                while(!communicationWithServer.videoUploadStatus) {
                    try {
                        Thread.sleep(1);
                    }
                    catch (Exception err) {
                        LogManager.PrintLog("VideoUploadProcess", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                break;
        }
    }

    void UploadVideo() {
        try {
            networkOrderStatus = WAIT_FOR_UPLOAD_VIDEO;
            String savePath = Environment.getExternalStorageDirectory().toString() + "/testVideo3.mp4";
            if(videoSavedPath != null) {
                savePath = videoSavedPath;
            }
            LogManager.PrintLog("VideoUploadProcess", "UploadVideo", "upload target file path: " + savePath, LOG_LEVEL_DEBUG);
            communicationWithServer.UploadFileTester3(savePath);
            uploadThread.start();
            uploadThread.join();
            LogManager.PrintLog("VideoUploadProcess", "UploadVideo", "Upload Process Ended", LOG_LEVEL_INFO);
            isProcessDone = true;
            progressDialog.dismiss();
        }
        catch (Exception err) {
            LogManager.PrintLog("VideoUploadProcess", "UploadVideo", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public boolean IsProcessDone() {
        return isProcessDone;
    }
}
