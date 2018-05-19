package com.jarvisdong.uikit.clear.concreate;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.jarvisdong.uikit.clear.UseCase;
import com.jarvisdong.uikit.mvp.BaseModelDataSource;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.jarvisdong.uikit.util.NotNullUtil.checkNotNull;


/**
 * Created by JarvisDong on 2018/2/6.
 */

public class DatasRepository implements BaseModelDataSource<UseCase.RequestValues, UseCase.ResponseValues> {
    private final String TAG = this.getClass().getSimpleName();
    private static volatile DatasRepository INSTANCE = null;

    private final BaseModelDataSource mTasksRemoteDataSource;

    private final BaseModelDataSource mTasksLocalDataSource;

    private DatasRepository(@NonNull BaseModelDataSource remoteDataSource,
                            @NonNull BaseModelDataSource localDataSource) {
        mTasksRemoteDataSource = checkNotNull(remoteDataSource);
        mTasksLocalDataSource = checkNotNull(localDataSource);
    }

    public static DatasRepository getInstance(BaseModelDataSource tasksRemoteDataSource,
                                              BaseModelDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (DatasRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DatasRepository(tasksRemoteDataSource, tasksLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    boolean mCacheIsDirty = false;
    Map<String, UseCase.ResponseValues> mCachedTasks;


    @Override
    public void feachDatas(@NonNull final UseCase.RequestValues requestValue, @NonNull final LoadDatasCallback<UseCase.ResponseValues> callback) {
        checkNotNull(requestValue);
        checkNotNull(callback);

        if (isInnerCache(requestValue)) {
            Log.e(TAG, "from in cache ");
            callback.onDatasLoaded(mCachedTasks.get(((CommonUseCase.RequestValues) requestValue).getRequestMark().tag));
            return;
        }
        mTasksRemoteDataSource.feachDatas(requestValue, new LoadDatasCallback<UseCase.ResponseValues>() {

            @Override
            public void onDatasLoaded(UseCase.ResponseValues mLoadAction) {
                if (requestValue instanceof CommonUseCase.RequestValues && mLoadAction instanceof CommonUseCase.ResponseValues) {
                    setCacheCommonUseCase((CommonUseCase.ResponseValues) mLoadAction, (CommonUseCase.RequestValues) requestValue);
                    refreshLocalDataSource(mLoadAction);
                }
                callback.onDatasLoaded(mLoadAction);
            }

            @Override
            public void onDataNotAvailable(@NonNull Throwable e1) {
                if (e1 != null) {
                    callback.onDataNotAvailable(e1);
                }
                mTasksLocalDataSource.feachDatas(requestValue, new LoadDatasCallback<UseCase.ResponseValues>() {
                    @Override
                    public void onDatasLoaded(UseCase.ResponseValues mLoadAction) {
                        if (mLoadAction != null) {
                            if (requestValue instanceof CommonUseCase.RequestValues) {
                                setCacheCommonUseCase((CommonUseCase.ResponseValues) mLoadAction, (CommonUseCase.RequestValues) requestValue);
                            }
                            callback.onDatasLoaded(mLoadAction);
                        }
                    }

                    @Override
                    public void onDataNotAvailable(@NonNull Throwable e2) {
                    }
                });
            }
        });
    }

    private boolean isInnerCache(@NonNull UseCase.RequestValues requestValue) {
        if (mCachedTasks != null && !mCacheIsDirty && requestValue instanceof CommonUseCase.RequestValues) {
            String key = ((CommonUseCase.RequestValues) requestValue).getRequestMark().tag;
            if (!TextUtils.isEmpty(key) && mCachedTasks.get(key) != null) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    private void setCacheCommonUseCase(CommonUseCase.ResponseValues mLoadAction, @NonNull CommonUseCase.RequestValues requestValue) {
        String key = requestValue.getRequestMark().tag;
        if (!TextUtils.isEmpty(key)) {
            refreshCache(key, mLoadAction);
        }
    }

    @Override
    public void saveDatas(@NonNull UseCase.ResponseValues responseValue) {
        checkNotNull(responseValue);
        mTasksRemoteDataSource.saveDatas(responseValue);
        mTasksLocalDataSource.saveDatas(responseValue);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        if (isCanDisposed(responseValue)) {
            mCachedTasks.put(((CommonUseCase.ResponseValues) responseValue).getRequestMark().tag, responseValue);
        }
    }

    @Override
    public void updateCompleted(@NonNull UseCase.ResponseValues responseValue) {
        checkNotNull(responseValue);
        mTasksRemoteDataSource.updateCompleted(responseValue);
        mTasksLocalDataSource.updateCompleted(responseValue);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        if (isCanDisposed(responseValue)) {
            mCachedTasks.put(((CommonUseCase.ResponseValues) responseValue).getRequestMark().tag, responseValue);
        }
    }

    @Override
    public void deleteDatas(@NonNull UseCase.ResponseValues responseValue) {
        checkNotNull(responseValue);
        mTasksRemoteDataSource.deleteDatas(responseValue);
        mTasksLocalDataSource.deleteDatas(responseValue);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        if (isCanDisposed(responseValue)) {
            mCachedTasks.remove(responseValue);
        }
    }

    private boolean isCanDisposed(UseCase.ResponseValues responseValues) {
        if (responseValues instanceof CommonUseCase.ResponseValues) {
            CommonUseCase.ResponseValues commonUseCase = (CommonUseCase.ResponseValues) responseValues;
            if (!TextUtils.isEmpty(commonUseCase.getRequestMark().tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAllDatas() {
        mTasksRemoteDataSource.deleteAllDatas();
        mTasksLocalDataSource.deleteAllDatas();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void cancel() {
        mTasksRemoteDataSource.cancel();
        mTasksLocalDataSource.cancel();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    public void refreshTasks() {
        mCacheIsDirty = true;
    }


    /**
     * 刷新内存缓存;
     */
    private void refreshCache(String tag, UseCase.ResponseValues t) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        mCachedTasks.put(tag, t);
        mCacheIsDirty = false;
        Log.e(TAG, "put in cache ");
    }

    /**
     * 刷新本地缓存;
     *
     * @param tasks
     */
    private void refreshLocalDataSource(UseCase.ResponseValues tasks) {
        mTasksLocalDataSource.deleteAllDatas();
        mTasksLocalDataSource.saveDatas(tasks);
    }

}
