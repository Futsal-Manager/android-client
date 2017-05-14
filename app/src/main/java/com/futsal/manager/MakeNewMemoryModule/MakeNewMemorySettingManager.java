package com.futsal.manager.MakeNewMemoryModule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.MakeVideoModule.CameraRecordManager;
import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 5. 15..
 */

public class MakeNewMemorySettingManager extends Dialog {

    ImageButton btnImageSetting;
    Button btnGoTestingLayout;
    Activity makeNewMemoryManager;

    public MakeNewMemorySettingManager(Context context, ImageButton btnImageSetting, Activity makeNewMemoryManager) {
        super(context);
        this.btnImageSetting = btnImageSetting;
        this.makeNewMemoryManager = makeNewMemoryManager;
    }

    public MakeNewMemorySettingManager(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MakeNewMemorySettingManager(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        LogManager.PrintLog("MakeNewMemorySettingManager", "dismiss", "Closing dialog", DefineManager.LOG_LEVEL_DEBUG);
        btnImageSetting.setBackgroundResource(R.drawable.before_setting);
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.make_new_memory_setting_manager);

        InitLayout();

        btnGoTestingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                OpenTestingLayout();
                makeNewMemoryManager.finish();
            }
        });
    }

    void OpenTestingLayout() {
        Intent goTestingLayout = new Intent(makeNewMemoryManager, CameraRecordManager.class);
        makeNewMemoryManager.startActivity(goTestingLayout);
    }

    void InitLayout() {
        btnGoTestingLayout = (Button)findViewById(R.id.btnGoTestingLayout);
    }
}
