package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.content.Intent;
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
import com.futsal.manager.ShowVideoModule.LibraryVideoManager;

import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;

/**
 * Created by stories2 on 2017. 6. 6..
 */

public class LoginManager extends Activity {

    EditText etxtEmail, etxtPassword;
    TextView txtSignUp;
    Button btnLogin;
    Intent moveToMainLayout;
    CommunicationWithServer communicationWithServer;
    LoginOrSignUpProcess loginOrSignUpProcess;
    Activity loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_manager);

        InitLayout();

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent developOptionLayout = new Intent(getApplicationContext(), SignUpManager.class);
                startActivity(developOptionLayout);
            }
        });

        etxtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LogManager.PrintLog("LoginManager", "onEditorAction", "Email: " + v.getText().toString(), DefineManager.LOG_LEVEL_INFO);
                }
                return false;
            }
        });

        etxtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    LogManager.PrintLog("LoginManager", "onEditorAction", "Password: " + v.getText().toString(), DefineManager.LOG_LEVEL_INFO);
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrSignUpProcess = new LoginOrSignUpProcess(loginManager, communicationWithServer, moveToMainLayout,
                        etxtEmail.getText().toString(), etxtPassword.getText().toString());
                loginOrSignUpProcess.execute();
            }
        });
    }

    void InitLayout() {
        etxtEmail = (EditText)findViewById(R.id.etxtEmail);
        etxtPassword = (EditText)findViewById(R.id.etxtPassword);
        txtSignUp = (TextView)findViewById(R.id.txtSignUp);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        loginManager = this;

        etxtEmail.setText(TEST_ACCOUNT);
        etxtPassword.setText(TEST_ACCOUNT_PASSWORD);

        moveToMainLayout = new Intent(this, LibraryVideoManager.class);
        communicationWithServer = new CommunicationWithServer(getApplicationContext());
    }
}
