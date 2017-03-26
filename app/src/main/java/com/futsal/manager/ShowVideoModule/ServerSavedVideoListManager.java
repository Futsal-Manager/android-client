package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.futsal.manager.DataModelModule.EachCardViewItems;
import com.futsal.manager.DataModelModule.RecyclerAdapter;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationWithServer;
import com.futsal.manager.R;

import java.util.ArrayList;
import java.util.List;

import static com.futsal.manager.DefineManager.CALLED_BY_SERVER_SAVED_LIST_ACTIVITY;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;
import static com.futsal.manager.DefineManager.WAIT_FOR_GET_FILE_LIST;
import static com.futsal.manager.DefineManager.WAIT_FOR_LOGIN;

/**
 * Created by stories2 on 2017. 3. 26..
 */

public class ServerSavedVideoListManager extends Activity implements Runnable{

    RecyclerView recyFunctionList;
    RecyclerView.LayoutManager recyFunctionListLayoutManager;
    List<EachCardViewItems> eachCardViewItems;
    CommunicationWithServer communicationWithServer;
    RecyclerAdapter recyFunctionListAdapter;
    List<String> serverSavedFileList;
    int networkOrderStatus;
    Thread loginLoopThread, fileListLoopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_saved_video_list_manager);

        recyFunctionList = (RecyclerView) findViewById(R.id.recyFunctionList);

        recyFunctionList.setHasFixedSize(true);
        recyFunctionListLayoutManager = new LinearLayoutManager(this);
        recyFunctionList.setLayoutManager(recyFunctionListLayoutManager);

        communicationWithServer = new CommunicationWithServer(getApplicationContext());
        ServerLogin();

        eachCardViewItems = new ArrayList<>();
        //CrawServerSavedVideoList();
        /*eachCardViewItems.add(new EachCardViewItems(R.mipmap.ic_launcher, "test1"));
        eachCardViewItems.add(new EachCardViewItems(R.mipmap.ic_launcher, "test2"));*/
        recyFunctionListAdapter = new RecyclerAdapter(getApplicationContext(),eachCardViewItems,R.layout.futsal_manager_main, CALLED_BY_SERVER_SAVED_LIST_ACTIVITY);

        recyFunctionList.setAdapter(recyFunctionListAdapter);
    }

    public void ServerLogin() {
        networkOrderStatus = WAIT_FOR_LOGIN;
        communicationWithServer.AuthLogin(TEST_ACCOUNT, TEST_ACCOUNT_PASSWORD);
        loginLoopThread = new Thread(this);
        loginLoopThread.start();
    }

    public void CrawServerSavedVideoList() {
        try {
            LogManager.PrintLog("ServerSavedVideoListManager", "CrawServerSavedVideoList", "Get Server File List", LOG_LEVEL_INFO);
            networkOrderStatus = WAIT_FOR_GET_FILE_LIST;
            communicationWithServer.FileList();
            fileListLoopThread = new Thread(this);
            fileListLoopThread.start();
            fileListLoopThread.join();
            for(String eachFileSavedUrl : serverSavedFileList) {
                eachCardViewItems.add(new EachCardViewItems(R.mipmap.ic_launcher, eachFileSavedUrl));
                LogManager.PrintLog("ServerSavedVideoListManager", "CrawServerSavedVideoList", "Data Added: " + eachFileSavedUrl, LOG_LEVEL_INFO);
            }
            recyFunctionListAdapter.notifyDataSetChanged();
            LogManager.PrintLog("ServerSavedVideoListManager", "CrawServerSavedVideoList", "Data Setted", LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("ServerSavedVideoListManager", "CrawServerSavedVideoList", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
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
                        LogManager.PrintLog("ServerSavedVideoListManager", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                CrawServerSavedVideoList();
                break;
            case WAIT_FOR_GET_FILE_LIST:
                while(serverSavedFileList == null) {
                    try {
                        serverSavedFileList = communicationWithServer.GetFileUrls();
                        Thread.sleep(1);
                    }
                    catch (Exception err) {
                        LogManager.PrintLog("ServerSavedVideoListManager", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    }
                }
                break;
        }
    }
}
