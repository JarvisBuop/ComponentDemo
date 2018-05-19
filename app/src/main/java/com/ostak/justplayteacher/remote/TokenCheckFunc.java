package com.ostak.justplayteacher.remote;


import com.ostak.justplayteacher.MyApp;
import com.ostak.justplayteacher.R;
import com.ostak.justplayteacher.remote.event.AppEvent;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by alex on 16/8/20.
 */
public class TokenCheckFunc<T> implements Function<T, T> {

    @Override
    public T apply(@NonNull T t) throws Exception {
        return getTokenErr(t);
    }


    private T getTokenErr(T t) {
        if (t != null && t instanceof CommonHttpResult) {
            if (((CommonHttpResult) t).isTokenError()) {
                ((CommonHttpResult) t).msg = MyApp.getAppInstansce().getString(R.string.token_error);
                RxBus2.getDefault().send(new AppEvent(AppEvent.EVENT_TYPE.EVENT_TOKEN_ERROR, null));
            }
        }
        return t;
    }
}
