package com.jarvisdong.uikit.mvp;

import android.support.annotation.Nullable;

/**
 * Created by JarvisDong on 2018/1/31.
 * OverView: base PERSENTER 层
 */

public interface BasePresenterOperate<T> {
    /**
     * 进入时调用的方法,可初始化调接口等
     *
     * @param requestType
     * @param msg
     * @param t
     */
    void enterOperate(int requestType, @Nullable String msg, @Nullable T t);

    /**
     * view 向persenter 发送的请求动作;
     * 统一管理发送
     *
     * @param mViewAction
     */
    void submitEventOperate(@Nullable VMessage<T> mViewAction);

    /**
     * model 通过persenter 向view 发送的执行动作;
     * 统一管理发送
     *
     * @param mModelAction
     */
    void submitDataOperate(@Nullable VMessage<T> mModelAction);

    /**
     * 退出时可以调用的方法
     */
    void exitOperate();


}
