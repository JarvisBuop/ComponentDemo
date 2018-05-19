package com.jarvisdong.uikit.adapter.rclistener;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by JarvisDong on 2018/3/15.
 * OverView: 头部吸顶效果;
 */

public class OnRcSectionScrollListener extends RecyclerView.OnScrollListener {
    private int itemHeight = 5;
    private final View headerView;

    public static final int HAS_STICKY_VIEW = 0;
    public static final int NONE_STICKY_VIEW = 1;


    public OnRcSectionScrollListener(View headerView) {
        this.headerView = headerView;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        View childViewUnder = recyclerView.findChildViewUnder(headerView.getMeasuredWidth() / 2, itemHeight);
        if (childViewUnder != null && childViewUnder.getContentDescription() != null) {
            if (headerView instanceof TextView) {
                ((TextView)headerView).setText(String.valueOf(childViewUnder.getContentDescription()));
            }
        }
        View txtTrans = recyclerView.findChildViewUnder(headerView.getMeasuredWidth() / 2, headerView.getMeasuredHeight() + 1);
        if (txtTrans != null && txtTrans.getTag() != null) {
            int status = (int) txtTrans.getTag();
            int dealtY = txtTrans.getTop() - headerView.getMeasuredHeight();
            switch (status) {
                case HAS_STICKY_VIEW:
                    default:
                    if (txtTrans.getTop() > 0) {
                        headerView.setTranslationY(dealtY);
                    } else {
                        headerView.setTranslationY(0);
                    }
                    break;
                case NONE_STICKY_VIEW:
                    headerView.setTranslationY(0);
                    break;
            }

        }
    }
}
