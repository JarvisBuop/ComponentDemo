package com.jarvisdong.uikit.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by JarvisDong on 2018/6/19.
 * OverView:所有组件都需要继承此application , 实现组件化;
 */

public class BaseApplication  extends Application{
    private static BaseApplication appInstance;

    public static BaseApplication getAppInstansce() {
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
    }

    private void initUtils() {
        Utils.init(getApplicationContext());
    }
}
