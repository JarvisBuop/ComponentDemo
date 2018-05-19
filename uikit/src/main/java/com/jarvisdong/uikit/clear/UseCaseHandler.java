/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jarvisdong.uikit.clear;


import android.support.annotation.NonNull;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static volatile UseCaseHandler INSTANCE;

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (UseCaseHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
                }
            }
        }
        return INSTANCE;
    }

    public static void destoryInstance(){
        INSTANCE = null;
    }


    private final UseCaseScheduler mUseCaseScheduler;

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    /**
     * 开始异步任务,回调响应;
     *
     * @param useCase  用来对获取数据及处理数据;
     * @param values   请求数据;
     * @param callback 回调;
     * @param <T>
     * @param <R>
     */
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValues> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
        /**
         * 保存参数,封装回调;
         * 最终的调用是由 TaskLogicManager完成,
         * 并由 UseCaseThreadPoolScheduler切换到主线程,
         * 最后由UiCallbackWrapper 封装的CallBack回调到UI;
         */

        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));
        mUseCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    /**
     * 直接回调响应,主要用于代理,一般不参与业务;
     *
     * @param response
     * @param useCaseCallback
     * @param <V>             <V extends UseCase.ResponseValues>
     */
    public <V extends UseCase.ResponseValues> void notifyResponse(final V response,
                                                                  final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValues> void notifyError(final Throwable e,
                                                                final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(e, useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.ResponseValues> implements
            UseCase.UseCaseCallback<V> {
        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                                 UseCaseHandler useCaseHandler) {
            mCallback = callback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            mUseCaseHandler.notifyError(e, mCallback);
        }
    }

}
