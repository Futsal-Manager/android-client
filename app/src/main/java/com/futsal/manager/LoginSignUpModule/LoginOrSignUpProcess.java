package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationWithServer;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.LOGIN_FAILURE;
import static com.futsal.manager.DefineManager.LOGIN_SUCCESS;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.NOT_LOGGED_IN;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;

/**
 * Created by stories2 on 2017. 6. 5..
 */

public class LoginOrSignUpProcess extends AsyncTask<Void, Void, Integer> {

    ProgressDialog loginOrSignUpProcess;
    Activity loginOrSignUpProcessActivity;
    CommunicationWithServer communicationWithServer;
    Intent moveToMainLayout;

    public LoginOrSignUpProcess(Activity loginOrSignUpProcessActivity) {
        super();

        loginOrSignUpProcess = new ProgressDialog(loginOrSignUpProcessActivity);
        this.loginOrSignUpProcessActivity = loginOrSignUpProcessActivity;
    }

    public LoginOrSignUpProcess(Activity loginOrSignUpProcessActivity, CommunicationWithServer communicationWithServer, Intent moveToMainLayout) {
        super();

        loginOrSignUpProcess = new ProgressDialog(loginOrSignUpProcessActivity);
        this.loginOrSignUpProcessActivity = loginOrSignUpProcessActivity;
        this.communicationWithServer = communicationWithServer;
        this.moveToMainLayout = moveToMainLayout;
    }


    @Override
    protected Integer doInBackground(Void... params) {

        try{
            communicationWithServer.AuthLoginVer2(TEST_ACCOUNT, TEST_ACCOUNT_PASSWORD);
            LogManager.PrintLog("LoginSignUpManager", "doInBackground", "waiting server response", LOG_LEVEL_INFO);
            while(communicationWithServer.GetLoginStatusVer2() == NOT_LOGGED_IN) {
                try {
                    Thread.sleep(1);
                }
                catch (Exception err) {
                    LogManager.PrintLog("LoginSignUpManager", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    break;
                }
            }
            LogManager.PrintLog("LoginSignUpManager", "doInBackground", "server response accepted", LOG_LEVEL_INFO);
        }
        catch (Exception err) {
            LogManager.PrintLog("LoginOrSignUpProcess", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
        return communicationWithServer.GetLoginStatusVer2();
    }

    @Override
    protected void onPreExecute() {
        try {
            loginOrSignUpProcess.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loginOrSignUpProcess.setMessage(loginOrSignUpProcessActivity.getString(R.string.loginChecking));

            loginOrSignUpProcess.show();
        }
        catch (Exception err) {

        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer loginStatus) {
        loginOrSignUpProcess.dismiss();
        LogManager.PrintLog("LoginOrSignUpProcess", "onPostExecute", "Login Status: " + loginStatus, LOG_LEVEL_INFO);
        if(loginStatus == LOGIN_SUCCESS) {
            LogManager.PrintLog("LoginOrSignUpProcess", "onPostExecute", "Login success", LOG_LEVEL_INFO);
            loginOrSignUpProcessActivity.startActivity(moveToMainLayout);
            loginOrSignUpProcessActivity.finish();
        }
        else if(loginStatus == LOGIN_FAILURE) {
            LogManager.PrintLog("LoginOrSignUpProcess", "onPostExecute", "Login fail", LOG_LEVEL_INFO);
            AlertDialog.Builder loginFailureMessageBuilder = new AlertDialog.Builder(loginOrSignUpProcessActivity);
            loginFailureMessageBuilder.setMessage(loginOrSignUpProcessActivity.getString(R.string.LoginFailureMessage));
            loginFailureMessageBuilder.setPositiveButton(R.string.omg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog loginFailureMessageDialog = loginFailureMessageBuilder.create();
            loginFailureMessageDialog.show();
        }
        super.onPostExecute(loginStatus);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Integer integer) {
        super.onCancelled(integer);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
