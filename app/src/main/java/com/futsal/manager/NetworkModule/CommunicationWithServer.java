package com.futsal.manager.NetworkModule;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.futsal.manager.DefineManager;
import com.futsal.manager.LogModule.LogManager;
import com.futsal.manager.R;

import org.json.JSONObject;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import retrofit2.http.PartMap;

import static com.futsal.manager.DefineManager.LOAD_FAILURE;
import static com.futsal.manager.DefineManager.LOAD_SUCCESS;
import static com.futsal.manager.DefineManager.LOGIN_FAILURE;
import static com.futsal.manager.DefineManager.LOGIN_SUCCESS;
import static com.futsal.manager.DefineManager.LOG_LEVEL_DEBUG;
import static com.futsal.manager.DefineManager.LOG_LEVEL_ERROR;
import static com.futsal.manager.DefineManager.LOG_LEVEL_INFO;
import static com.futsal.manager.DefineManager.NOT_LOADED;
import static com.futsal.manager.DefineManager.NOT_LOGGED_IN;
import static com.futsal.manager.DefineManager.NOT_SIGN_UP;
import static com.futsal.manager.DefineManager.SERVER_DOMAIN_NAME;
import static com.futsal.manager.DefineManager.SIGN_UP_FAILURE;
import static com.futsal.manager.DefineManager.SIGN_UP_SUCCESS;
import static com.futsal.manager.NetworkModule.Retrofit2NetworkInterface.retrofit;

/**
 * Created by stories2 on 2017. 3. 11..
 */

public class CommunicationWithServer{

    public boolean videoUploadStatus = false;

    static Context applicationContext;
    static boolean DEBUG_MODE = false, loginStatus = false;
    String hardCoding = "connect.sid=s:JTB7Mndgcy0NUnxK8VUSWoV6r-TZgpFX.TD5FeoFJMNYbbpAGc9soY5IkoIsJex5hHDAoLHZAVsw", fileUrl;
    List<String> fileUrlList;
    int loginStatusVer2 = NOT_LOGGED_IN, getFileStatusVer2 = NOT_LOADED,
        signUpStatusVer2 = NOT_SIGN_UP;

    public CommunicationWithServer(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

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
        builder.followRedirects(false);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        AddCookiesInterceptor addInterceptor = new AddCookiesInterceptor(applicationContext);
        ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor(applicationContext);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(addInterceptor);
        //builder.addInterceptor(receivedCookiesInterceptor);
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
                .baseUrl("http://" + SERVER_DOMAIN_NAME + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
               // .client(InterceptorClient())
                .build();
    }

    public static OkHttpClient fileOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(5, TimeUnit.MINUTES);
        builder.readTimeout(5, TimeUnit.MINUTES);
        builder.writeTimeout(5, TimeUnit.MINUTES);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor(applicationContext);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //builder.addInterceptor(interceptor);
        builder.addInterceptor(receivedCookiesInterceptor);
        return builder.build();
    }

