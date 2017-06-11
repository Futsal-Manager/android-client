package com.futsal.manager.ShowVideoModule;

import android.os.AsyncTask;
import android.os.Handler;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;

import static com.futsal.manager.DefineManager.CLOSE_VIDEO_UPLOAD_STATUS_VIEW;
import static com.futsal.manager.DefineManager.NO_ACTION;
import static com.futsal.manager.DefineManager.OPEN_VIDEO_UPLOAD_STATUS_VIEW;
import static com.futsal.manager.DefineManager.VIDEO_EDIT_REQUEST_STATUS;
import static com.futsal.manager.DefineManager.VIDEO_UPLOADED_FAILURE;
import static com.futsal.manager.DefineManager.VIDEO_UPLOADED_SUCCESSFULLY;
import static com.futsal.manager.DefineManager.VIDEO_UPLOADING;

/**
 * Created by stories2 on 2017. 6. 11..
 */

public class LibraryVideoManagerProcess extends AsyncTask<Void, Void, Void> {

    boolean running;
    int lastVideoUploadStatus;
    Handler videoUploadStatusView;

    public LibraryVideoManagerProcess(Handler videoUploadStatusView) {
        super();
        running = true;
        lastVideoUploadStatus = NO_ACTION;

        this.videoUploadStatusView = videoUploadStatusView;
    }

    @Override
    protected Void doInBackground(Void... params) {

        while(running) {
            try {
                //LogManager.PrintLog("LibraryVideoManagerProcess", "doInBackground", "status: " + VIDEO_EDIT_REQUEST_STATUS + " lstatus: " + lastVideoUploadStatus, LOG_LEVEL_INFO);
                switch (VIDEO_EDIT_REQUEST_STATUS) {
                    case NO_ACTION:
                        if(lastVideoUploadStatus != VIDEO_EDIT_REQUEST_STATUS) {
                            lastVideoUploadStatus = VIDEO_EDIT_REQUEST_STATUS;
                            videoUploadStatusView.sendEmptyMessage(CLOSE_VIDEO_UPLOAD_STATUS_VIEW);
                        }
                        break;
                    case VIDEO_UPLOADING:

                        if(lastVideoUploadStatus != VIDEO_EDIT_REQUEST_STATUS) {
                            lastVideoUploadStatus = VIDEO_EDIT_REQUEST_STATUS;
                            videoUploadStatusView.sendEmptyMessage(OPEN_VIDEO_UPLOAD_STATUS_VIEW);
                        }
                        break;
                    case VIDEO_UPLOADED_FAILURE:
                        if(lastVideoUploadStatus != VIDEO_EDIT_REQUEST_STATUS) {
                            lastVideoUploadStatus = VIDEO_EDIT_REQUEST_STATUS;
                            videoUploadStatusView.sendEmptyMessage(CLOSE_VIDEO_UPLOAD_STATUS_VIEW);
                        }
                        break;
                    case VIDEO_UPLOADED_SUCCESSFULLY:
                        if(lastVideoUploadStatus != VIDEO_EDIT_REQUEST_STATUS) {
                            lastVideoUploadStatus = VIDEO_EDIT_REQUEST_STATUS;
                            videoUploadStatusView.sendEmptyMessage(CLOSE_VIDEO_UPLOAD_STATUS_VIEW);
                        }
                        break;
                    default:
                        break;
                }
                Thread.sleep(1);
            }
            catch (Exception err) {
                LogManager.PrintLog("LibraryVideoManagerProcess", "doInBackground", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        try {
            videoUploadStatusView.sendEmptyMessage(CLOSE_VIDEO_UPLOAD_STATUS_VIEW);
        }
        catch (Exception err) {

        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            videoUploadStatusView.sendEmptyMessage(CLOSE_VIDEO_UPLOAD_STATUS_VIEW);
        }
        catch (Exception err) {

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

    public void CloseProcess() {
        running = false;
    }
}