package com.jarvisdong.uikit.mvp;

import android.support.annotation.NonNull;

import com.jarvisdong.uikit.clear.UseCase;


/**
 * Created by JarvisDong on 2018/1/31.
 * OverView: MODEL 层
 * T:observer onext的返回对象类型;
 * Q:请求参数类型;
 */

public interface BaseModelDataSource<Q extends UseCase.RequestValues,P extends UseCase.ResponseValues> {

    /**
     * 加载对象列表
     *
     * @param <P>
     */
    interface LoadDatasCallback<P> {

        void onDatasLoaded(P mLoadAction);

        void onDataNotAvailable(@NonNull Throwable e);
    }

    /**
     * 带参数的加载数据,persenter中的回调;
     */
    void feachDatas(@NonNull Q requestValue, @NonNull LoadDatasCallback<P> callback);

    /**
     * 保存数据;
     */
    void saveDatas(@NonNull P responseValue);

    /**
     * 更改数据;
     */
    void updateCompleted(@NonNull P responseValue);


    /**
     * 删除数据;
     */
    void deleteDatas(@NonNull P responseValue);

    /**
     * 删除所有数据;
     */
    void deleteAllDatas();

    /**
     * 取消加载;
     */
    void cancel();
}
