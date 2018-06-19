package com.jarvisdong.teaapp.domain.presenter;

import android.app.Activity;
import android.content.Context;

import com.jarvisdong.teaapp.domain.contract.BaseConcreateContract;
import com.jarvisdong.teaapp.domain.contract.Injection;
import com.jarvisdong.uikit.clear.UseCaseHandler;
import com.jarvisdong.uikit.clear.concreate.CommonUseCase;
import com.jarvisdong.uikit.util.NotNullUtil;

import static com.jarvisdong.teaapp.domain.contract.Injection.provideCommonUseCase;

/**
 * Created by JarvisDong on 2018/2/8.
 * OverView:
 * 新建任务-基准类presenter;
 */

public abstract class TaskConcreatePresenter implements BaseConcreateContract.BaseConcreatePresenter {
    protected final BaseConcreateContract.BaseConcreateViewer mViewer;
    protected final UseCaseHandler mUseCaseHandler;
    protected final CommonUseCase mCommonUseCase;
    protected final Activity mContext;

    public TaskConcreatePresenter(BaseConcreateContract.BaseConcreateViewer viewer, UseCaseHandler mUseCaseHandler, Context mContext) {
        NotNullUtil.checkNotNull(viewer);
        NotNullUtil.checkNotNull(mUseCaseHandler);
        this.mViewer = NotNullUtil.checkNotNull(viewer);
        this.mUseCaseHandler = NotNullUtil.checkNotNull(mUseCaseHandler);
        this.mCommonUseCase = provideCommonUseCase(mContext);
        this.mContext = (Activity) mContext;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        Injection.cancelUseCase(mCommonUseCase);
    }

}
