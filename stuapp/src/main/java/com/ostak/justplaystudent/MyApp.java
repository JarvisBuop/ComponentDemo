package com.ostak.justplaystudent;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;

/**
 * Created by JarvisDong on 2018/6/1.
 * OverView:
 */

public class MyApp extends Application {
    private static MyApp appInstance;

    public static MyApp getAppInstansce() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        /**
         * 工具类初始化
         */
        initUtils();
        startRxEvent();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /**
         * 分包初始化;
         */
        MultiDex.install(this);
    }

    private void initUtils() {
        Utils.init(getApplicationContext());
    }

    private void startRxEvent(){
//        RxBus2.getDefault().toFlowable()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        LogUtils.e("MyApp","MyApp Init~~~");
////                        if (o instanceof AppEvent) {
////                            SoaAppEventHandler.getInstance(getApplicationContext()).onEvent((AppEvent) o);
////                        }
//                    }
//                });
    }
}
