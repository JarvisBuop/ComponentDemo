package com.jarvisdong.uikit.adapter.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JarvisDong on 2017/12/8.
 * OverView:
 */

public interface OnConvertViewClickListener {
    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

    boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
}
