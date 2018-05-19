/*
 * Copyright 2017 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jarvisdong.uikit.adapter.wrapper;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.blankj.utilcode.util.LogUtils;
import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.adapter.impl.SwipeMenuCreator;
import com.jarvisdong.uikit.adapter.impl.SwipeMenuItemClickListener;
import com.jarvisdong.uikit.adapter.swipe.SwipeMenu;
import com.jarvisdong.uikit.adapter.swipe.SwipeMenuLayout;
import com.jarvisdong.uikit.adapter.swipe.SwipeMenuView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.jarvisdong.uikit.adapter.wrapper.EmptyWrapper.ITEM_TYPE_EMPTY;
import static com.jarvisdong.uikit.adapter.wrapper.HeaderAndFooterWrapper.BASE_ITEM_TYPE_FOOTER;
import static com.jarvisdong.uikit.adapter.wrapper.HeaderAndFooterWrapper.BASE_ITEM_TYPE_HEADER;
import static com.jarvisdong.uikit.adapter.wrapper.LoadMoreWrapper.ITEM_TYPE_LOAD_MORE;


/**
 * 装饰类-左右滑菜单;
 * tips:根据viewtype 来判断那种item可以拥有菜单;
 */
public class SwipeAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isDebug = false;
    private final RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private SwipeMenuCreator mSwipeMenuCreator;
    private SwipeMenuItemClickListener mSwipeMenuItemClickListener;
    private boolean allowSwipe = true;


    public void setAllowSwipe(boolean allowSwipe) {
        this.allowSwipe = allowSwipe;
        notifyDataSetChanged();
    }

    public SwipeAdapterWrapper(RecyclerView.Adapter adapter, RecyclerView mRecyclerView) {
        checkAdapterExist(adapter);
        this.mAdapter = adapter;
        this.mRecyclerView = mRecyclerView;
        if (mRecyclerView != null)
            onScrollConflict();
    }

    public RecyclerView.Adapter getOriginAdapter() {
        return mAdapter;
    }

    private boolean checkAdapterExist(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new IllegalStateException("not inner adapter ,please set invalidate adapter!!");
        } else {
            return true;
        }
    }

    /**
     * Set to create menu listener.
     *
     * @param swipeMenuCreator listener.
     */
    public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        this.mSwipeMenuCreator = swipeMenuCreator;
    }

    /**
     * Set to click menu listener.
     *
     * @param swipeMenuItemClickListener listener.
     */
    public void setSwipeMenuItemClickListener(SwipeMenuItemClickListener swipeMenuItemClickListener) {
        this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LogUtils.e("viewType="+viewType);
        final RecyclerView.ViewHolder viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
        if (mSwipeMenuCreator == null || !allowSwipe || isOtherWrapper(viewType)) return viewHolder;

        final SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_swipe_view_item, parent, false);
        SwipeMenu swipeLeftMenu = new SwipeMenu(swipeMenuLayout, viewType);
        SwipeMenu swipeRightMenu = new SwipeMenu(swipeMenuLayout, viewType);

        mSwipeMenuCreator.onCreateMenu(swipeLeftMenu, swipeRightMenu, viewType);

        int leftMenuCount = swipeLeftMenu.getMenuItems().size();
        if (leftMenuCount > 0) {
            SwipeMenuView swipeLeftMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_left);
            // noinspection WrongConstant
            swipeLeftMenuView.setOrientation(swipeLeftMenu.getOrientation());
            swipeLeftMenuView.createMenu(swipeLeftMenu, swipeMenuLayout, mSwipeMenuItemClickListener, LEFT_DIRECTION);
        }

        int rightMenuCount = swipeRightMenu.getMenuItems().size();
        if (rightMenuCount > 0) {
            SwipeMenuView swipeRightMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_right);
            // noinspection WrongConstant
            swipeRightMenuView.setOrientation(swipeRightMenu.getOrientation());
            swipeRightMenuView.createMenu(swipeRightMenu, swipeMenuLayout, mSwipeMenuItemClickListener, RIGHT_DIRECTION);
        }

        ViewGroup viewGroup = (ViewGroup) swipeMenuLayout.findViewById(R.id.swipe_content);
        viewGroup.addView(viewHolder.itemView);

        try {
            // TODO: 2018/3/19 将自定义的swipeview设置给viewholder 的属性itemView
            Field itemView = getSupperClass(viewHolder.getClass()).getDeclaredField("itemView");
            if (!itemView.isAccessible()) itemView.setAccessible(true);
            itemView.set(viewHolder, swipeMenuLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    private boolean isOtherWrapper(int viewType) {
        if (viewType == ITEM_TYPE_EMPTY || viewType == ITEM_TYPE_LOAD_MORE ||
                viewType >= BASE_ITEM_TYPE_HEADER || viewType >= BASE_ITEM_TYPE_FOOTER
                ) {
            return true;
        }
        return false;
    }

    private Class<?> getSupperClass(Class<?> aClass) {
        Class<?> supperClass = aClass.getSuperclass();
        if (supperClass != null && !supperClass.equals(Object.class)) {
            return getSupperClass(supperClass);
        }
        return aClass;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        bindItemView(holder);
        mAdapter.onBindViewHolder(holder, position, payloads);
    }

    private void bindItemView(RecyclerView.ViewHolder holder) {
        View itemView = holder.itemView;
        if (itemView instanceof SwipeMenuLayout) {
            SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) itemView;
            int childCount = swipeMenuLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = swipeMenuLayout.getChildAt(i);
                if (childView instanceof SwipeMenuView) {
                    ((SwipeMenuView) childView).bindViewHolder(holder);
                }
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mAdapter.onViewAttachedToWindow(holder);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        mAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        if (checkAdapterExist(mAdapter)) {
            return mAdapter.getItemId(position);
        }
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (checkAdapterExist(mAdapter)) {
            mAdapter.onViewRecycled(holder);
        } else {
            super.onViewRecycled(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (checkAdapterExist(mAdapter)) {
            return mAdapter.onFailedToRecycleView(holder);
        } else {
            return super.onFailedToRecycleView(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (checkAdapterExist(mAdapter)) {
            mAdapter.onViewDetachedFromWindow(holder);
        } else {
            super.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (checkAdapterExist(mAdapter)) {
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        } else {
            super.onDetachedFromRecyclerView(recyclerView);
        }
    }

    /**
     * open menu on left.
     *
     * @param position position.
     */
    public void smoothOpenLeftMenu(int position) {
        smoothOpenMenu(position, LEFT_DIRECTION, SwipeMenuLayout.DEFAULT_SCROLLER_DURATION);
    }

    /**
     * open menu on left.
     *
     * @param position position.
     * @param duration time millis.
     */
    public void smoothOpenLeftMenu(int position, int duration) {
        smoothOpenMenu(position, LEFT_DIRECTION, duration);
    }

    /**
     * open menu on right.
     *
     * @param position position.
     */
    public void smoothOpenRightMenu(int position) {
        smoothOpenMenu(position, RIGHT_DIRECTION, SwipeMenuLayout.DEFAULT_SCROLLER_DURATION);
    }

    /**
     * open menu on right.
     *
     * @param position position.
     * @param duration time millis.
     */
    public void smoothOpenRightMenu(int position, int duration) {
        smoothOpenMenu(position, RIGHT_DIRECTION, duration);
    }

    /**
     * open menu.
     *
     * @param position  position.
     * @param direction use {@link #LEFT_DIRECTION}, {@link #RIGHT_DIRECTION}.
     * @param duration  time millis.
     */
    public void smoothOpenMenu(int position, @DirectionMode int direction, int duration) {
        if (mOldSwipedLayout != null) {
            if (mOldSwipedLayout.isMenuOpen()) {
                mOldSwipedLayout.smoothCloseMenu();
            }
        }
        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(position);
        if (vh != null) {
            View itemView = getSwipeMenuView(vh.itemView);
            if (itemView instanceof SwipeMenuLayout) {
                mOldSwipedLayout = (SwipeMenuLayout) itemView;
                if (direction == RIGHT_DIRECTION) {
                    mOldTouchedPosition = position;
                    mOldSwipedLayout.smoothOpenRightMenu(duration);
                } else if (direction == LEFT_DIRECTION) {
                    mOldTouchedPosition = position;
                    mOldSwipedLayout.smoothOpenLeftMenu(duration);
                }
            }
        }
    }

    /**
     * Close menu.
     */
    public void smoothCloseMenu() {
        if (mOldSwipedLayout != null && mOldSwipedLayout.isMenuOpen()) {
            mOldSwipedLayout.smoothCloseMenu();
        }
    }


    /**
     * ---------------------
     * Oritation
     * ---------------------
     */
    public static final int LEFT_DIRECTION = 1;
    public static final int RIGHT_DIRECTION = -1;

    @IntDef({LEFT_DIRECTION, RIGHT_DIRECTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DirectionMode {
    }

    /**
     * deal conflict
     */

    private int mDownX;
    private int mDownY;
    private int mScaleTouchSlop;
    private static final int INVALID_POSITION = -1;
    protected SwipeMenuLayout mOldSwipedLayout;
    protected int mOldTouchedPosition = INVALID_POSITION;

    private void onScrollConflict() {
        mScaleTouchSlop = ViewConfiguration.get(mRecyclerView.getContext()).getScaledTouchSlop();
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (isDebug) LogUtils.e("onInterceptTouchEvent:" + e.toString() + "\n");
                boolean isIntercepted = false;
                if (!allowSwipe)  // swipe and menu conflict.
                    return isIntercepted;
                else {
                    if (e.getPointerCount() > 1) return true;
                    int action = e.getAction();
                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN: {
                            mDownX = x;
                            mDownY = y;
                            isIntercepted = false;

                            int touchingPosition = rv.getChildAdapterPosition(rv.findChildViewUnder(x, y));
                            if (touchingPosition != mOldTouchedPosition && mOldSwipedLayout != null && mOldSwipedLayout.isMenuOpen()) {
                                mOldSwipedLayout.smoothCloseMenu();
                                isIntercepted = true;
                            }

                            if (isIntercepted) {
                                mOldSwipedLayout = null;
                                mOldTouchedPosition = INVALID_POSITION;
                            } else {
                                RecyclerView.ViewHolder vh = rv.findViewHolderForAdapterPosition(touchingPosition);
                                if (vh != null) {
                                    View itemView = getSwipeMenuView(vh.itemView);
                                    if (itemView instanceof SwipeMenuLayout) {
                                        mOldSwipedLayout = (SwipeMenuLayout) itemView;
                                        mOldTouchedPosition = touchingPosition;
                                    }
                                }
                            }
                            break;
                        }
                        // They are sensitive to retain sliding and inertia.
                        case MotionEvent.ACTION_MOVE: {
                            isIntercepted = handleUnDown(x, y, isIntercepted);
                            if (mOldSwipedLayout == null) break;
                            ViewParent viewParent = rv.getParent();
                            if (viewParent == null) break;

                            int disX = mDownX - x;
                            // 向左滑，显示右侧菜单，或者关闭左侧菜单。
                            boolean showRightCloseLeft = disX > 0 && (mOldSwipedLayout.hasRightMenu() || mOldSwipedLayout.isLeftCompleteOpen());
                            // 向右滑，显示左侧菜单，或者关闭右侧菜单。
                            boolean showLeftCloseRight = disX < 0 && (mOldSwipedLayout.hasLeftMenu() || mOldSwipedLayout.isRightCompleteOpen());
                            viewParent.requestDisallowInterceptTouchEvent(showRightCloseLeft || showLeftCloseRight);
                        }
                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_CANCEL:
                        {
                            isIntercepted = handleUnDown(x, y, isIntercepted);
                            break;
                        }
                    }
                }
                return isIntercepted;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (isDebug) LogUtils.e("onTouchEvent:" + e.toString() + "\n");
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mOldSwipedLayout != null && mOldSwipedLayout.isMenuOpen()) {
                            mOldSwipedLayout.smoothCloseMenu();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                if (isDebug)
                    LogUtils.e("onRequestDisallowInterceptTouchEvent:" + disallowIntercept + "\n");


            }
        });
    }

    private boolean handleUnDown(int x, int y, boolean defaultValue) {
        int disX = mDownX - x;
        int disY = mDownY - y;

        // swipe
        if (Math.abs(disX) > mScaleTouchSlop && Math.abs(disX) > Math.abs(disY))
            return false;
        // click
        if (Math.abs(disY) < mScaleTouchSlop && Math.abs(disX) < mScaleTouchSlop)
            return false;
        return defaultValue;
    }

    private View getSwipeMenuView(View itemView) {
        if (itemView instanceof SwipeMenuLayout) return itemView;
        List<View> unvisited = new ArrayList<>();
        unvisited.add(itemView);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            if (!(child instanceof ViewGroup)) { // view
                continue;
            }
            if (child instanceof SwipeMenuLayout) return child;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return itemView;
    }
}