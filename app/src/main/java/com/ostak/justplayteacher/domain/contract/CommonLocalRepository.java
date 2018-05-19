package com.ostak.justplayteacher.domain.contract;

import android.support.annotation.NonNull;

import com.jarvisdong.uikit.clear.UseCase;
import com.jarvisdong.uikit.mvp.BaseModelDataSource;


/**
 * Created by JarvisDong on 2018/2/6.
 *
 */

public class CommonLocalRepository implements BaseModelDataSource<UseCase.RequestValues, UseCase.ResponseValues> {
    private static volatile CommonLocalRepository sInstance;

    // Prevent direct instantiation.
    private CommonLocalRepository() {

    }

    public static CommonLocalRepository getInstance() {
        if (sInstance == null) {
            synchronized (CommonLocalRepository.class) {
                if (sInstance == null) {
                    sInstance = new CommonLocalRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void feachDatas(@NonNull UseCase.RequestValues requestValue, @NonNull LoadDatasCallback<UseCase.ResponseValues> callback) {

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

    }
}