    public static Retrofit fileRetrofit() {

        OkHttpClient client = OkHttpClientTester();

        if(DEBUG_MODE) {
            return new Retrofit.Builder()
                    .baseUrl("http://" + SERVER_DOMAIN_NAME + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(fileOkHttpClient())
                    .client(client)
                    .build();
        }
        else {
            return new Retrofit.Builder()
                    .baseUrl("http://" + SERVER_DOMAIN_NAME + "/")
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
                try {
                    Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body().GetMsg());
                }
                catch (Exception err) {
                    Log.d(applicationContext.getString(R.string.app_name), "failed: " + err.getMessage());
                }
                loginStatus = true;
            }

            @Override
            public void onFailure(Call<AuthLoginResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());
                loginStatus = false;
            }
        });
    }

    public void AuthLoginVer2(String username, String password) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = createRetrofit().create(Retrofit2NetworkInterface.class);
        AuthLoginRequest authLoginRequest = new AuthLoginRequest();
        authLoginRequest.SetUsername(username);
        authLoginRequest.SetPassword(password);

        loginStatusVer2 = NOT_LOGGED_IN;

        Call<AuthLoginResponse> calling = retrofit2NetworkInterface.AuthLoginProcess("application/json", authLoginRequest);
        calling.enqueue(new Callback<AuthLoginResponse>() {
            @Override
            public void onResponse(Call<AuthLoginResponse> call, Response<AuthLoginResponse> response) {

                Headers headers = response.headers();
                String sid = response.headers().get("connect.sid");
                Log.d(applicationContext.getString(R.string.app_name), "sid: " + sid);
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body());
                /*if(sid == null) {
                    loginStatusVer2 = LOGIN_FAILURE;
                    return;
                }*/
                try {
                    Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body().GetMsg());
                    if(response.body().GetMsg().equals("success login")) {
                        loginStatusVer2 = LOGIN_SUCCESS;
                    }
                    else {
                        loginStatusVer2 = LOGIN_FAILURE;
                    }
                    return ;
                }
                catch (Exception err) {
                    Log.d(applicationContext.getString(R.string.app_name), "failed: " + err.getMessage());
                }
                //loginStatusVer2 = LOGIN_SUCCESS;
            }

            @Override
            public void onFailure(Call<AuthLoginResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());
                loginStatusVer2 = LOGIN_FAILURE;
            }
        });
    }

    public boolean GetLoginStatus() {
        return loginStatus;
    }

    public int GetLoginStatusVer2() {
        return loginStatusVer2;
    }

    public void AuthSignup(String username, String password) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = retrofit.create(Retrofit2NetworkInterface.class);
        AuthSignupRequest authSignupRequest = new AuthSignupRequest();
        authSignupRequest.SetUsername(username);
        authSignupRequest.SetPassword(password);
        Call<AuthSignupResponse> calling = retrofit2NetworkInterface.AuthSignup("application/json", authSignupRequest);
        signUpStatusVer2 = NOT_SIGN_UP;
        calling.enqueue(new Callback<AuthSignupResponse>() {
            @Override
            public void onResponse(Call<AuthSignupResponse> call, Response<AuthSignupResponse> response) {
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response);
                signUpStatusVer2 = SIGN_UP_SUCCESS;
            }

            @Override
            public void onFailure(Call<AuthSignupResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());

                signUpStatusVer2 = SIGN_UP_FAILURE;
            }
        });
    }

    public void AuthSignupVer2(String username, String password, String team) {
        signUpStatusVer2 = NOT_SIGN_UP;
        Retrofit2NetworkInterface retrofit2NetworkInterface = retrofit.create(Retrofit2NetworkInterface.class);
        AuthSignupRequest authSignupRequest = new AuthSignupRequest();
        authSignupRequest.SetUsername(username);
        authSignupRequest.SetPassword(password);
        authSignupRequest.SetTeam(team);
        Call<AuthSignupResponse> calling = retrofit2NetworkInterface.AuthSignup("application/json", authSignupRequest);
        calling.enqueue(new Callback<AuthSignupResponse>() {
            @Override
            public void onResponse(Call<AuthSignupResponse> call, Response<AuthSignupResponse> response) {
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response);
                try {
                    LogManager.PrintLog("CommunicationWithServer", "AuthSignupVer2" , "res: " + response.body().GetRes(), LOG_LEVEL_INFO);
                    signUpStatusVer2 = SIGN_UP_SUCCESS;
                }
                catch (Exception err) {
                    LogManager.PrintLog("CommunicationWithServer", "AuthSignupVer2" , "Error: " + err.getMessage(), LOG_LEVEL_ERROR);
                    signUpStatusVer2 = SIGN_UP_FAILURE;
                }
            }

            @Override
            public void onFailure(Call<AuthSignupResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "Error: " + t.getMessage());

                signUpStatusVer2 = SIGN_UP_FAILURE;
            }
        });
    }

    public int GetSignUpStatusVer2() {
        return signUpStatusVer2;
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
        fileUrlList = null;
        getFileStatusVer2 = NOT_LOADED;
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
                Log.d(applicationContext.getString(R.string.app_name), "response: " + response.body().GetList());
                ArrayList fileList = response.body().GetList();
                ArrayList<FileResponse.s3url> linkList = fileList;
                fileUrlList = new ArrayList<String>();
                LogManager.PrintLog("CommunicationWithServer", "FileList", "saving each file url", DefineManager.LOG_LEVEL_INFO);
                if(linkList != null) {
                    for(FileResponse.s3url eachFileUrlIndex : linkList) {
                        fileUrlList.add(eachFileUrlIndex.GetS3url());
                        LogManager.PrintLog("CommunicationWithServer", "FileList", "each file url: " + eachFileUrlIndex.GetS3url(), DefineManager.LOG_LEVEL_INFO);
                    }
                }
                //Log.d(applicationContext.getString(R.string.app_name), "url: " + linkList.get(0).GetS3url());

                //Log.d(applicationContext.getString(R.string.app_name), "file url: " + fileList.get(0));
                try {
                    fileUrl = linkList.get(0).GetS3url();
                }
                catch (Exception err) {
                    LogManager.PrintLog("CommunicationWithServer", "FileList", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                }
                getFileStatusVer2 = LOAD_SUCCESS;
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Log.d(applicationContext.getString(R.string.app_name), "failed");
                fileUrlList = new ArrayList<String>();
                getFileStatusVer2 = LOAD_FAILURE;
            }
        });
    }

    public String GetFileUrl() {
        return fileUrl;
    }

    public List<String> GetFileUrls() {
        return fileUrlList;
    }

    public int GetFileStatus() {
        return getFileStatusVer2;
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

    public void UploadFileTester(Uri fileSavedPath) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = fileRetrofit().create(Retrofit2NetworkInterface.class);
        File savedVideoFile = LoadFileFromMemory(fileSavedPath.getPath());
        FileUploadRequest fileUploadRequest = new FileUploadRequest();
        fileUploadRequest.SetFile(savedVideoFile.getPath().toString());
        Call<FileUploadResponse> calling;
        calling = retrofit2NetworkInterface.FileUpload("multipart/form-data", fileUploadRequest);
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

    public void UploadFileTester2(Uri fileSavedPath) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = fileRetrofit().create(Retrofit2NetworkInterface.class);
        File savedVideoFile = LoadFileFromMemory(fileSavedPath.getPath());
        //GetMimeType(fileSavedPath.getPath().toString())
        //RequestBody requestBody = RequestBody.create(MediaType.parse(GetMimeType(fileSavedPath.getPath().toString())));
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody fileBody = RequestBody.create(MediaType.parse(GetMimeType(fileSavedPath.getPath().toString())),
                savedVideoFile);
        map.put("file\"; file=\"" + savedVideoFile.getPath().toString() + "\"", fileBody);

        Call<FileUploadResponse> calling = retrofit2NetworkInterface.FileUpload(map);
        calling.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(Call<FileUploadResponse> call, Response<FileUploadResponse> response) {
                Log.v("Upload", "success: " + response.body().GetRes());
            }

            @Override
            public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public void UploadFileTester3(String fileSavedPath) {
        LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Upload Target Path: " + fileSavedPath, LOG_LEVEL_DEBUG);
        Retrofit2NetworkInterface retrofit2NetworkInterface = fileRetrofit().create(Retrofit2NetworkInterface.class);
        File savedVideoFile = LoadFileFromMemory(fileSavedPath);
        LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "File loaded: " + savedVideoFile, LOG_LEVEL_DEBUG);
        FileUploadRequest fileUploadRequest = new FileUploadRequest();
        fileUploadRequest.SetFile(savedVideoFile.getPath().toString());
        LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "File upload ready", LOG_LEVEL_DEBUG);
        //GetMimeType(fileSavedPath.getPath().toString());
        RequestBody reqFile = RequestBody.create(MediaType.parse(GetMimeType(fileSavedPath)), savedVideoFile);//GetMimeType(fileSavedPath.getPath().toString())
        LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Video Name?: " + savedVideoFile.getName(), LOG_LEVEL_DEBUG);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", savedVideoFile.getName(), reqFile);
        //RequestBody name = RequestBody.create(MediaType.parse("application/json"), "{\"file\":\"" + savedVideoFile.getPath().toString() + "\"}");
        LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Video Upload Ready", LOG_LEVEL_INFO);
        try {
            Call<FileUploadResponse> calling = retrofit2NetworkInterface.FileUpload(fileUploadRequest, body, "chunked", "-1");
            LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Calling` Retrofit Network", LOG_LEVEL_INFO);

            calling.enqueue(new Callback<FileUploadResponse>() {
                @Override
                public void onResponse(Call<FileUploadResponse> call, Response<FileUploadResponse> response) {
                    videoUploadStatus = true;
                    try {
                        LogManager.PrintLog("CommunicationWithServer", "onResponse", "Raw Response: " + response.message(), LOG_LEVEL_DEBUG);
                        if(response.headers().get("code").equals("200")) {
                            //Log.v("Upload", "success: " + response.body().GetRes());
                            LogManager.PrintLog("CommunicationWithServer", "onResponse", "Upload Success: " + response.body().toString(), DefineManager.LOG_LEVEL_INFO);
                        }
                        else {
                            try{
                                LogManager.PrintLog("CommunicationWithServer", "onResponse", "Error Body: " + response.errorBody().string(), DefineManager.LOG_LEVEL_ERROR);
                            }
                            catch (Exception err2) {
                                LogManager.PrintLog("CommunicationWithServer", "onResponse", "Error: " + err2.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                            }
                        }
                    }
                    catch (Exception err) {
                        LogManager.PrintLog("CommunicationWithServer", "onResponse", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                        videoUploadStatus = true;
                    }
                }

                @Override
                public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                    LogManager.PrintLog("CommunicationWithServer", "onFailure", "Error: " + t.getMessage(), LOG_LEVEL_ERROR);
                }
            });
        }
        catch (Exception err) {
            LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Upload Process Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
    }

    public void UploadFileTester3(Uri fileSavedPath) {
        Retrofit2NetworkInterface retrofit2NetworkInterface = fileRetrofit().create(Retrofit2NetworkInterface.class);
        File savedVideoFile = LoadFileFromMemory(fileSavedPath.getPath());
        FileUploadRequest fileUploadRequest = new FileUploadRequest();
        fileUploadRequest.SetFile(savedVideoFile.getPath().toString());

        //GetMimeType(fileSavedPath.getPath().toString());
        RequestBody reqFile = RequestBody.create(MediaType.parse(GetMimeType(fileSavedPath.getPath().toString())), savedVideoFile);//GetMimeType(fileSavedPath.getPath().toString())
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", savedVideoFile.getName(), reqFile);
        //RequestBody name = RequestBody.create(MediaType.parse("application/json"), "{\"file\":\"" + savedVideoFile.getPath().toString() + "\"}");
        LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Video Upload Ready", LOG_LEVEL_INFO);
        try {
            Call<FileUploadResponse> calling = retrofit2NetworkInterface.FileUpload(fileUploadRequest, body, "chunked", "-1");
            LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Calling` Retrofit Network", LOG_LEVEL_INFO);

            calling.enqueue(new Callback<FileUploadResponse>() {
                @Override
                public void onResponse(Call<FileUploadResponse> call, Response<FileUploadResponse> response) {
                    videoUploadStatus = true;
                    try {
                        LogManager.PrintLog("CommunicationWithServer", "onResponse", "Raw Response: " + response.message(), LOG_LEVEL_DEBUG);
                        if(response.headers().get("code").equals("200")) {
                            //Log.v("Upload", "success: " + response.body().GetRes());
                            LogManager.PrintLog("CommunicationWithServer", "onResponse", "Upload Success: " + response.body().toString(), DefineManager.LOG_LEVEL_INFO);
                        }
                        else {
                            try{
                                LogManager.PrintLog("CommunicationWithServer", "onResponse", "Error Body: " + response.errorBody().string(), DefineManager.LOG_LEVEL_ERROR);
                            }
                            catch (Exception err2) {
                                LogManager.PrintLog("CommunicationWithServer", "onResponse", "Error: " + err2.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                            }
                        }
                    }
                    catch (Exception err) {
                        LogManager.PrintLog("CommunicationWithServer", "onResponse", "Error: " + err.getMessage(), DefineManager.LOG_LEVEL_ERROR);
                        videoUploadStatus = true;
                    }
                }

                @Override
                public void onFailure(Call<FileUploadResponse> call, Throwable t) {
                    LogManager.PrintLog("CommunicationWithServer", "onFailure", "Error: " + t.getMessage(), LOG_LEVEL_ERROR);
                }
            });
        }
        catch (Exception err) {
            LogManager.PrintLog("CommunicationWithServer", "UploadFileTester3", "Upload Process Error: " + err.getMessage(), LOG_LEVEL_ERROR);
        }
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
            .baseUrl("http://" + SERVER_DOMAIN_NAME + "/")
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

    @POST("file")
    Call<FileUploadResponse>FileUpload(@Header("Content-Type") String contentType, @Body FileUploadRequest fileUploadRequest);

    @POST("file")
    @Multipart
    Call<FileUploadResponse>FileUpload(@PartMap Map<String, RequestBody> params);

    @POST("file")
    @Multipart
    Call<FileUploadResponse>FileUpload(@Part("file") FileUploadRequest fileUploadRequest, @Part MultipartBody.Part videoData);

    @POST("file")
    @Multipart
    Call<FileUploadResponse>FileUpload(@Part("file") FileUploadRequest fileUploadRequest, @Part MultipartBody.Part videoData,
                                       @Header("Transfer-Encoding") String chunkedEnable, @Header("Content-Length") String contentLength);
}