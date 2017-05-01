package com.futsal.manager.DevModule;

import android.app.Activity;
import android.os.Bundle;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 5. 2..
 */

public class DevelopModeManager extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.develop_mode_manager);
    }
}
