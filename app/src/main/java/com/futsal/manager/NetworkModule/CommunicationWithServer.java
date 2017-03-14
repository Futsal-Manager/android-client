package com.futsal.manager.NetworkModule;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.futsal.manager.R;

import org.json.JSONObject;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.futsal.manager.NetworkModule.Retrofit2NetworkInterface.retrofit;

/**
 * Created by stories2 on 2017. 3. 11..
 */

public class CommunicationWithServer{

    static Context applicationContext;
    static boolean DEBUG_MODE = false;
    String hardCoding = "connect.sid=s:JTB7Mndgcy0NUnxK8VUSWoV6r-TZgpFX.TD5FeoFJMNYbbpAGc9soY5IkoIsJex5hHDAoLHZAVsw";

    public CommunicationWithServer(Context applicationContext, boolean mode) {
        this.applicationContext = applicationContext;
        DEBUG_MODE = mode;
    }

    public void SetMode(boolean mode) {
        DEBUG_MODE = mode;
    }

    public void SetHashCode(String hardCoding) {
        this.hardCoding = hardCoding;
        Log.d(applicationContext.getString(R.string.app_name), "Hash Set: " + this.hardCoding);
    }

    public String GetHashCode() {
        return hardCoding;
    }

//login
    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        AddCookiesInterceptor addInterceptor = new AddCookiesInterceptor(applicationContext);
        ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor(applicationContext);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(addInterceptor);
        builder.addInterceptor(receivedCookiesInterceptor);
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

    public static Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
               // .client(InterceptorClient())
                .build();
    }

    public static OkHttpClient fileOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor(applicationContext);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(receivedCookiesInterceptor);
        return builder.build();
    }

    public static Retrofit fileRetrofit() {

        OkHttpClient client = OkHttpClientTester();

        if(DEBUG_MODE) {
            return new Retrofit.Builder()
                    .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(fileOkHttpClient())
                    .client(client)
                    .build();
        }
        else {
            return new Retrofit.Builder()
                    .baseUrl("http://ec2-52-78-237-85.ap-northeast-2.compute.amazonaws.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(fileOkHttpClient())
                    //.client(client)
                    .build();
        }
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
        Call<FileResponse> calling;
        if(DEBUG_MODE) {
            calling = retrofit2NetworkInterface.FileList(hardCoding);
        }
        else {
            calling = retrofit2NetworkInterface.FileList();
        }
        calling.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                Log.d(applicationContext.getString(R.string.app_name), "header test: " + response.headers().get("code"));
                try {
                    String errMsg = response.errorBody().string();
                    Log.d(applicationContext.getString(R.string.app_name), "error body test: " + errMsg);
                    JSONObject catchServerErrorMsg = new JSONObject(errMsg);
                    Log.d(applicationContext.getString(R.string.app_name), "FileList ServerError Msg: " + catchServerErrorMsg.getString("errmsg"));
                }
                catch (Exception err) {
                    Log.d(applicationContext.getString(R.string.app_name), "error body failed: " + err.getMessage());
                }
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body());
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
            }
        });
    }

    public void UploadFile(Uri fileSavedPath) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = fileRetrofit().create(Retrofit2NetworkInterface.class);
        Log.d(applicationContext.getString(R.string.app_name), "custom get type: " + GetMimeType(fileSavedPath.getPath().toString()));
        Log.d(applicationContext.getString(R.string.app_name), "file path: " + fileSavedPath.getPath().toString());
        File savedVideoFile = LoadFileFromMemory(fileSavedPath.getPath());
        Log.d(applicationContext.getString(R.string.app_name), "file is null: " + savedVideoFile);
        Log.d(applicationContext.getString(R.string.app_name), "contentResolver is null: " + applicationContext.getContentResolver());
        Log.d(applicationContext.getString(R.string.app_name), "type is null: " + applicationContext.getContentResolver().getType(fileSavedPath));
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(GetMimeType(fileSavedPath.getPath().toString())),
                        savedVideoFile
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", savedVideoFile.getName(), requestFile);

        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        Call<FileUploadResponse> calling;
        if(DEBUG_MODE) {
            calling = retrofit2NetworkInterface.FileUpload(hardCoding, description, body);
        }
        else {
            calling = retrofit2NetworkInterface.FileUpload(description, body);
        }
        calling.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(Call<FileUploadResponse> call, Response<FileUploadResponse> response) {
                Log.v("Upload", "success: " + response.body().GetS3url());
            }

            @Override
            public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public String GetMimeType(String path) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public File LoadFileFromMemory(String fileSavedPath) {
        //File sdcard = Environment.getExternalStorageDirectory();
        //return new File(sdcard, "testVideo3.mp4");
        return new File(fileSavedPath);
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
    Call<FileResponse>FileList();

    @GET("file")
    Call<FileResponse>FileList(@Header("Cookie") String cookie);


    @POST("file")
    @Multipart
    Call<FileUploadResponse>FileUpload(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    @POST("file")
    @Multipart
    Call<FileUploadResponse>FileUpload(@Header("Cookie") String cookie, @Part("description") RequestBody description, @Part MultipartBody.Part file);
}