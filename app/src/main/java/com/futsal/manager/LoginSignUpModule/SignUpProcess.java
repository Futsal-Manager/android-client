package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationWithServer;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.NOT_SIGN_UP;
import static com.futsal.manager.DefineManager.SIGN_UP_FAILURE;
import static com.futsal.manager.DefineManager.SIGN_UP_SUCCESS;

/**
 * Created by stories2 on 2017. 6. 5..
 */

public class SignUpProcess extends AsyncTask<Void, Void, Void> {

    ProgressDialog signUpDialog;
    Activity loginSignUpManager;
    CommunicationWithServer communicationWithServer;
    String userName, userPassword, team;

    public SignUpProcess() {
        super();
    }

    public SignUpProcess(Activity loginSignUpManager, CommunicationWithServer communicationWithServer, String userName, String userPassword) {
        this.loginSignUpManager = loginSignUpManager;
        this.communicationWithServer = communicationWithServer;
        this.userName = userName;
        this.userPassword = userPassword;

        signUpDialog = new ProgressDialog(loginSignUpManager);
    }

    public SignUpProcess(Activity loginSignUpManager, CommunicationWithServer communicationWithServer, String userName, String userPassword, String team) {
        this.loginSignUpManager = loginSignUpManager;
        this.communicationWithServer = communicationWithServer;
        this.userName = userName;
        this.userPassword = userPassword;
        this.team = team;

        LogManager.PrintLog("SignUpProcess", "SignUpProcess", "Send data info: " + userName + " " + userPassword + " " + team, LOG_LEVEL_INFO);

        signUpDialog = new ProgressDialog(loginSignUpManager);
    }

    @Override
    protected Void doInBackground(Void... params) {
        LogManager.PrintLog("SignUpProcess", "doInBackground", "Start Auth Sign Up", LOG_LEVEL_INFO);
        //communicationWithServer.AuthSignup(userName, userPassword);
        communicationWithServer.AuthSignupVer2(userName, userPassword, team);
        while(communicationWithServer.GetSignUpStatusVer2() == NOT_SIGN_UP) {
            try {
                Thread.sleep(1);
            }
            catch (Exception err) {
                LogManager.PrintLog("SignUpProcess", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        signUpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        signUpDialog.setMessage(loginSignUpManager.getString(R.string.signingUp));

        signUpDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            signUpDialog.dismiss();

            if(communicationWithServer.GetSignUpStatusVer2() == SIGN_UP_SUCCESS) {

                LogManager.PrintLog("SignUpProcess", "doInBackground", "Sign Up success", LOG_LEVEL_INFO);
                AlertDialog.Builder signUpSuccessMessageBuilder = new AlertDialog.Builder(loginSignUpManager);
                signUpSuccessMessageBuilder.setMessage(loginSignUpManager.getString(R.string.signUpSuccessMessage));
                signUpSuccessMessageBuilder.setPositiveButton(R.string.whatEver, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog signUpSuccessDialog = signUpSuccessMessageBuilder.create();
                signUpSuccessDialog.show();
            }
            else if(communicationWithServer.GetSignUpStatusVer2() == SIGN_UP_FAILURE){

                LogManager.PrintLog("SignUpProcess", "doInBackground", "Sign Up fail", LOG_LEVEL_INFO);
                AlertDialog.Builder signUpFailureSuccessMessageBuilder = new AlertDialog.Builder(loginSignUpManager);
                signUpFailureSuccessMessageBuilder.setMessage(loginSignUpManager.getString(R.string.signUpFailureMessage));
                signUpFailureSuccessMessageBuilder.setPositiveButton(R.string.whatEver, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog signUpFailureDialog = signUpFailureSuccessMessageBuilder.create();
                signUpFailureDialog.show();

            }
        }
        catch (Exception err) {
            LogManager.PrintLog("SignUpProcess", "onPostExecute", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
        }
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
