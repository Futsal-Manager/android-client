package com.futsal.manager.LoadingModule;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.LoginSignUpModule.LoginManager;
import com.futsal.manager.R;

import java.util.Arrays;

import static com.futsal.manager.DefineManager.ANDROID_VERSION_OF_MARSHMALLOW;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.PERMISSION_REQUESTED_ORDER;

/**
 * Created by stories2 on 2017. 4. 11..
 */

public class PermissionManager extends AppCompatActivity implements Animation.AnimationListener{

    String[] needPermissionList;
    PermissionManagerProcesser permissionManagerProcesser;
    Animation loadingScreenFadeOut;
    RelativeLayout loadingScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_manager);
        LogManager.PrintLog("PermissionManager", "onCreate", "Android Version: " + Build.VERSION.SDK_INT, DefineManager.LOG_LEVEL_INFO);

        permissionManagerProcesser = new PermissionManagerProcesser();

        loadingScreenFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        loadingScreenFadeOut.setAnimationListener(this);

        loadingScreen = (RelativeLayout) findViewById(R.id.loadingScreen);

        if(Build.VERSION.SDK_INT >= ANDROID_VERSION_OF_MARSHMALLOW) {
            needPermissionList = new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE
            };
            if(!permissionManagerProcesser.CheckNeedPermissionStatus(getApplicationContext(), needPermissionList)) {
                ActivityCompat.requestPermissions(this, needPermissionList, PERMISSION_REQUESTED_ORDER);
            }
            else {
                //MoveToMainActivity();
                loadingScreen.setAnimation(loadingScreenFadeOut);
            }
        }
        else {
            //MoveToMainActivity();
            loadingScreen.setAnimation(loadingScreenFadeOut);
        }
    }

    void MoveToMainActivity() {
        Intent moveToMainActivityIntent = new Intent(this, LoginManager.class);
        startActivity(moveToMainActivityIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isPermissionGrantedSuccessfully = true;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUESTED_ORDER:
                LogManager.PrintLog("PermissionManager", "onRequestPermissionsResult", "permissions: " + Arrays.toString(permissions), LOG_LEVEL_INFO);
                LogManager.PrintLog("PermissionManager", "onRequestPermissionsResult", "grantResults: " + Arrays.toString(grantResults), LOG_LEVEL_INFO);
                for(int indexOfGrantResult : grantResults) {
                    if(indexOfGrantResult == -1) {
                        isPermissionGrantedSuccessfully = false;
                    }
                }
                if(isPermissionGrantedSuccessfully) {
                    MoveToMainActivity();
                }
                else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        MoveToMainActivity();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}