package com.jarvisdong.uikit.impl;

import android.view.View;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public interface ICommonListener<T> {
    void clickPostBack(View view, int position, T obj);
}
