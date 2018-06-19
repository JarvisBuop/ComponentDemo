package com.jarvisdong.teaapp.domain;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.domain.contract.BaseConcreateContract;
import com.jarvisdong.teaapp.remote.CommonHttpResult;
import com.jarvisdong.teaapp.remote.RetrofitServer;
import com.jarvisdong.teaapp.util.ErrorUtil;
import com.jarvisdong.uikit.adapter.impl.IEventListener;
import com.jarvisdong.uikit.clear.UseCase;
import com.jarvisdong.uikit.clear.UseCaseHandler;
import com.jarvisdong.uikit.clear.concreate.CommonUseCase;
import com.jarvisdong.uikit.mvp.BaseModelDataSource;
import com.jarvisdong.uikit.util.ToastUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

import static com.jarvisdong.uikit.util.NotNullUtil.checkNotNull;


/**
 * @param //返回内容
 */
public class CucConnectConfig {
    private CompositeDisposable mDisposables;

    /**
     * 需要掉接口的返回泛型类,(CommonHttpResult已包装过)
     *
     * @param mDisposables
     * @param requestValue
     * @param callback
     */
    public void link(CompositeDisposable mDisposables, UseCase.RequestValues requestValue, BaseModelDataSource.LoadDatasCallback<UseCase.ResponseValues> callback) {
        this.mDisposables = checkNotNull(mDisposables);
        if (requestValue instanceof CommonUseCase.RequestValues) {
            CommonUseCase.RequestValues mRequest = (CommonUseCase.RequestValues) requestValue;
            checkNotNull(mRequest.getRequestMark(), "RequestMark not be null");
            checkNotNull(mRequest.getRequestMark().mClazz, "mClazz not be null");
            CommonObjCallBack(mRequest.getRequestMark().mClazz, mRequest, callback);
        } else {

        }
    }

    //通用;
    public <E extends Class> void CommonObjCallBack(E e, CommonUseCase.RequestValues requestValue, BaseModelDataSource.LoadDatasCallback<UseCase.ResponseValues> callback) {
        DisposableObserver<CommonHttpResult<E>> observer = new DisposableObserver<CommonHttpResult<E>>() {

            @Override
            public void onNext(@NonNull CommonHttpResult<E> CommonHttpResult) {
                callback.onDatasLoaded(new CommonUseCase.ResponseValues(CommonHttpResult, requestValue.getRequestMark()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onDataNotAvailable(e);
            }

            @Override
            public void onComplete() {

            }
        };
        mDisposables.add(observer);
        /**
         * 真正调取网络的地方;
         */
        invokeServer(e, observer, requestValue);
    }


//    public void CommonObjPageCallBack(E e, final int markCode, String markStr) {
//        Subscriber<AbeCommonHttpPageListResult<E>> subscriber = new Subscriber<AbeCommonHttpPageListResult<E>>() {
//            @Override
//            public void onCompleted() {
//                commonImpl.onCompleted();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                commonImpl.onError(e);
//            }
//
//            @Override
//            public void onNext(AbeCommonHttpPageListResult<E> result) {
//                if (result != null && result.getData() != null && result.getCode() == 200) {
//                    commonImpl.getModel(result.getMsg(), result.getData(), markCode);
//                } else {
//                    if (result != null && !TextUtils.isEmpty(result.getMsg())) {
//                        Toast.makeText(SoaApplication.getInstance(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(SoaApplication.getInstance(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        };
//        commonImpl.initModel(markCode, markStr, subscriber);//提交调取接口;
//    }

    /**
     * @param e
     * @param observer
     * @param requestValue
     * @param <E>
     */
    private <E extends Class> void invokeServer(E e, Observer<CommonHttpResult<E>> observer, @NonNull CommonUseCase.RequestValues requestValue) {
        if (requestValue.getRequestMark().data == null) return;
        Bundle paramBundle = requestValue.getRequestMark().data;
        String methodName = requestValue.getRequestMark().methodName;
        if (methodName != null) {
            LogUtils.e("methodname:" + methodName);
            try {
                ArrayList objectArgs = getObjectArgs(observer, methodName, paramBundle);
                if (objectArgs == null || objectArgs.size() <= 1) return;
                //混淆文件过不了;
//                Method[] methods = Class.forName("com.smartbuild.oa.data.remote.BilinServer").getMethods();
                Method[] methods = RetrofitServer.class.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName) && method.getParameterTypes().length == objectArgs.size()) {
                        method.invoke(RetrofitServer.getInstance(), objectArgs.toArray());
                    }
                }
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        }
    }

    private ArrayList getObjectArgs(Observer observer, String methodName, Bundle paramBundle) {
        ArrayList objectArgs = new ArrayList();
        objectArgs.add(observer);
        switch (methodName) {
            case "getInitAllWorkTaskPageByRx2"://新建任务初始化
                objectArgs.add(paramBundle.getString("token"));
                objectArgs.add(paramBundle.getString("worktaskTypeCode"));
                objectArgs.add(paramBundle.getString("parentWorktaskId"));
                break;

            case "":
                break;
        }
        return objectArgs;
    }

    public static final String INTERFACECODE = "interfaceCode";
    public static final int ADDWORKTASKINFO = 1;
    public static final int EXECUTE_COMMAND_BIG = 2;
    public static final int EXECUTE_COMMAND_SMALL = 3;

    //快速启动;
    public static <T extends Object> void getCommonServiceInvoke(@Nullable Context mContext, BaseConcreateContract.BaseConcreateViewer mViewer,
                                                                 UseCaseHandler mUseCaseHandler, CommonUseCase mCommonUseCase,
                                                                 CommonUseCase.RequestValues useCaseRequest, IEventListener<CommonHttpResult<T>> mListener) {
        mViewer.setLoadingIndicator(true, mContext == null ? MyApp.getAppInstansce().getString(R.string.please_wait) : mContext.getString(R.string.please_wait));
        mUseCaseHandler.execute(mCommonUseCase, useCaseRequest, new UseCase.UseCaseCallback<CommonUseCase.ResponseValues>() {

            @Override
            public void onSuccess(CommonUseCase.ResponseValues response) {
                mViewer.setLoadingIndicator(false, "");
                if (mListener != null) {
                    mListener.callbackChild(null, 0, (CommonHttpResult<T>) response.getLoadData(),0,null);
                }
            }

            @Override
            public void onError(@android.support.annotation.NonNull Throwable e) {
                mViewer.setLoadingIndicator(false, "");
                ToastUtil.toastImmediate(ErrorUtil.getErrorMsg(e));
            }
        });
    }

}
