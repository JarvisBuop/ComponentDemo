package debug;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.jarvisdong.uikit.base.BaseApplication;

/**
 * Created by JarvisDong on 2018/6/1.
 * OverView: 组件化的app, 集成化会被删除 ;
 */

public class MyModuleApp extends BaseApplication {
    private static MyModuleApp appInstance;

    public static MyModuleApp getAppInstansce() {
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

        //ARouter配置
        if (true) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
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
//                        LogUtils.e("MyModuleApp","MyModuleApp Init~~~");
////                        if (o instanceof AppEvent) {
////                            SoaAppEventHandler.getInstance(getApplicationContext()).onEvent((AppEvent) o);
////                        }
//                    }
//                });
    }
}
