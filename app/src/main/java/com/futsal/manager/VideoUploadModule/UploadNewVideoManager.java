package com.futsal.manager.VideoUploadModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.MakeVideoModule.VideoUploadProcess;
import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 5. 2..
 */

public class UploadNewVideoManager extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_new_video_manager);

        UploadNewVideoProcess uploadNewVideoProcess = new UploadNewVideoProcess(this);
        uploadNewVideoProcess.execute();

    }

    public class UploadNewVideoProcess extends AsyncTask<Void, Void, Void> {

        ProgressDialog uploadNewVideoProcessDialog;
        Activity uploadNewVideoManager;

        public UploadNewVideoProcess(Activity uploadNewVideoManager) {
            super();
            this.uploadNewVideoManager = uploadNewVideoManager;
            uploadNewVideoProcessDialog = new ProgressDialog(uploadNewVideoManager);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                uploadNewVideoProcessDialog.dismiss();
                Handler videoUploadHandler = new Handler(Looper.getMainLooper());
                videoUploadHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogManager.PrintLog("UploadNewVideoManager", "onCreate", "Video Upload Start", DefineManager.LOG_LEVEL_INFO);
                        VideoUploadProcess videoUploadProcess = new VideoUploadProcess(uploadNewVideoManager);
                    }
                }, 0);
            }
            catch (Exception err) {
                LogManager.PrintLog("UploadNewVideoProcess", "doInBackground", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            try {
                uploadNewVideoProcessDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                uploadNewVideoProcessDialog.setMessage("비디오 업로드 준비 중");

                uploadNewVideoProcessDialog.show();
            }
            catch (Exception err) {
                LogManager.PrintLog("UploadNewVideoProcess", "onPreExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                LogManager.PrintLog("UploadNewVideoProcess", "onPostExecute", "Video Upload Done", DefineManager.LOG_LEVEL_INFO);
            }
            catch (Exception err) {
                LogManager.PrintLog("UploadNewVideoProcess", "onPostExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
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
