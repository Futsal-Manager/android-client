package com.futsal.manager.NetworkModule;

/**
 * Created by stories2 on 2017. 3. 12..
 */

/*public class ReceivedCookiesInterceptor implements Interceptor {

    Context context;

    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }

    // 값 불러오기, KEY
    private Set<String> getPreferences(String name, String key, Context mContext){
        SharedPreferences pref = mContext.getSharedPreferences(name, MODE_PRIVATE);

        Set <String> hashset = pref.getStringSet(key, new HashSet<String>() );
        //Log.d("test", "load cookie: " + hashset);
        return hashset;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        // Preference에서 cookies를 가져오는 작업을 수행
        Set<String> preferences =  getPreferences("FUTSAL", "FUTSAL_COOKIE", context);//getSharedPreferences("FUTSAL", new Set<String>());
//builder.addHeader("Cookie", preferences[0])
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.d("test re", "load cookie: "+cookie);
        }


        // Web,Android,iOS 구분을 위해 User-Agent세팅
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");


        return chain.proceed(builder.build());
    }*/
/*
    // 값 저장하기, ex name: FUTSAL_COOKIE, key: value
    private void savePreferences(String name, String key, HashSet<String> value, Context mContext){
        SharedPreferences pref = mContext.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        editor.commit();
        Log.d("test", "hash2: " + value);
    }


    // login을 하고 나서 저장할아이, Cookie
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            Log.d("test", "hash3: " + cookies);

            // Preference에 cookies를 넣어주는 작업을 수행
            savePreferences("FUTSAL", "FUTSAL_COOKIE", cookies, context);

        }

        return originalResponse;
    }*/
/*
    // 요청을 보낼때 인터셉트.
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = getPreferences("Futsal", "FUTSAL_COOKIE");
        // Preference에서 cookies를 가져오는 작업을 수행


        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }

        // Web,Android,iOS 구분을 위해 User-Agent세팅
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");


        return chain.proceed(builder.build());
    }
}*/