package com.jarvisdong.teaapp.remote;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JarvisDong on 2018/5/19.
 *
 * @Description:
 * @see:
 */

public class CommonHttpResult<T> {
    @SerializedName("bSucceed")
    public boolean result;
    @SerializedName("errCode")
    public int code;
    @SerializedName("errMsg")
    public String msg;
    @SerializedName("Result")
    public T data;

    public boolean isSuccess() {
        return result;
    }

    public boolean isTokenError() {
        return code == 403;
    }

    public String getErrorMsg() {
        return (TextUtils.isEmpty(msg)) ? "操作失败" : msg;
    }
}

