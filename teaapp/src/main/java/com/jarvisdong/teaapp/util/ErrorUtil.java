package com.jarvisdong.teaapp.util;

import android.text.TextUtils;

import com.jarvisdong.teaapp.remote.CommonHttpResult;
import com.jarvisdong.teaapp.remote.RxBus2;
import com.jarvisdong.teaapp.remote.event.AppEvent;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jarvisdong.teaapp.remote.event.AppEvent.EVENT_TYPE.EVENT_TOKEN_ERROR;


/**
 * Created by alex on 16/6/17.
 */
public class ErrorUtil {
    public static String getErrorMsg(Throwable e) {
        String msg = null;
        int statusCode = 0;
        if (e instanceof HttpException) {
            ResponseBody errorBody = ((HttpException) e).response().errorBody();
            statusCode = ((HttpException) e).code();
            // TODO: 2017/5/19 判断token是否为403,token错误,退出登录;
            if (statusCode != 0) {
                if(statusCode == 403){
                    RxBus2.getDefault().send(new AppEvent(EVENT_TOKEN_ERROR,null));//退出登录,重新登录;
                }
            }

            if (errorBody != null) {
                try {
                    // TODO: 2017/5/19 提示错误信息;
                    Object errorCurrent = GsonConverterFactory
                            .create()
                            .responseBodyConverter(CommonHttpResult.class,
                                    CommonHttpResult.class.getAnnotations(), null)
                            .convert(errorBody);
                    if (errorCurrent != null) {
                        if (errorCurrent instanceof CommonHttpResult) {
                            CommonHttpResult result = (CommonHttpResult) errorCurrent;
                            msg = result.getErrorMsg();
                            return msg;
                        }
                    }

                    Object errorResponse = GsonConverterFactory
                            .create()
                            .responseBodyConverter(CommonHttpResult.class,
                                    CommonHttpResult.class.getAnnotations(), null)
                            .convert(errorBody);

                    if (errorResponse != null) {
                        if (errorResponse instanceof CommonHttpResult) {
                            CommonHttpResult result = (CommonHttpResult) errorResponse;
                            msg = result.getErrorMsg();
                        }
                    }


                } catch (Exception re) {
                    re.toString();
                }
            }
        } else if (e instanceof SocketException
                || e instanceof UnknownHostException) {
            msg = "网络有点问题";
            return msg;
        } else if (e instanceof SocketTimeoutException) {
            msg = "请求超时";
            return msg;
        }
        if (TextUtils.isEmpty(msg)) {
            if (statusCode >= 500) {
                msg = "服务器内部错误";
            } else {
                msg = "未知错误:" + e.toString();
            }
        }

        return msg;
    }
}
