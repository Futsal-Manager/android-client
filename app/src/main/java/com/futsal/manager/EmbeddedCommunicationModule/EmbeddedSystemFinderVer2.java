package com.futsal.manager.EmbeddedCommunicationModule;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.futsal.manager.R;

/**
 * Created by stories2 on 2017. 6. 14..
 */

public class EmbeddedSystemFinderVer2 extends Dialog {

    public EmbeddedSystemFinderVer2(Context context) {
        super(context);
    }

    public EmbeddedSystemFinderVer2(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EmbeddedSystemFinderVer2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.embedded_system_finder_ver2);
    }
}
