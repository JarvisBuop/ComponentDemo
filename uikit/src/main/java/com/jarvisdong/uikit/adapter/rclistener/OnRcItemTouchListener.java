package com.jarvisdong.uikit.adapter.rclistener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JarvisDong on 2018/3/13.
 * OverView: recycler的事件手势回调;
 */

public class OnRcItemTouchListener implements RecyclerView.OnItemTouchListener {
    private final GestureDetectorCompat mGestureDetector;
    private final RecyclerView mRecycler;

    public OnRcItemTouchListener(RecyclerView mRecycler) {
        this.mRecycler = mRecycler;
        this.mGestureDetector = new GestureDetectorCompat(mRecycler.getContext(), new ItemTouchHelperGestureListener());
        mGestureDetector.setIsLongpressEnabled(false);
    }

    public OnRcItemTouchListener(RecyclerView mRecycler, GestureDetector.SimpleOnGestureListener simpleOnGestureListener) {
        this.mRecycler = mRecycler;
        this.mGestureDetector = new GestureDetectorCompat(mRecycler.getContext(), simpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = mRecycler.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = mRecycler.getChildViewHolder(child);
                onSingleTapUpCallback(vh);
            }
            return true;
        }
    }

    public void onSingleTapUpCallback(RecyclerView.ViewHolder vh) {

    }
}
