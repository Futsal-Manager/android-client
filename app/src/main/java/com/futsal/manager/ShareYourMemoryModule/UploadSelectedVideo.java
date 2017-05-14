package com.futsal.manager.ShareYourMemoryModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.MakeVideoModule.VideoUploadProcess;
import com.futsal.manager.R;
import com.victor.loading.newton.NewtonCradleLoading;

import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;

/**
 * Created by stories2 on 2017. 5. 15..
 */

public class UploadSelectedVideo extends Activity implements Runnable{

    NewtonCradleLoading newtonCradleLoading;
    TextView txtWaitStatus;
    String selectTargetVideoPath;
    VideoUploadProcess videoUploadProcess;
    Thread uploadWaitingLoop;
    boolean running, updatingStatusResetFlag, updateDoneStatusResetFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_selected_video);

        InitLayout();
    }

    void InitLayout() {
        running = true;
        updatingStatusResetFlag = true;
        updateDoneStatusResetFlag = true;

        uploadWaitingLoop = new Thread(this);

        txtWaitStatus = (TextView) findViewById(R.id.txtWaitStatus);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();

        selectTargetVideoPath = GetVideoPhysicalPath();
        if(selectTargetVideoPath != null) {
            videoUploadProcess = new VideoUploadProcess(this, selectTargetVideoPath);
            uploadWaitingLoop.start();
        }
        else {
            ShowWarningDialog();
        }
    }

    String GetVideoPhysicalPath() {
        String yourRealPath = null;
        Intent fileShareInfoIntent = getIntent();

        Uri videoFileUri = (Uri)fileShareInfoIntent.getParcelableExtra(Intent.EXTRA_STREAM);

        String[] filePathColumn = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(videoFileUri, filePathColumn, null, null, null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            yourRealPath = cursor.getString(columnIndex);
            LogManager.PrintLog("UploadSelectedVideo", "InitLayout", "file path: " + yourRealPath, DefineManager.LOG_LEVEL_DEBUG);
        } else {
            //boooo, cursor doesn't have rows ...
        }
        cursor.close();
        return yourRealPath;
    }

    void ShowWarningDialog() {
        try {
            AlertDialog.Builder bluetoothNotAvailableDialogBuilder = new AlertDialog.Builder(this);
            bluetoothNotAvailableDialogBuilder.setMessage(getString(R.string.iHateYouMessage));
            bluetoothNotAvailableDialogBuilder.setPositiveButton(R.string.meToo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog bluetoothModuleInitFailMessage = bluetoothNotAvailableDialogBuilder.create();
            bluetoothModuleInitFailMessage.show();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "ShowWarningDialog", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            running = false;
            uploadWaitingLoop.interrupt();
            uploadWaitingLoop.join();
        }
        catch (Exception err) {
            LogManager.PrintLog("EmbeddedSystemFinderProcesser", "onDestroy", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
        super.onDestroy();
    }

    @Override
    public void run() {
        while(running) {
            try {
                if(videoUploadProcess.IsProcessDone()) {
                    running = false;
                    if(updateDoneStatusResetFlag) {
                        updateDoneStatusResetFlag = false;
                        txtWaitStatus.setText("Upload Done!");
                        newtonCradleLoading.stop();
                    }
                }
                else {
                    if(updatingStatusResetFlag){
                        updatingStatusResetFlag = false;
                        txtWaitStatus.setText("Uploading...");
                    }
                }
            }
            catch (Exception err) {
                LogManager.PrintLog("EmbeddedSystemFinderProcesser", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
        }
    }
}
