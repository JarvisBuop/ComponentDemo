package com.ostak.justplayteacher.remote;

import com.ostak.justplayteacher.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by JarvisDong on 2018/1/28.
 */

public class RetrofitServer<T> extends BaseServerConfig {
    private final Retrofit mRetrofit;
    private final RetrofitService retrofitImpl;

    private RetrofitServer() {
        /**
         * 配置okhttp
         */
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.retryOnConnectionFailure(false);
        clientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    Timber.e(message);
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //加差值器,请求信息等;
        clientBuilder.addInterceptor(interceptor);


        /**
         * 配置Retrofit;
         * 注意 converter是有顺序的,先运行前面的;
         */
        mRetrofit = createRetrofit(BASE_URL,clientBuilder.build(),null);
        retrofitImpl = mRetrofit.create(RetrofitService.class);
    }

    private static class Builder {
        private static final RetrofitServer sInstance = new RetrofitServer();
    }

    public static RetrofitServer getInstance() {
        return Builder.sInstance;
    }

    /**
     * Feach Data
     */
//    public void getLogBeanByRxjava(Observer<BaseBean<LogBean>> mSubscriber) {
////        retrofitImpl.appUserLoginByRx("13817228124", "228124", "0", "1", "2")
////                //指定io线程加载;
////                .subscribeOn(Schedulers.io())
////                .subscribe(mSubscriber);
//    }
}
