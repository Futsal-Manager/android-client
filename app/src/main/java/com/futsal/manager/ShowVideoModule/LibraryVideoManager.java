package com.futsal.manager.ShowVideoModule;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.TabHost;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 5. 27..
 */

public class LibraryVideoManager extends Activity {

    TabHost tabHostLibrary;
    TabHost.TabSpec fullFilmTab, highLightFilmTab;
    LocalActivityManager localActivityManager;
    Window libraryVideoManagerWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.library_video_manager);

        libraryVideoManagerWindow = getWindow();
        libraryVideoManagerWindow.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        tabHostLibrary = (TabHost) findViewById(R.id.tabHostLibrary);

        fullFilmTab = tabHostLibrary.newTabSpec("Full Film Tab");
        highLightFilmTab = tabHostLibrary.newTabSpec("High Light Tab");

        fullFilmTab.setIndicator("Full Film");
        highLightFilmTab.setIndicator("High Light Film");

        fullFilmTab.setContent(new Intent(this, FullFilmManager.class));
        highLightFilmTab.setContent(new Intent(this, HighLightFilmManager.class));

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
    }
}
