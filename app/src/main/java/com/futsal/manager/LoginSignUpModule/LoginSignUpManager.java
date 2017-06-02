package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.app.ProgressDialog;
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

import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_manager);

        moveToMainLayout = new Intent(this, FutsalManagerMain.class);
        communicationWithServer = new CommunicationWithServer(getApplicationContext());

        btnLogIn = (Button)findViewById(R.id.btnLogIn);
        etxtEmail = (EditText) findViewById(R.id.etxtEmail);
        etxtPassword = (EditText) findViewById(R.id.etxtPassword);

        etxtEmail.setText(TEST_ACCOUNT);
        etxtPassword.setText(TEST_ACCOUNT_PASSWORD);

        LoginOrSignUpProcess loginOrSignUpProcess = new LoginOrSignUpProcess(this);
        loginOrSignUpProcess.execute();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public class LoginOrSignUpProcess extends AsyncTask<Void, Void, Void>{

        ProgressDialog loginOrSignUpProcess;

        public LoginOrSignUpProcess(Activity loginOrSignUpProcessActivity) {
            super();

            loginOrSignUpProcess = new ProgressDialog(loginOrSignUpProcessActivity);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                communicationWithServer.AuthLogin(TEST_ACCOUNT, TEST_ACCOUNT_PASSWORD);
                Runnable waitForLogin = new Runnable() {
                    @Override
                    public void run() {
                        LogManager.PrintLog("LoginSignUpManager", "run", "sigining", LOG_LEVEL_INFO);
                        while(!communicationWithServer.GetLoginStatus()) {
                            try {
                                Thread.sleep(1);
                            }
                            catch (Exception err) {
                                LogManager.PrintLog("LoginSignUpManager", "run", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                                break;
                            }
                        }
                    }
                };
            }
            catch (Exception err) {
                LogManager.PrintLog("LoginOrSignUpProcess", "doInBackground", "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            loginOrSignUpProcess.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loginOrSignUpProcess.setMessage("사용자 확인 중");

            loginOrSignUpProcess.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loginOrSignUpProcess.dismiss();
            startActivity(moveToMainLayout);
            finish();
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
}
