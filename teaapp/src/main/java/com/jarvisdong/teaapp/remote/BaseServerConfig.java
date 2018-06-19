package com.jarvisdong.teaapp.remote;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseServerConfig {

    private static final String PRODUCT_URL = "";

    //----abe 2017/4/1 新接口139
    private static final String NEW_DEV_URL = "";
    //-----end-----

    public static String BASE_URL = NEW_DEV_URL;
    public static final int DEFAULT_TIMEOUT = 30;

    public static final Retrofit createRetrofit(String baseUrl, OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(gson == null ? GsonConverterFactory.create() : GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//这里Rxjava2写了兼容,必须放在1前面;
                .build();
    }
}
