package com.jarvisdong.uikit.baseui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.baseui.manager.FragmentLifeCycleImpl;
import com.jarvisdong.uikit.baseui.manager.IComponentContainer;
import com.jarvisdong.uikit.baseui.manager.LifeCycleComponent;
import com.jarvisdong.uikit.baseui.manager.LifeCycleComponentManager;


/**
 * Created by JarvisDong on 2017/10/29.
 * fragment 基准类;
 */

public abstract class DBaseFragment extends Fragment implements FragmentLifeCycleImpl, IComponentContainer {
    public String TAG = this.getClass().getSimpleName();
    protected LayoutInflater mInflater;
    private ProgressDialog dialog;
    protected Context mContext;
    /**
     * fragment容器Id;
     */
    private int containerId;

    private boolean isCacheView = false;
    private View mCacheView;

    //周期管理;
    protected Object mDataIn;
    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();
    private boolean mFirstResume = true;


    public boolean ismFirstResume() {
        return mFirstResume;
    }

    public Object getmDataIn() {
        return mDataIn;
    }

    public void setCacheView(boolean cacheView) {
        isCacheView = cacheView;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    /**
     * 调用接口时，显示dialog
     */
    public void showDialog(DialogInterface.OnCancelListener mCancleListener) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(mContext.getResources().getString(R.string.loading));
        dialog.setOnCancelListener(mCancleListener);
        dialog.show();
    }

    /**
     * 处理接口返回时，隐藏dialog
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 简化findviewbyid;
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(@IdRes int resId) {
        return (T) getView().findViewById(resId);
    }

    //================================
    //=========生命周期;============
    //================================

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext= context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        if (isCacheView) {
            if (mCacheView == null) {
                mCacheView = createView(inflater, container, savedInstanceState);
            }
            // 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
            ViewGroup parent = (ViewGroup) mCacheView.getParent();
            if (parent != null) {
                parent.removeView(mCacheView);
            }
            return mCacheView;
        }
        View view = createView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        processLogic(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFirstResume) {
            onBack();
        }
        if (mFirstResume) {
            mFirstResume = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        onLeave();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mComponentContainer.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //=================================
    //=============抽象方法===========
    //=================================

    /**
     * 初始化布局;
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    protected abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化控件;
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    /**
     * 初始化数据,添加逻辑;
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);


    @Override
    public void onEnter(Object data) {
        mDataIn = data;
    }

    @Override
    public void onLeave() {
        mComponentContainer.onBecomesTotallyInvisible();
    }

    @Override
    public void onBack() {
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public void onBackWithData(Object data) {
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    /**
     * return true if activity onbackpress event has been processed and shield onbackpress key;
     *
     * @return
     */
    @Override
    public boolean processBackPressed() {
        return false;
    }

    /**
     * (FragmentLifeCycleImpl接口->)DBaseFragment方法->LifeCycleComponent接口->具体的实现类;
     *
     * @param component 为了给子fragment统一管理;
     *  给需要管理生命周期的fragment实现component接口即可;
     */
    @Override
    public void addComponent(LifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }
}
