package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.futsal.manager.EmbeddedCommunicationModule.EmbeddedSystemFinder;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationWithServer;
import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.List;

import static com.futsal.manager.DefineManager.LIBRARY_TYPE_SHARE;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.NOT_LOADED;
import static com.futsal.manager.DefineManager.VIDEO_SERVER_DOMAIN_NAME;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class HighLightFilmManager extends Activity {

    GridView highLightGridView;
    List<EachGridViewItemModel> highLightFilmList;
    FloatingActionButton floatingActionBtnNewMemory;
    List<String> serverSavedFileList;
    CommunicationWithServer communicationWithServer;
    EachGridViewItem highLightGridViewAdapater;
    CrawlFileListFromServerProcess crawlFileListFromServerProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_light_film_manager);

        highLightGridView = (GridView)findViewById(R.id.gridHighLightFilm);
        floatingActionBtnNewMemory = (FloatingActionButton)findViewById(R.id.floatingActionBtnNewMemory);

        highLightFilmList = new ArrayList<EachGridViewItemModel>();

        highLightGridViewAdapater = new EachGridViewItem(highLightFilmList, getApplicationContext(), R.layout.library_video_manager_item);
        communicationWithServer = new CommunicationWithServer(getApplicationContext());

        highLightGridView.setAdapter(highLightGridViewAdapater);

        floatingActionBtnNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordVideoLayout = new Intent(getApplicationContext(), EmbeddedSystemFinder.class);
                startActivity(recordVideoLayout);
            }
        });

        crawlFileListFromServerProcess = new CrawlFileListFromServerProcess(this, communicationWithServer, serverSavedFileList, highLightGridViewAdapater,
                 highLightGridView);
        crawlFileListFromServerProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        highLightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EachGridViewItemModel eachGridViewItemModel = highLightFilmList.get(position);
                PlayVideo(eachGridViewItemModel.GetVideoOriginName());
            }
        });
    }

    void PlayVideo(String videoPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class CrawlFileListFromServerProcess extends AsyncTask<Void, Void, Void>{

        Activity highLightFilmManager;
        ProgressDialog crawlFileListDialog;
        CommunicationWithServer communicationWithServer;
        List<String> serverSavedFileList;
        EachGridViewItem highLightGridViewAdapater;
        GridView highLightGridView;

        public CrawlFileListFromServerProcess(Activity highLightFilmManager, CommunicationWithServer communicationWithServer,
                                              List<String> serverSavedFileList, EachGridViewItem highLightGridViewAdapater,
                                              GridView highLightGridView) {
            this.highLightFilmManager = highLightFilmManager;
            this.communicationWithServer = communicationWithServer;
            this.serverSavedFileList = serverSavedFileList;
            this.highLightGridViewAdapater = highLightGridViewAdapater;
            this.highLightGridView = highLightGridView;

            crawlFileListDialog = new ProgressDialog(highLightFilmManager);

            if(serverSavedFileList != null) {
                serverSavedFileList.clear();
                serverSavedFileList = null;
            }

            highLightFilmList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                communicationWithServer.FileList();
                while(communicationWithServer.GetFileStatus() == NOT_LOADED) {
                    try {
                        Thread.sleep(1);
                    }
                    catch (Exception err) {
                        LogManager.PrintLog("HighLightFilmManager", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                serverSavedFileList = communicationWithServer.GetFileUrls();
                if(serverSavedFileList != null) {
                    LogManager.PrintLog("HighLightFilmManager", "doInBackground", "File list loaded", LOG_LEVEL_INFO);

                    for(String eachFileName : serverSavedFileList) {
                        LogManager.PrintLog("HighLightFilmManager", "doInBackground", "file url: " + eachFileName, LOG_LEVEL_DEBUG);

                        EachGridViewItemModel eachGridViewItemModel = new EachGridViewItemModel();
                        eachGridViewItemModel.SetVideoName(ParseFileName(eachFileName));
                        eachGridViewItemModel.SetVideoOriginName(eachFileName);
                        eachGridViewItemModel.SetSubBtnType(LIBRARY_TYPE_SHARE);
                        eachGridViewItemModel.SetMediaScanContext(getApplicationContext());

                        highLightFilmList.add(eachGridViewItemModel);
                    }
                }
                highLightGridViewAdapater = new EachGridViewItem(highLightFilmList, getApplicationContext(), R.layout.library_video_manager_item);
            }
            catch (Exception err) {
                LogManager.PrintLog("HighLightFilmManager", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            crawlFileListDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            crawlFileListDialog.setMessage(getString(R.string.loadingMyFileListFromServer));

            crawlFileListDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            crawlFileListDialog.dismiss();
            highLightGridView.setAdapter(highLightGridViewAdapater);
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

        String ParseFileName(String originFileName) {
            originFileName = originFileName.replace("https://" + VIDEO_SERVER_DOMAIN_NAME + "/", "");
            LogManager.PrintLog("HighLightFilmManager", "ParseFileName", "replaced: " + originFileName, LOG_LEVEL_DEBUG);
            if(originFileName.contains("FutsalManager") && originFileName.length() == 29) {
                String dateString = originFileName.split("FutsalManager")[1];
                String year = dateString.substring(0, 2);
                String month = dateString.substring(2, 4);
                String day = dateString.substring(4, 6);
                String hour = dateString.substring(6, 8);
                String min = dateString.substring(8, 10);
                String fileNameParsed = "20" + year + "-" + month + "-" + day + " " + hour + ":" + min;
                LogManager.PrintLog("HighLightFilmManager", "ParseFileName", "parsed: " + fileNameParsed, LOG_LEVEL_DEBUG);
                return fileNameParsed;
            }
            else {
                return "Other Video File";
            }
        }
    }
}
