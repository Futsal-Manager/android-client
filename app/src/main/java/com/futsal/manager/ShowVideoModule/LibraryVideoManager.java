package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.DevModule.DevelopModeManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.LoginSignUpModule.LoginManager;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_TEAM_NAME;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class LibraryVideoManager extends Activity {

    TabHost tabHostLibrary;
    TabHost.TabSpec fullFilmTab, highLightFilmTab;
    LocalActivityManager localActivityManager;
    Window libraryVideoManagerWindow;
    ImageButton imgBtnProfile;
    ImageView imgExitUserInfo, imgVideoUploadMinimize;
    TextView txtUserName, txtUserEmail;
    Button btnLogOut, btnVideoUploadCancel, btnVideoUploadViewClose;
    RelativeLayout userInfoLayout, layoutVideoUploadBig, layoutVideoUploadSmall;
    View layoutVideoUploadBigView, layoutVideoUploadSmallView;

    boolean isVideoUploadingViewShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.library_video_manager);

        libraryVideoManagerWindow = getWindow();
        libraryVideoManagerWindow.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        InitLayout();

        try {

            localActivityManager = new LocalActivityManager(this, true);
            localActivityManager.dispatchCreate(savedInstanceState);
            tabHostLibrary.setup(localActivityManager);
            tabHostLibrary.addTab(fullFilmTab);
            tabHostLibrary.addTab(highLightFilmTab);
        }
        catch (Exception err) {
            LogManager.PrintLog("LibraryVideoManager", "onCreate", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }

        tabHostLibrary.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                localActivityManager.dispatchResume();
            }
        });

        imgBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoLayout.setVisibility(View.VISIBLE);
            }
        });

        imgBtnProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent developOptionLayout = new Intent(getApplicationContext(), DevelopModeManager.class);
                developOptionLayout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(developOptionLayout);
                return false;
            }
        });

        imgExitUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoLayout.setVisibility(View.INVISIBLE);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginLayout = new Intent(getApplicationContext(), LoginManager.class);
                startActivity(loginLayout);
                finish();
            }
        });

        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layoutVideoUploadSmallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVideoUploadingViewShowing != true) {
                    isVideoUploadingViewShowing = !isVideoUploadingViewShowing;
                    layoutVideoUploadSmall.setVisibility(View.INVISIBLE);
                    layoutVideoUploadBig.setVisibility(View.VISIBLE);
                }
            }
        });

        imgVideoUploadMinimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseVideoUploadView();
            }
        });

        layoutVideoUploadBigView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnVideoUploadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnVideoUploadViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseVideoUploadView();
            }
        });
    }

    void CloseVideoUploadView() {
        if(isVideoUploadingViewShowing) {
            isVideoUploadingViewShowing = !isVideoUploadingViewShowing;
            layoutVideoUploadSmall.setVisibility(View.VISIBLE);
            layoutVideoUploadBig.setVisibility(View.INVISIBLE);
        }
    }

    void InitLayout() {

        tabHostLibrary = (TabHost) findViewById(R.id.tabHostLibrary);
        imgBtnProfile = (ImageButton) findViewById(R.id.imgBtnProfile);
        userInfoLayout = (RelativeLayout)findViewById(R.id.userInfoLayout);
        layoutVideoUploadBig = (RelativeLayout)findViewById(R.id.layoutVideoUploadBig);
        layoutVideoUploadSmall = (RelativeLayout)findViewById(R.id.layoutVideoUploadSmall);
        imgExitUserInfo = (ImageView)findViewById(R.id.imgExitUserInfo);
        imgVideoUploadMinimize = (ImageView)findViewById(R.id.imgVideoUploadMinimize);
        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtUserEmail = (TextView)findViewById(R.id.txtUserEmail);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        btnVideoUploadCancel = (Button)findViewById(R.id.btnVideoUploadCancel);
        btnVideoUploadViewClose = (Button)findViewById(R.id.btnVideoUploadViewClose);
        layoutVideoUploadBigView = (View)findViewById(R.id.layoutVideoUploadBigView);
        layoutVideoUploadSmallView = (View)findViewById(R.id.layoutVideoUploadSmallView);

        fullFilmTab = tabHostLibrary.newTabSpec("Full Film Tab");
        highLightFilmTab = tabHostLibrary.newTabSpec("High Light Tab");
        isVideoUploadingViewShowing = false;

        fullFilmTab.setIndicator("Full Film");
        highLightFilmTab.setIndicator("High Light Film");

        fullFilmTab.setContent(new Intent(this, FullFilmManager.class));
        highLightFilmTab.setContent(new Intent(this, HighLightFilmManager.class));

        userInfoLayout.setVisibility(View.INVISIBLE);
        layoutVideoUploadBig.setVisibility(View.INVISIBLE);

        txtUserName.setText(TEST_TEAM_NAME);
        txtUserEmail.setText(TEST_ACCOUNT);
    }
}
