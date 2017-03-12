package com.futsal.manager.NetworkModule;

import android.content.Context;
import android.util.Log;

import com.futsal.manager.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by stories2 on 2017. 3. 11..
 */

public class CommunicationWithServer{

    Context applicationContext;

    public CommunicationWithServer(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void AuthLogin(String username, String password) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = Retrofit2NetworkInterface.retrofit.create(Retrofit2NetworkInterface.class);
        AuthLoginRequest authLoginRequest = new AuthLoginRequest();
        authLoginRequest.SetUsername(username);
        authLoginRequest.SetPassword(password);
        Call<AuthLoginResponse> calling = retrofit2NetworkInterface.AuthLoginProcess("application/json", authLoginRequest);
        calling.enqueue(new Callback<AuthLoginResponse>() {
            @Override
            public void onResponse(Call<AuthLoginResponse> call, Response<AuthLoginResponse> response) {
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body().GetMsg());
            }

            @Override
            public void onFailure(Call<AuthLoginResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());
            }
        });
    }
}

interface Retrofit2NetworkInterface {

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @POST("auth/login")
    Call<AuthLoginResponse> AuthLoginProcess(@Header("Content-Type") String contentType, @Body AuthLoginRequest responseData);
}
