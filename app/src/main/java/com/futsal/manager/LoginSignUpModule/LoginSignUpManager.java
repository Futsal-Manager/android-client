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
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;

/**
 * Created by stories2 on 2017. 4. 25..
 */

public class LoginSignUpManager extends Activity {

    Button btnLogIn;
    EditText etxtEmail, etxtPassword;
    Intent moveToMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_manager);

        moveToMainLayout = new Intent(this, FutsalManagerMain.class);

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
                Thread.sleep(2000);
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
