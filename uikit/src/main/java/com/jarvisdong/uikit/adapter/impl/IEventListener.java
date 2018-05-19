package com.jarvisdong.uikit.adapter.impl;

import android.view.View;

/**
 * Created by JarvisDong on 2017/12/8.
 * OverView:
 */

public interface IEventListener<T> {
    /**
     *
     * @param view 当前点击的view
     * @param pos 当前在适配器中的位置;
     * @param obj payLoads 点击事件自定义对象,用于view的id,区分不开的情况;
     * @param viewtype 当前viewholder的viewtype;
     * @param callback viewholder中存储的对象;
     */
    void callbackChild(View view, int pos, T obj, int viewtype, Object callback);
}
