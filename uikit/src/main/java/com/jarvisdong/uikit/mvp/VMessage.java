package com.jarvisdong.uikit.mvp;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JarvisDong on 2018/2/6.
 * OverView:数据传递message
 * 1.view presenter ||  presenter view 间的;
 * 2.model 掉接口;
 */

public class VMessage<T> implements Parcelable {
    public int what;
    // TODO: 2018/2/8 影响存储;
    public String tag;
    // TODO: 2018/2/8 影响掉接口;
    public Class mClazz;
    public String methodName;
    // TODO: 2018/2/28 传参及判断; (key = interfaceCode)
    public Bundle data;
    //// TODO: 2018/2/28 辅助判断调接口;
    public int arg1;
    public int arg2;
    // TODO: 2018/2/28 传递对象;
    public Object obj;
    public T t;

    public VMessage(Class mClazz, String tag, Bundle data) {
        this.mClazz = mClazz;
        this.tag = tag;
        this.data = data;
    }

    /**
     *
     * @param tag 跟数据保存相关;
     *
     * @param mClazz  返回值类型;
     * @param data 参数封装;
     * @param methodName 方法名;
     */
    public VMessage(Class mClazz, String tag, Bundle data, String methodName) {
        this.mClazz = mClazz;
        this.tag = tag;
        this.data = data;
        this.methodName = methodName;
    }

    public VMessage() {
    }

    public VMessage(int what, T t) {
        this.what = what;
        this.t = t;
    }

    protected VMessage(Parcel in) {
        what = in.readInt();
        tag = in.readString();
        mClazz = (Class) in.readSerializable();
        arg1 = in.readInt();
        arg2 = in.readInt();
        data = in.readBundle();
        if (in.readInt() != 0) {
            obj = in.readParcelable(getClass().getClassLoader());
        }
    }

    public static final Creator<VMessage> CREATOR = new Creator<VMessage>() {
        @Override
        public VMessage createFromParcel(Parcel in) {
            return new VMessage(in);
        }

        @Override
        public VMessage[] newArray(int size) {
            return new VMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(what);
        dest.writeString(tag);
        dest.writeSerializable(mClazz);
        dest.writeInt(arg1);
        dest.writeInt(arg2);
        dest.writeBundle(data);
        if (obj != null) {
            try {
                Parcelable p = (Parcelable)obj;
                dest.writeInt(1);
                dest.writeParcelable(p, flags);
            } catch (ClassCastException e) {
                throw new RuntimeException(
                        "Can't marshal non-Parcelable objects across processes.");
            }
        } else {
            dest.writeInt(0);
        }
    }
}
