package com.futsal.manager.NetworkModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.futsal.manager.R;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by stories2 on 2017. 3. 12..
 */

public class AddCookiesInterceptor implements Interceptor{
    Context context;

    public AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    // 값 저장하기, ex name: FUTSAL_COOKIE, key: value
    private void savePreferences(String name, String key, HashSet<String> value, Context mContext){
        SharedPreferences pref = mContext.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        editor.commit();
        //Log.d("test", "save cookie: " + value);
    }

    // login을 하고 나서 저장할아이, Cookie
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                Log.d("test", "origin cookie: " + header);
                String[] headers = header.split(";");
                for(String checkHeader : headers) {
                    if(checkHeader.contains("connect.sid")) {
                        String result = null;
                        try {
                            //result = java.net.URLDecoder.decode(checkHeader,"UTF-8");
                            result = checkHeader;
                        }
                        catch (Exception err) {
                            Log.d(context.getString(R.string.app_name), "Error in intercept: " + err.getMessage());
                        }
                        cookies.add(result);
                        Log.d("test", "header: " + result);
                    }
                }
            }
            Log.d("test", "save cookie: " + cookies);

            // Preference에 cookies를 넣어주는 작업을 수행
            savePreferences("FUTSAL", "FUTSAL_COOKIE", cookies, context);

        }

        return originalResponse;
    }

}
