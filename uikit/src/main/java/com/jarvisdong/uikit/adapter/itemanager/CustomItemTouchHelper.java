package com.jarvisdong.uikit.adapter.itemanager;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.blankj.utilcode.util.LogUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by JarvisDong on 2018/3/13.
 * OverView:
 */

public class CustomItemTouchHelper<T> extends ItemTouchHelper.Callback {
    private boolean isDebug = true;
    public static final float ALPHA_FULL = 1.0f;
    RecyclerView.Adapter mAdapter;
    List<T> mDatas;

    public CustomItemTouchHelper(RecyclerView.Adapter mAdapter, List<T> mDatas) {
        this.mDatas = mDatas;
        this.mAdapter = mAdapter;
    }

    public CustomItemTouchHelper(RecyclerView.Adapter mAdapter, List<T> mDatas, ItemTouchHelperCallback mCallback) {
        this.mDatas = mDatas;
        this.mAdapter = mAdapter;
        this.mCallback = mCallback;
    }

    /**
     * viewholder能接受的actio
     * 生成拖动中flag
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            dragFlags |= (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            swipeFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 用户拖动item
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (isDebug)
            LogUtils.e("from:" + fromPosition + " to:" + toPosition);
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }


        if (mAdapter != null && mDatas != null && !mDatas.isEmpty()) {
            try {
                Collections.swap(mDatas, fromPosition, toPosition);
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            } catch (Exception e) {
                if (isDebug)
                    LogUtils.e(e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {

        return super.canDropOver(recyclerView, current, target);
    }

    // TODO: 2018/3/13 拖拽结束
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != null) {
            int clearPosition = viewHolder.getAdapterPosition();
            if (isDebug)
                LogUtils.e("clearPosition:" + clearPosition);
            if (mCallback != null) {
                mCallback.clearView(viewHolder);
            }
        }
        super.clearView(recyclerView, viewHolder);
    }

    // TODO: 2018/3/13 拖拽开始
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            int selectPosition = viewHolder.getAdapterPosition();
            if (isDebug)
                LogUtils.e("selectPosition:" + selectPosition + " actionState:" + actionState);
            if (mCallback != null) {
                mCallback.onSelectedChanged(viewHolder, actionState);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }


    /**
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mAdapter != null && mDatas != null) {
            mDatas.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    }

    /**
     * @return false:不是长按拖拽;改成自定义view触摸拖拽;
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    private ItemTouchHelperCallback mCallback = null;

    public void setItemTouchHelperCallback(ItemTouchHelperCallback mCallback) {
        this.mCallback = mCallback;
    }

    public interface ItemTouchHelperCallback {
        void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);

        void clearView(RecyclerView.ViewHolder viewHolder);
    }
}
