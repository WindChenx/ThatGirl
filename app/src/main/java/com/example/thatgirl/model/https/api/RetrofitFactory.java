package com.example.thatgirl.model.https.api;

import retrofit2.Retrofit;
import retrofit2.http.Url;

public class RetrofitFactory {
    private static Retrofit sRetrofit;
    // 创建网络请求Observable
    public static ApiService createRequest() {
        return getRetrofit().create(ApiService.class);
    }

    // 配置Retrofit
    private synchronized static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.Host_URL) // 对应服务端的host

                    .build();
        }
        return sRetrofit;
    }
}
