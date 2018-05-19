package com.jarvisdong.uikit.clear.concreate;

import android.support.annotation.NonNull;

import com.jarvisdong.uikit.clear.UseCase;
import com.jarvisdong.uikit.mvp.BaseModelDataSource;
import com.jarvisdong.uikit.mvp.VMessage;

import static com.jarvisdong.uikit.util.NotNullUtil.checkNotNull;


/**
 * Created by JarvisDong on 2018/2/2.
 * OverView: domain层
 * <p>
 * 通过调用model层实现对数据的管理
 * 添加自定义逻辑可以对数据处理后再封装
 * 这里可以替换其他的UseCase ,解耦;
 */

public class CommonUseCase extends UseCase<CommonUseCase.RequestValues, CommonUseCase.ResponseValues> {
    private final DatasRepository datasRepository;

    public CommonUseCase(DatasRepository datasRepository) {
        this.datasRepository = datasRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            datasRepository.refreshTasks();
        }
        datasRepository.feachDatas(values, new BaseModelDataSource.LoadDatasCallback<UseCase.ResponseValues>() {
            @Override
            public void onDatasLoaded(UseCase.ResponseValues mLoadAction) {
                getUseCaseCallback().onSuccess((ResponseValues) mLoadAction);
            }

            @Override
            public void onDataNotAvailable(@NonNull Throwable e) {
                getUseCaseCallback().onError(e);
            }
        });
    }

    @Override
    public void cancel() {
        datasRepository.cancel();
    }

    /**
     * 扩展的封装动作;
     */
    public static class RequestValues implements UseCase.RequestValues {
        @NonNull
        private VMessage requestMark = null;
        private final boolean mForceUpdate;

        public RequestValues(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }

        public RequestValues(boolean forceUpdate, Class mClazz, String tag) {
            mForceUpdate = forceUpdate;
            requestMark = new VMessage(mClazz, tag, null);
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public VMessage getRequestMark() {
            return requestMark;
        }

        public void setRequestMark(VMessage requestMark) {
            this.requestMark = requestMark;
        }
    }

    public static class ResponseValues<T> implements UseCase.ResponseValues {
        private VMessage requestMark = null;

        private final T mLoadData;

        public ResponseValues(@NonNull T tasks) {
            mLoadData = checkNotNull(tasks, "data cannot be null!");
        }

        public ResponseValues(@NonNull T tasks, VMessage requestMark) {
            mLoadData = checkNotNull(tasks, "data cannot be null!");
            this.requestMark = requestMark;
        }

        public T getLoadData() {
            return mLoadData;
        }

        public VMessage getRequestMark() {
            return requestMark;
        }

        public void setRequestMark(VMessage requestMark) {
            this.requestMark = requestMark;
        }
    }
}
