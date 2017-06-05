package com.futsal.manager.LoginSignUpModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.futsal.manager.NetworkModule.CommunicationWithServer;
import com.futsal.manager.R;
import com.futsal.manager.ShowVideoModule.LibraryVideoManager;

import static com.futsal.manager.DefineManager.TEST_ACCOUNT;
import static com.futsal.manager.DefineManager.TEST_ACCOUNT_PASSWORD;
import static com.futsal.manager.DefineManager.TEST_TEAM_NAME;

/**
 * Created by stories2 on 2017. 4. 25..
 */

public class LoginSignUpManager extends Activity {

    Button btnLogIn, btnSignUp;
    EditText etxtEmail, etxtPassword, etxtTeam;
    Intent moveToMainLayout;
    CommunicationWithServer communicationWithServer;
    LoginOrSignUpProcess loginOrSignUpProcess;
    SignUpProcess signUpProcess;
    Activity loginSignUpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_manager);

        moveToMainLayout = new Intent(this, LibraryVideoManager.class);
        communicationWithServer = new CommunicationWithServer(getApplicationContext());

        loginSignUpManager = this;

        btnLogIn = (Button)findViewById(R.id.btnLogIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        etxtEmail = (EditText) findViewById(R.id.etxtEmail);
        etxtPassword = (EditText) findViewById(R.id.etxtPassword);
        etxtTeam = (EditText) findViewById(R.id.etxtTeam);

        etxtEmail.setText(TEST_ACCOUNT);
        etxtPassword.setText(TEST_ACCOUNT_PASSWORD);
        etxtTeam.setText(TEST_TEAM_NAME);

//        loginOrSignUpProcess = new LoginOrSignUpProcess(loginSignUpManager);
//        loginOrSignUpProcess.execute();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrSignUpProcess = new LoginOrSignUpProcess(loginSignUpManager, communicationWithServer, moveToMainLayout);
                loginOrSignUpProcess.execute();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpProcess = new SignUpProcess(loginSignUpManager, communicationWithServer,
                        etxtEmail.getText().toString(), etxtPassword.getText().toString(), etxtTeam.getText().toString());
                signUpProcess.execute();
            }
        });
    }


}
