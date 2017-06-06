package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.NetworkModule.CommunicationWithServer;
import com.futsal.manager.R;

import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;
import static com.futsal.manager.DefineManager.TEST_TEAM_NAME;

/**
 * Created by stories2 on 2017. 6. 6..
 */

public class SignUpManager extends Activity {

    TextView txtSignIn;
    EditText etxtTeam, etxtEmail, etxtPassword;
    Button btnSignUp;
    Activity signUpManager;
    SignUpProcess signUpProcess;
    CommunicationWithServer communicationWithServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_manager);

        InitLayout();

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etxtTeam.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LogManager.PrintLog("SignUpManager", "onEditorAction", "Team: " + v.getText().toString(), DefineManager.LOG_LEVEL_INFO);
                }
                return false;
            }
        });

        etxtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LogManager.PrintLog("SignUpManager", "onEditorAction", "Email: " + v.getText().toString(), DefineManager.LOG_LEVEL_INFO);
                }
                return false;
            }
        });

        etxtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LogManager.PrintLog("SignUpManager", "onEditorAction", "Password: " + v.getText().toString(), DefineManager.LOG_LEVEL_INFO);
                }
                return false;
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpProcess = new SignUpProcess(signUpManager, communicationWithServer,
                        etxtEmail.getText().toString(), etxtPassword.getText().toString(), etxtTeam.getText().toString());
                signUpProcess.execute();
            }
        });
    }

    void InitLayout() {
        txtSignIn = (TextView)findViewById(R.id.txtSignIn);
        etxtTeam = (EditText)findViewById(R.id.etxtTeam);
        etxtEmail = (EditText)findViewById(R.id.etxtEmail);
        etxtPassword = (EditText)findViewById(R.id.etxtPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        signUpManager = this;

        etxtEmail.setText(TEST_ACCOUNT);
        etxtPassword.setText(TEST_ACCOUNT_PASSWORD);
        etxtTeam.setText(TEST_TEAM_NAME);

        communicationWithServer = new CommunicationWithServer(getApplicationContext());
    }
}
