package com.futsal.manager.ShareYourMemoryModule;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.futsal.manager.LogModule.LogManager;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.NO_ACTION;
import static com.futsal.manager.DefineManager.VIDEO_EDIT_REQUEST_STATUS;
import static com.futsal.manager.DefineManager.VIDEO_UPLOADED_FAILURE;

/**
 * Created by stories2 on 2017. 6. 11..
 */

public class UploadVideoBackground extends Service {

    UploadVideoBackgroundProcess uploadVideoBackgroundProcess;

    public UploadVideoBackground() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogManager.PrintLog("UploadVideoBackground", "onStart", "Service started", LOG_LEVEL_INFO);
        VIDEO_EDIT_REQUEST_STATUS = NO_ACTION;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            VIDEO_EDIT_REQUEST_STATUS = NO_ACTION;
            uploadVideoBackgroundProcess = new UploadVideoBackgroundProcess();
            uploadVideoBackgroundProcess.execute();
            LogManager.PrintLog("UploadVideoBackground", "onStartCommand", "processing", LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("UploadVideoBackground", "onStartCommand", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            VIDEO_EDIT_REQUEST_STATUS = VIDEO_UPLOADED_FAILURE;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            LogManager.PrintLog("UploadVideoBackground", "onDestroy", "Done", LOG_LEVEL_INFO);
            //VIDEO_EDIT_REQUEST_STATUS = NO_ACTION;
        }
        catch (Exception err) {
            LogManager.PrintLog("UploadVideoBackground", "onDestroy", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }
}
