package com.futsal.manager.ShareYourMemoryModule;

import android.content.Context;
import android.os.AsyncTask;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.LOG_LEVEL_WARN;
import static com.futsal.manager.DefineManager.NO_ACTION;
import static com.futsal.manager.DefineManager.VIDEO_EDIT_REQUEST_STATUS;
import static com.futsal.manager.DefineManager.VIDEO_UPLOADED_FAILURE;
import static com.futsal.manager.DefineManager.VIDEO_UPLOADING;

/**
 * Created by stories2 on 2017. 6. 11..
 */

public class UploadVideoBackgroundProcess extends AsyncTask<Void, Void, Void> {
    Context context;

    public UploadVideoBackgroundProcess(Context context) {
        super();
        this.context = context;
        if(context == null) {
            LogManager.PrintLog("UploadVideoBackgroundProcess", "UploadVideoBackgroundProcess", "context null", LOG_LEVEL_WARN);
        }
        LogManager.PrintLog("UploadVideoBackgroundProcess", "UploadVideoBackgroundProcess", "init ok", LOG_LEVEL_INFO);
    }

    @Override
    protected Void doInBackground(Void... params) {
        LogManager.PrintLog("UploadVideoBackgroundProcess", "doInBackground", "can u see me?", LOG_LEVEL_INFO);
        try {
            VIDEO_EDIT_REQUEST_STATUS = VIDEO_UPLOADING;
            LogManager.PrintLog("UploadVideoBackgroundProcess", "doInBackground", "Start uploading", LOG_LEVEL_INFO);
            Thread.sleep(30000);
            LogManager.PrintLog("UploadVideoBackgroundProcess", "doInBackground", "End of uploading", LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("UploadVideoBackgroundProcess", "doInBackground", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            VIDEO_EDIT_REQUEST_STATUS = VIDEO_UPLOADED_FAILURE;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        LogManager.PrintLog("UploadVideoBackgroundProcess", "onPreExecute", "rdy to process", LOG_LEVEL_INFO);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        LogManager.PrintLog("UploadVideoBackgroundProcess", "onPostExecute", "Execute end", DefineManager.LOG_LEVEL_INFO);
        VIDEO_EDIT_REQUEST_STATUS = NO_ACTION;
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
