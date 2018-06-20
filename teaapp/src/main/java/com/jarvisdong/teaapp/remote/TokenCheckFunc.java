package com.jarvisdong.teaapp.remote;


import com.jarvisdong.teaapp.remote.event.AppEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by alex on 16/8/20.
 * 应该放入uikit中;
 */
public class TokenCheckFunc<T> implements Function<T, T> {

    @Override
    public T apply(@NonNull T t) throws Exception {
        return getTokenErr(t);
    }


    private T getTokenErr(T t) {
        if (t != null && t instanceof CommonHttpResult) {
            if (((CommonHttpResult) t).isTokenError()) {
                ((CommonHttpResult) t).msg = "登录凭证过期,请重新登录";
                RxBus2.getDefault().send(new AppEvent(AppEvent.EVENT_TYPE.EVENT_TOKEN_ERROR, null));
            }
        }
        return t;
    }
}
