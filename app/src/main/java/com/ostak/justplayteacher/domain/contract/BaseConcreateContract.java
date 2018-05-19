package com.ostak.justplayteacher.domain.contract;

import android.content.Intent;
import android.view.View;

import com.jarvisdong.uikit.mvp.BasePresenterOperate;
import com.jarvisdong.uikit.mvp.BaseViewerOperate;


/**
 * Created by JarvisDong on 2018/2/26.
 * OverView:
 */

public interface BaseConcreateContract {

    interface BaseConcreatePresenter extends BasePresenterOperate {
        void subscribe();

        void unsubscribe();

        void result(int requestCode, int resultCode, Intent bundle);
    }

    interface BaseConcreateViewer extends BaseViewerOperate {
        void setLoadingIndicator(boolean active, String msg);

        void back();

        boolean isCanSubmit();

        View getWantedView(int code);
    }
}
