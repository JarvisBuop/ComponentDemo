package com.jarvisdong.uikit.baseui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.util.IntentUtils;


/**
 * Created by JarvisDong on 2017/10/29.
 * activity 基准类;
 */

public abstract class DBaseActivity extends AppCompatActivity {
    //==========================================
    //取消输入框;
    private boolean isEnableCloseSoftInputMethod = false;
    //==========================================
    //转场动画;
    private boolean isOverridePendingTransition = false;
    private View viewTrans = null;
    private String viewTransMark = null;
    private Pair<View, String>[] mPair;
    //==========================================
    protected Context mContext;
    private ProgressDialog dialog;
    protected String TAG;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(getContentViewId() <= 0 ? R.layout.loading_frame : getContentViewId());
        mContext = DBaseActivity.this;
        TAG = this.getClass().getSimpleName();
        initView(savedInstanceState);
        initVariable();
        processLogic(savedInstanceState);
    }

    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void finish() {
        super.finish();
        if (isOverridePendingTransition) {
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isOverridePendingTransition) {
            ActivityCompat.finishAfterTransition(this);
        }
    }


    @Override
    public void startActivity(Intent intent) {
        if (isOverridePendingTransition) {
            if (viewTrans != null) {
                IntentUtils.startActivityByShare(mContext, viewTrans, viewTransMark, intent);
            } else if (mPair != null) {
                IntentUtils.startActivityByShare(mContext, intent, mPair);
            }
        } else {
            super.startActivity(intent);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (isOverridePendingTransition) {
            if (viewTrans != null) {
                IntentUtils.startActivityByShareForResult(mContext, viewTrans, viewTransMark, intent, requestCode);
            } else if (mPair != null) {
                IntentUtils.startActivityByShareForResult(mContext, intent, requestCode, mPair);
            }
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnableCloseSoftInputMethod && event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    //*****************************************
    //  *************设置方法*****************
    //*****************************************

    /**
     * 使能点击输入框外部关闭输入框;
     *
     * @param isEnable
     */
    public void enableCloseSoftInputMethod(boolean isEnable) {
        isEnableCloseSoftInputMethod = isEnable;
    }

    public void enableOverridePendingTransition(boolean isEnable, View view, String mark) {
        this.viewTransMark = mark;
        this.viewTrans = view;
        isOverridePendingTransition = isEnable;
    }

    public void enableOverridePendingTransition(boolean isEnable, Pair<View, String>... pair) {
        this.mPair = pair;
        isOverridePendingTransition = isEnable;
    }

    /**
     * 获取主题色
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取深主题色
     */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 调用接口时，显示dialog
     */
    public void showDialog(final DialogInterface.OnCancelListener mCancleListener) {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(this);
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

    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }


    //=======================================
    //抽象方法
    //=======================================

    /**
     * 初始化布局;
     *
     * @return
     */
    public abstract int getContentViewId();

    /**
     * 初始化布局以及View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化变量
     */
    protected abstract void initVariable();

    /**
     * 处理业务逻辑，状态恢复等操作
     */
    protected abstract void processLogic(Bundle savedInstanceState);

}
