package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.futsal.manager.EmbeddedCommunicationModule.EmbeddedSystemFinder;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.futsal.manager.DefineManager.LIBRARY_TYPE_EDIT;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class FullFilmManager extends Activity {

    GridView fullFilmGridView;
    List<EachGridViewItemModel> fullFilmList;
    FloatingActionButton floatingActionBtnNewMemory;
    File[] listOfSavedFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_film_manager);

        fullFilmGridView = (GridView)findViewById(R.id.gridFullFilm);
        floatingActionBtnNewMemory = (FloatingActionButton)findViewById(R.id.floatingActionBtnNewMemory);

        InitLayout();

        floatingActionBtnNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordVideoLayout = new Intent(getApplicationContext(), EmbeddedSystemFinder.class);
                startActivity(recordVideoLayout);
            }
        });

        fullFilmGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogManager.PrintLog("FullFilmManager", "onItemClick", "Item selected pos: " + position, LOG_LEVEL_DEBUG);
                EachGridViewItemModel eachGridViewItemModel = fullFilmList.get(position);

                ScanAndPlayVideo(eachGridViewItemModel.GetVideoOriginName(), getApplicationContext());
                //PlayVideo(eachGridViewItemModel.GetVideoOriginName());
            }
        });
    }

    void InitLayout() {

        listOfSavedFiles = GetFileList(GetFilePath());

        fullFilmList = new ArrayList<EachGridViewItemModel>();

        for(File indexOfSavedFile : listOfSavedFiles) {
            try {
                EachGridViewItemModel indexOfSavedFileItemModel = new EachGridViewItemModel();
                indexOfSavedFileItemModel.SetVideoOriginName(indexOfSavedFile.getName());
                indexOfSavedFileItemModel.SetVideoName(ParseFileName(indexOfSavedFile));
                indexOfSavedFileItemModel.SetThumnailImage(GetVideoThumbnailImage(indexOfSavedFile.getAbsolutePath()));
                indexOfSavedFileItemModel.SetVideoDurationTime(GetVideoDurationTime(indexOfSavedFile.getAbsolutePath()));
                indexOfSavedFileItemModel.SetSubBtnType(LIBRARY_TYPE_EDIT);
                indexOfSavedFileItemModel.SetMediaScanContext(getApplicationContext());

                fullFilmList.add(indexOfSavedFileItemModel);
            }
            catch (Exception err) {
                LogManager.PrintLog("FullFilmManager", "InitLayout", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
        }

        EachGridViewItem fullFilmGridViewAdapater = new EachGridViewItem(fullFilmList, getApplicationContext(), R.layout.library_video_manager_item);

        fullFilmGridView.setAdapter(fullFilmGridViewAdapater);

    }

    @Override
    protected void onResume() {
        super.onResume();
        InitLayout();
        LogManager.PrintLog("FullFilmManager", "onResume", "update video list", LOG_LEVEL_INFO);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogManager.PrintLog("FullFilmManager", "onPause", "test", LOG_LEVEL_INFO);
    }

    void PlayVideo(String videoPath) {

        File videoFile = new File(videoPath);
        videoFile.setReadable(true, false);
        Uri intentUri = Uri.fromFile(videoFile);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(intentUri, "video/mp4");
        startActivity(intent);
    }

    public void ScanAndPlayVideo(String videoSavedPath, final Context mediaScanContext) {
        MediaScannerConnection.scanFile(mediaScanContext, new String[]{videoSavedPath},
                null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        try {
                            PlayVideo(path);
                        }
                        catch (Exception err) {
                            LogManager.PrintLog("EachGridViewItem", "onScanCompleted", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                        }
                    }
                });
    }

    String GetVideoDurationTime(String videoSavedPath) {
        try {
            MediaPlayer mp = MediaPlayer.create(this, Uri.parse(videoSavedPath));
            int duration = mp.getDuration();
            mp.release();
/*convert millis to appropriate time*/
            return String.format("%02d' %02d' %02d'",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration)
            );
        }
        catch (Exception err) {
            LogManager.PrintLog("FullFilmManager", "GetVideoDurationTime", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            return "Error!";
        }
    }

    Bitmap GetVideoThumbnailImage(String videoSavedPath) {
        try {
            LogManager.PrintLog("FullFilmManager", "GetVideoThumbnailImage", "thumbnail video path: " + videoSavedPath, LOG_LEVEL_DEBUG);
            return ThumbnailUtils.createVideoThumbnail(videoSavedPath, MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        catch (Exception err) {
            LogManager.PrintLog("FullFilmManager", "GetVideoThumbnailImage", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            return null;
        }
    }

    String ParseFileName(File videoFile) {
        String originFileName = videoFile.getName();
        if(originFileName.contains("FutsalManager") && originFileName.length() == 29) {
            String dateString = originFileName.split("FutsalManager")[1];
            String year = dateString.substring(0, 2);
            String month = dateString.substring(2, 4);
            String day = dateString.substring(4, 6);
            String hour = dateString.substring(6, 8);
            String min = dateString.substring(8, 10);
            String fileNameParsed = "20" + year + "-" + month + "-" + day + " " + hour + ":" + min;
            LogManager.PrintLog("FullFilmManager", "ParseFileName", "parsed: " + fileNameParsed, LOG_LEVEL_DEBUG);
            return fileNameParsed;
        }
        else {
            return "Other Video File";
        }
    }

    File[] GetFileList(String path) {
        File[] listOfFiles = null;

        File targetDirectory = new File(path);
        listOfFiles = targetDirectory.listFiles();

        for(File indexOfFile : listOfFiles) {
            LogManager.PrintLog("FullFilmManager", "GetFilePath", "fileName: " + indexOfFile.getName(), LOG_LEVEL_DEBUG);
        }

        return listOfFiles;
    }

    public String GetFilePath() {
        File fileSavePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + File.separator + "FutsalManager");
        String savePath = fileSavePath.getPath();
        LogManager.PrintLog("FullFilmManager", "GetFilePath", "Path: " + savePath, LOG_LEVEL_DEBUG);
        if(!fileSavePath.exists()) {
            fileSavePath.mkdirs();
        }
        return savePath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
