package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 3. 27..
 */

public class ShowVideoManager extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    VideoView videoWebPlayer;
    MediaController mediaController;
    ProgressDialog progressDialog;
    Intent videoUrlData;
    String nowVideoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_video_manager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoUrlData = getIntent();

        videoWebPlayer = (VideoView) findViewById(R.id.videoWebPlayer);
        videoWebPlayer.setOnCompletionListener(this);
        videoWebPlayer.setOnErrorListener(this);

        nowVideoUrl = videoUrlData.getStringExtra("videoUrl");
        LogManager.PrintLog("ShowVideoManager", "onCreate", "will play video Url: " + nowVideoUrl, DefineManager.LOG_LEVEL_INFO);

        PlayVideo(nowVideoUrl);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }

    public void PlayVideo(String videopath) {
        Log.e("entered", "playvide");
        Log.e("path is", "" + videopath);
        try {
            progressDialog = ProgressDialog.show(ShowVideoManager.this, "",
                    "Buffering video...", false);
            progressDialog.setCancelable(true);
            getWindow().setFormat(PixelFormat.TRANSLUCENT);

            mediaController = new MediaController(getApplicationContext());

            Uri video = Uri.parse(videopath);
            videoWebPlayer.setMediaController(mediaController);
            videoWebPlayer.setVideoURI(video);


            videoWebPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoWebPlayer.start();
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            LogManager.PrintLog("ShowVideoManager", "PlayVideo", "Error: " + e.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        return true;
    }
}
