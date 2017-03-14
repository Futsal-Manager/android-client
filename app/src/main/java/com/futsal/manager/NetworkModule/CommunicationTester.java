package com.futsal.manager.NetworkModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.SearchView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 3. 12..
 */

public class CommunicationTester extends Activity {

    CommunicationWithServer communicationWithServer;
    Button btnLogIn, btnSignup, btnFileList, btnFileUpload, btnPlayVideo;
    EditText etxtUsername, etxtPassword;
    ToggleButton toggleDebugMode;
    SearchView searchHash;
    VideoView videoPlay;
    MediaController mediaController;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_tester);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnFileList = (Button) findViewById(R.id.btnFileList);
        btnFileUpload = (Button) findViewById(R.id.btnFileUpload);
        btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        etxtUsername = (EditText) findViewById(R.id.etxtUsername);
        etxtPassword = (EditText) findViewById(R.id.etxtPassword);
        toggleDebugMode = (ToggleButton) findViewById(R.id.toggleDebugMode);
        searchHash = (SearchView) findViewById(R.id.searchHash);
        videoPlay = (VideoView) findViewById(R.id.videoPlay);

        communicationWithServer = new CommunicationWithServer(getApplicationContext(), false);

        try {
            String result = java.net.URLDecoder.decode("connect.sid=s%3AqhOcJI_zRefCGFWA5sKeuSkJcJLQK2ou.1aWVFuMROowOiy0NhopnJXMJGiaVENE0K8p1ry1Ylu4","UTF-8");
            Log.d(getString(R.string.app_name), "result: " + result);
        }
        catch (Exception err) {
            Log.d(getString(R.string.app_name), "ASDFLK: " + err.getMessage());
        }
        searchHash.setQuery(communicationWithServer.GetHashCode(), false);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationWithServer.AuthLogin(etxtUsername.getText().toString(), etxtPassword.getText().toString());
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationWithServer.AuthSignup(etxtUsername.getText().toString(), etxtPassword.getText().toString());
            }
        });

        btnFileList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationWithServer.FileList();
            }
        });

        btnFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savePath = Environment.getExternalStorageDirectory().toString() + "/testVideo3.mp4";
                //communicationWithServer.UploadFile(Uri.parse(savePath));
                //communicationWithServer.UploadFileTester(Uri.parse(savePath));
                //communicationWithServer.UploadFileTester2(Uri.parse(savePath));
                communicationWithServer.UploadFileTester3(Uri.parse(savePath));
            }
        });

        btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webVideoUrl = communicationWithServer.GetFileUrl();
                Log.d(getString(R.string.app_name), "web video url: " + webVideoUrl);
                PlayVideo(webVideoUrl);
            }
        });

        toggleDebugMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                communicationWithServer.SetMode(b);
                Log.d(getString(R.string.app_name), "Mode: " + b);
            }
        });

        searchHash.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                communicationWithServer.SetHashCode(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
    }
    public void PlayVideo(String videopath) {
        Log.e("entered", "playvide");
        Log.e("path is", "" + videopath);
        try {
            progressDialog = ProgressDialog.show(CommunicationTester.this, "",
                    "Buffering video...", false);
            progressDialog.setCancelable(true);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);

            mediaController = new MediaController(getApplicationContext());

            Uri video = Uri.parse(videopath);
            videoPlay.setMediaController(mediaController);
            videoPlay.setVideoURI(video);

            videoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoPlay.start();
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            System.out.println("Video Play Error :" + e.getMessage());
        }

    }
}
