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
 * Use cases are the entry points to the domain layer.
 *
 * @param <Q> the request type 请求参数泛型
 * @param <P> the response type 响应参数泛型
 *
 * executeUseCase 具体的实现是通过model层调取数据并处理;
 */
public abstract class UseCase<Q extends UseCase.RequestValues, P extends UseCase.ResponseValues> {

    private Q mRequestValues;

    private UseCaseCallback<P> mUseCaseCallback;

    public void setRequestValues(Q requestValues) {
        mRequestValues = requestValues;
    }

    public Q getRequestValues() {
        return mRequestValues;
    }

    public UseCaseCallback<P> getUseCaseCallback() {
        return mUseCaseCallback;
    }

    public void setUseCaseCallback(UseCaseCallback<P> useCaseCallback) {
        mUseCaseCallback = useCaseCallback;
    }

    void run() {
       executeUseCase(mRequestValues);
    }

    protected abstract void executeUseCase(Q requestValues);
    public abstract void cancel();

    /**
     * Data passed to a request.
     */
    public interface RequestValues {
    }

    /**
     * Data received from a request.
     */
    public interface ResponseValues {
    }

    /**
     * Data callback
     * @param
     */
    public interface UseCaseCallback<W> {
        void onSuccess(W response);
        void onError(@NonNull Throwable e);
    }
}
