package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.futsal.manager.FutsalManagerMain;
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
 * Created by stories2 on 2017. 4. 25..
 */

public class LoginSignUpManager extends Activity {

    Button btnLogIn;
    EditText etxtEmail, etxtPassword;
    Intent moveToMainLayout;
    CommunicationWithServer communicationWithServer;
    LoginOrSignUpProcess loginOrSignUpProcess;
    Activity loginSignUpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_manager);

        moveToMainLayout = new Intent(this, FutsalManagerMain.class);
        communicationWithServer = new CommunicationWithServer(getApplicationContext());

        loginSignUpManager = this;

        btnLogIn = (Button)findViewById(R.id.btnLogIn);
        etxtEmail = (EditText) findViewById(R.id.etxtEmail);
        etxtPassword = (EditText) findViewById(R.id.etxtPassword);

        etxtEmail.setText(TEST_ACCOUNT);
        etxtPassword.setText(TEST_ACCOUNT_PASSWORD);

        loginOrSignUpProcess = new LoginOrSignUpProcess(loginSignUpManager);
        loginOrSignUpProcess.execute();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrSignUpProcess = new LoginOrSignUpProcess(loginSignUpManager);
                loginOrSignUpProcess.execute();
            }
        });
    }

    public class LoginOrSignUpProcess extends AsyncTask<Void, Void, Integer>{

        ProgressDialog loginOrSignUpProcess;
        Activity loginOrSignUpProcessActivity;

        public LoginOrSignUpProcess(Activity loginOrSignUpProcessActivity) {
            super();

            loginOrSignUpProcess = new ProgressDialog(loginOrSignUpProcessActivity);
            this.loginOrSignUpProcessActivity = loginOrSignUpProcessActivity;
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
            loginOrSignUpProcess.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loginOrSignUpProcess.setMessage(getString(R.string.loginChecking));

            loginOrSignUpProcess.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer loginStatus) {
            loginOrSignUpProcess.dismiss();
            if(loginStatus == LOGIN_SUCCESS) {
                LogManager.PrintLog("LoginOrSignUpProcess", "onPostExecute", "Login success", LOG_LEVEL_INFO);
                startActivity(moveToMainLayout);
                finish();
            }
            else if(loginStatus == LOGIN_FAILURE) {
                LogManager.PrintLog("LoginOrSignUpProcess", "onPostExecute", "Login fail", LOG_LEVEL_INFO);
                AlertDialog.Builder loginFailureMessageBuilder = new AlertDialog.Builder(loginOrSignUpProcessActivity);
                loginFailureMessageBuilder.setMessage(getString(R.string.LoginFailureMessage));
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
}
