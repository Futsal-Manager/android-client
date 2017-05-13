package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 5. 14..
 */

public class MakeNewMemoryManager extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_memory_manager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
