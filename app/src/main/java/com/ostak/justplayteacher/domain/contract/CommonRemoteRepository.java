package com.ostak.justplayteacher.domain.contract;

import android.support.annotation.NonNull;

import com.jarvisdong.uikit.clear.UseCase;
import com.jarvisdong.uikit.mvp.BaseModelDataSource;
import com.ostak.justplayteacher.domain.CucConnectConfig;
import com.ostak.justplayteacher.remote.RxUtil2;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @param
 */
public class CommonRemoteRepository implements BaseModelDataSource<UseCase.RequestValues, UseCase.ResponseValues> {
    private static volatile CommonRemoteRepository sInstance;

    // Prevent direct instantiation.
    private CommonRemoteRepository() {

    }

    public static CommonRemoteRepository getInstance() {
        if (sInstance == null) {
            synchronized (CommonRemoteRepository.class) {
                if (sInstance == null) {
                    sInstance = new CommonRemoteRepository();
                }
            }
        }
        return sInstance;
    }

    private CucConnectConfig mServer = null;
    private CompositeDisposable mDisposables = null;

    @Override
    public void feachDatas(@NonNull UseCase.RequestValues requestValue, @NonNull LoadDatasCallback<UseCase.ResponseValues> callback) {
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        if (mServer == null) {
            mServer = new CucConnectConfig();
        }
        mServer.link(mDisposables, requestValue, callback);
    }

    @Override
    public void saveDatas(@NonNull UseCase.ResponseValues responseValue) {

    }

    @Override
    public void updateCompleted(@NonNull UseCase.ResponseValues responseValue) {

    }

    @Override
    public void deleteDatas(@NonNull UseCase.ResponseValues responseValue) {

    }

    @Override
    public void deleteAllDatas() {

    }

    @Override
    public void cancel() {
        if (mDisposables != null) {
            mDisposables.clear();
            RxUtil2.dispose(mDisposables);
            mDisposables = null;
        }
        if (mServer != null) {
            mServer = null;
        }
    }
}
