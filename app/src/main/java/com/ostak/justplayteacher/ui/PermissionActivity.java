package com.ostak.justplayteacher.ui;

import android.Manifest;
import android.os.Bundle;

import com.jarvisdong.uikit.baseui.DBaseActivity;

/**
 * Created by JarvisDong on 2018/5/19.
 *
 * @Description:
 * @see:
 * @deprecated
 */

public class PermissionActivity extends DBaseActivity {
    protected boolean isHavePermission = false;
    protected boolean isImmediately = false;
    protected String[] mPermissionArr = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    public int getContentViewId() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
