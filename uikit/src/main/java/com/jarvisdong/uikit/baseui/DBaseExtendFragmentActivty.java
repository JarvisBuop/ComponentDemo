package com.jarvisdong.uikit.baseui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.baseui.manager.FragmentParam;

import java.util.List;


/**
 * Created by JarvisDong on 2017/10/29.
 * 处理activity中使用fragment的页面;
 * 全部fragment;
 */

public abstract class DBaseExtendFragmentActivty extends DBaseActivity {
    protected DBaseFragment mCurrentFragment;
    private boolean isBackOrStay = false;
    private int stayFragment = 1;
    private boolean mCloseWarned = true;
    private String closeWarningHint = Utils.getApp().getResources().getString(R.string.close_warning);
    private long timeRecord = 0;

    private static final String TAG = DBaseExtendFragmentActivty.class.getClass().getSimpleName();
    private boolean isDebug = true;

    @Override
    public abstract int getContentViewId();

    @Override
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    protected abstract void initVariable();

    @Override
    protected abstract void processLogic(Bundle savedInstanceState);

    public void setBackOrStay(boolean backOrStay) {
        isBackOrStay = backOrStay;
    }

    public void setmCloseWarned(boolean mCloseWarned, String warningStr) {
        this.mCloseWarned = mCloseWarned;
        if (!TextUtils.isEmpty(warningStr)) closeWarningHint = warningStr;
    }

    //===================================
    //  设置fragment
    //===================================

    /**
     * 放入回退栈中,fragment类,存放数据;
     *
     * @param cls
     * @param data
     */
    public void pushFragmentToBackStack(Class<?> cls, String fragTag, Object data, DBaseFragment from) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        param.data = data;
        param.from = from;
        param.fragTag = fragTag;
        goToThisFragment(param);
    }

    public void pushFragmentToBackStack(FragmentParam param) {
        goToThisFragment(param);
    }

    public String getFragmentTag(FragmentParam param) {
//        StringBuilder sb = new StringBuilder(param.cls.toString());
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(param.fragTag)) {
            sb.append(param.fragTag);
        }else {
            sb.append(param.cls.toString());
        }
        return sb.toString();
    }

    public String getFragmentTag(Class cls, String fragTag) {
        StringBuilder sb = new StringBuilder(cls.toString());
        if (!TextUtils.isEmpty(fragTag)) {
            sb.append(fragTag);
        }
        return sb.toString();
    }

    /**
     * 切换fragment,并加入到回退栈中;
     * 替换原来的fragmnet,
     * 调取原来的fragment的onLeave方法,调用此fragment的onEnter方法;
     *
     * @param param
     */
    private void goToThisFragment(FragmentParam param) {
        int containerId = param.from.getContainerId();
        Class<?> cls = param.cls;
        if (cls == null) {
            return;
        }
        try {
            String fragmentTag = getFragmentTag(param);
            FragmentManager fm = getSupportFragmentManager();
            DBaseFragment fragment = (DBaseFragment) fm.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                if (param.from != null) {
                    fragment = param.from;
                } else {
                    fragment = (DBaseFragment) cls.newInstance();
                }
            }
            if (mCurrentFragment != null && mCurrentFragment != fragment) {
                mCurrentFragment.onLeave();
            }
            fragment.onEnter(param.data);

            FragmentTransaction ft = fm.beginTransaction();
            if (fragment.isAdded()) {
                if (mCurrentFragment != null && mCurrentFragment != fragment) {
                    ft.hide(mCurrentFragment);
                }
                hideOtherStackFrag(ft);
                ft.show(fragment);
            } else {
                ft.add(containerId, fragment, fragmentTag);
                ft.addToBackStack(fragmentTag);
            }
            mCurrentFragment = fragment;

            ft.commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (isDebug) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            LogUtils.eTag(TAG, "frag::" + count);
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            StringBuilder sb = new StringBuilder();
            for (Fragment fragment : fragments) {
                sb.append(fragment.getTag() + "\n");
            }
            LogUtils.eTag(TAG, "child::" + sb.toString());
        }
    }

    private void hideOtherStackFrag(FragmentTransaction ft) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            ft.hide(fragment);
        }
    }

    /**
     * 显示fragment,调用onBackWithData方法;
     *
     * @param cls
     * @param data
     */
    public void goToFragment(Class<?> cls, String fragTag, Object data) {
        if (cls == null) {
            return;
        }
        String fragmentTag = getFragmentTag(cls, fragTag);
        DBaseFragment fragment = (DBaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment != null) {
            mCurrentFragment = fragment;
            fragment.onBackWithData(data);
        }
        getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);
    }

    /**
     * pop指定的frag,用于临时的frag清除;
     */
    public void popToFragment(String fragTag) {
        if (TextUtils.isEmpty(fragTag)) {
            return;
        }
        DBaseFragment fragment = (DBaseFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
        if (fragment != null) {
            getSupportFragmentManager().popBackStackImmediate(fragTag, 0);
        }
    }

    /**
     * 弹出最顶部的fragment;显示下一个fragment,运行onBackWithData;
     *
     * @param data
     */
    public void popTopFragment(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
        if (tryToUpdateCurrentAfterPop() && mCurrentFragment != null) {
            mCurrentFragment.onBackWithData(data);
        }
    }


    private boolean tryToUpdateCurrentAfterPop() {
        FragmentManager fm = getSupportFragmentManager();
        int cnt = fm.getBackStackEntryCount();
        if (cnt > 0) {
            String name = fm.getBackStackEntryAt(cnt - 1).getName();
            Fragment fragment = fm.findFragmentByTag(name);
            if (fragment != null && fragment instanceof DBaseFragment) {
                mCurrentFragment = (DBaseFragment) fragment;
            }
            return true;
        }
        return false;
    }

    /**
     * 弹出所有fragment,显示rootfragment;
     *
     * @param data
     */
    public void popToRoot(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        }
        popTopFragment(data);
    }

    /**
     * 处理硬件返回键的逻辑;
     */
    protected void doReturnBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1 && isTaskRoot()) {
            if (mCloseWarned && !TextUtils.isEmpty(closeWarningHint)) {
                if (isDoubleClick()) {
                    finish();
                } else {
                    Toast.makeText(this, closeWarningHint, Toast.LENGTH_SHORT).show();
                }
            } else {
                finish();
            }
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && mCurrentFragment != null) {
                mCurrentFragment.onBack();
            }
        }
    }

    private boolean isDoubleClick() {
        long longTime = System.currentTimeMillis();
        if (longTime - timeRecord <= 2000) {
            return true;
        }
        timeRecord = longTime;
        return false;
    }

    /**
     * process back pressed
     */
    @Override
    public void onBackPressed() {

        // process back for fragment
        if (isBackOrStay) {
            super.onBackPressed();
        }

        // process back for fragment
        boolean enableBackPressed = true;
        if (mCurrentFragment != null) {
            enableBackPressed = !mCurrentFragment.processBackPressed();
        }
        if (enableBackPressed) {
            doReturnBack();
        }
    }


    protected DBaseFragment switchFragment(DBaseFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }
        return fragment;
    }

    /**
     * 内嵌fragment的切换
     *
     * @param containerViewId
     * @param from
     * @param to
     */
    protected void switchChildFragment(Fragment fragment, int containerViewId, Fragment from, Fragment to, boolean isback) {
        FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
        ft.hide(from);
        ft.replace(containerViewId, to);
        if (isback) {
            ft.addToBackStack(to.getClass().getName());
        }
        try {
            ft.commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }
}
