package com.futsal.manager.NetworkModule;

import android.content.Context;
import android.util.Log;

import com.futsal.manager.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static com.futsal.manager.NetworkModule.Retrofit2NetworkInterface.retrofit;

/**
 * Created by stories2 on 2017. 3. 11..
 */

public class CommunicationWithServer{

    static Context applicationContext;

    public CommunicationWithServer(Context applicationContext) {
        this.applicationContext = applicationContext;


    }
//login
    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        AddCookiesInterceptor addInterceptor = new AddCookiesInterceptor(applicationContext);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(addInterceptor);
        return builder.build();
    }

    private static OkHttpClient OkHttpClientTester() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // init cookie manager
        CookieHandler cookieHandler = new CookieManager();
        return new OkHttpClient.Builder().addNetworkInterceptor(interceptor)
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
//file
    /*private static OkHttpClient fileOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        ReceivedCookiesInterceptor receivedInterceptor = new ReceivedCookiesInterceptor(applicationContext);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(receivedInterceptor);
        return builder.build();
    }*/
/*
    public static OkHttpClient InterceptorClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new AddCookiesInterceptor(applicationContext));
        Log.d("test", "ap:" + applicationContext);
        client.interceptors().add(new ReceivedCookiesInterceptor(applicationContext));
        return client;
    }*/

    public static Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
               // .client(InterceptorClient())
                .build();
    }

    public static Retrofit fileRetrofit() {

        OkHttpClient client = OkHttpClientTester();

        /*Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create());

        return new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(fileOkHttpClient())
                // .client(InterceptorClient())
                .build();*/
        return new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public void AuthLogin(String username, String password) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = createRetrofit().create(Retrofit2NetworkInterface.class);
        AuthLoginRequest authLoginRequest = new AuthLoginRequest();
        authLoginRequest.SetUsername(username);
        authLoginRequest.SetPassword(password);
        Call<AuthLoginResponse> calling = retrofit2NetworkInterface.AuthLoginProcess("application/json", authLoginRequest);
        calling.enqueue(new Callback<AuthLoginResponse>() {
            @Override
            public void onResponse(Call<AuthLoginResponse> call, Response<AuthLoginResponse> response) {

                Headers headers = response.headers();
                String sid = response.headers().get("connect.sid");
                Log.d(applicationContext.getString(R.string.app_name), "sid: " + sid);
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body());
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body().GetMsg());
            }

            @Override
            public void onFailure(Call<AuthLoginResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());
            }
        });
    }

    public void AuthSignup(String username, String password) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = retrofit.create(Retrofit2NetworkInterface.class);
        AuthSignupRequest authSignupRequest = new AuthSignupRequest();
        authSignupRequest.SetUsername(username);
        authSignupRequest.SetPassword(password);
        Call<AuthSignupResponse> calling = retrofit2NetworkInterface.AuthSignup("application/json", authSignupRequest);
        calling.enqueue(new Callback<AuthSignupResponse>() {
            @Override
            public void onResponse(Call<AuthSignupResponse> call, Response<AuthSignupResponse> response) {
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response);
            }

            @Override
            public void onFailure(Call<AuthSignupResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());
            }
        });
    }

    public void FileList() {
        Retrofit2NetworkInterface retrofit2NetworkInterface = fileRetrofit().create(Retrofit2NetworkInterface.class);
        Call<FileResponse> calling = retrofit2NetworkInterface.FileList("connect.sid=s%3A0RTgxu9-Zh2RLRE-BZ6Mx08ykSwxZ8qq.%2F3EvetAvcvyB9z%2FDtVzcFQu75fRqDl7njM3Fzwgu4EI");
        calling.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body());
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
            }
        });
    }
}

interface Retrofit2NetworkInterface {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("auth/login")
    Call<AuthLoginResponse> AuthLoginProcess(@Header("Content-Type") String contentType, @Body AuthLoginRequest responseData);

    @POST("auth/signup")
    Call<AuthSignupResponse> AuthSignup(@Header("Content-Type") String contentType, @Body AuthSignupRequest responseData);

    @GET("file")
    Call<FileResponse>FileList(@Header("Cookie") String cookie);
}