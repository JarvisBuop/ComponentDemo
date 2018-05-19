package com.ostak.justplayteacher.remote;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 修改支持rxjava2
 */
public class RxBus2 {
    private static volatile RxBus2 mDefaultInstance;

    private RxBus2() {
    }

    public static RxBus2 getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus2.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus2();
                }
            }
        }
        return mDefaultInstance;
    }

    private final Subject<Object> _bus = PublishSubject.create().toSerialized();

    public void send(Object o) {
        if (_bus.hasObservers()) {
            _bus.onNext(o);
        }
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    public Flowable<Object> toFlowable() {
        return _bus.toFlowable(BackpressureStrategy.BUFFER);
    }
}
