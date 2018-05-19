package com.jarvisdong.uikit.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.jarvisdong.uikit.adapter.base.MultiItemTypeAdapter;
import com.jarvisdong.uikit.adapter.entity.IExpandable;
import com.jarvisdong.uikit.adapter.impl.SwipeMenuCreator;
import com.jarvisdong.uikit.adapter.impl.SwipeMenuItemClickListener;
import com.jarvisdong.uikit.adapter.itemanager.ItemViewDelegate;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.jarvisdong.uikit.adapter.wrapper.EmptyWrapper;
import com.jarvisdong.uikit.adapter.wrapper.HeaderAndFooterWrapper;
import com.jarvisdong.uikit.adapter.wrapper.LoadMoreWrapper;
import com.jarvisdong.uikit.adapter.wrapper.SwipeAdapterWrapper;

import java.util.Collection;
import java.util.List;

/**
 * Created by JarvisDong on 2018/3/15.
 * OverView: 通用快捷适配器
 * 已集成功能:
 * 1.无数据-空布局;
 * 2.添加-头布局,脚布局;
 * 3.刷新功能;
 * 4.左右滑菜单功能;
 * 5.收缩展开列表项功能;
 */

public abstract class QuickFuncAdapter<T,K extends ViewHolder> extends MultiItemTypeAdapter<T,K> {
    public static final int EMPTY_VIEW = 0x00000111;
    public static final int HEADER_FOOTER_VIEW = 0x00000222;
    public static final int LOADING_VIEW = 0x00000555;
    public static final int HEADER_LOADING_VIEW = 0x00000666;

    private final RecyclerView recyclerview;
    protected EmptyWrapper mEmptyWrapper;
    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    protected LoadMoreWrapper mLoadMoreWrapper;
    private SwipeAdapterWrapper mSwipeAdapterWrapper;
    private final int adapterStatus;

    private boolean isDebug = true;
    private boolean isHasEmpty = false;
    private boolean isHasHeaderOrFooter = false;
    private boolean isHasLoadMore = false;
    private boolean isSwipeEnable = false;//是否使用菜单;


    public QuickFuncAdapter(RecyclerView recyclerView, List datas, int adapterStatus) {
        super(recyclerView.getContext(), datas);
        this.recyclerview = recyclerView;
        this.adapterStatus = adapterStatus;
        final int layoutId = addCustomItemViewDelegate(this);
        if (layoutId > 0) {
            addItemViewDelegate(new ItemViewDelegate<T>() {
                @Override
                public int getItemViewLayoutId() {
                    return layoutId;
                }

                @Override
                public boolean isForViewType(T item, int position) {
                    return true;
                }

                @Override
                public void convert(ViewHolder holder, T t, int position) {
                    QuickFuncAdapter.this.convert(holder, t, position);
                }
            });
        }
        if (useItemViewDelegateManager()) {
            switch (adapterStatus) {
                case EMPTY_VIEW:
                    mEmptyWrapper = new EmptyWrapper(this);
                    isHasEmpty = true;
                    break;
                case HEADER_FOOTER_VIEW:
                    mEmptyWrapper = new EmptyWrapper(this);
                    mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mEmptyWrapper);
                    isHasEmpty = true;
                    isHasHeaderOrFooter = true;
                    break;
                case LOADING_VIEW:
                    mEmptyWrapper = new EmptyWrapper(this);
                    mLoadMoreWrapper = new LoadMoreWrapper(mEmptyWrapper);
                    isHasEmpty = true;
                    isHasLoadMore = true;
                    break;
                case HEADER_LOADING_VIEW:
                    mEmptyWrapper = new EmptyWrapper(this);
                    mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mEmptyWrapper);
                    mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
                    isHasEmpty = true;
                    isHasHeaderOrFooter = true;
                    isHasLoadMore = true;
                    break;
            }
        } else {
            throwStatusException("ItemViewDelegate at lease have one!");
        }
    }

    /**
     * 代替recyclerview.setAdapter(targetAdapter);
     *
     * @param recyclerView
     */
    public void attachRecyclerView(RecyclerView recyclerView) {
        if (isSwipeWrapperEnable()) {
            recyclerView.setAdapter(mSwipeAdapterWrapper);
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                recyclerView.setAdapter(mEmptyWrapper);
                break;
            case HEADER_FOOTER_VIEW:
                recyclerView.setAdapter(mHeaderAndFooterWrapper);
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                recyclerView.setAdapter(mLoadMoreWrapper);
                break;
        }
    }

    /**
     * 需要重写的方法
     * addItemViewDelegate(...)
     */
    protected abstract int addCustomItemViewDelegate(QuickFuncAdapter adapter);

    protected void convert(ViewHolder holder, T t, int position) {
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }


    /**
     * -----------------
     * SWIPE ENABLE
     * -----------------
     */
    public boolean isSwipeWrapperEnable() {
        return mSwipeAdapterWrapper != null && isSwipeEnable;
    }

    public SwipeAdapterWrapper enableItemSwipeFun(boolean enable, SwipeMenuCreator swipeMenuCreator,
                                                  SwipeMenuItemClickListener swipeMenuItemClickListener) {
        isSwipeEnable = enable;
        if (isSwipeEnable) {
            mSwipeAdapterWrapper = new SwipeAdapterWrapper(getEnableWrapper(), recyclerview);
            mSwipeAdapterWrapper.setSwipeMenuCreator(swipeMenuCreator);
            mSwipeAdapterWrapper.setSwipeMenuItemClickListener(swipeMenuItemClickListener);
        } else {
            mSwipeAdapterWrapper = null;
        }
        attachRecyclerView(recyclerview);
        return mSwipeAdapterWrapper;
    }

    private RecyclerView.Adapter getEnableWrapper() {
        if (isLoadMoreWrapperEnable()) {
            return mLoadMoreWrapper;
        } else if (isHeadFootWrapperEnable()) {
            return mHeaderAndFooterWrapper;
        } else if (isEmptyWrapperEnable()) {
            return mEmptyWrapper;
        }
        return this;
    }

    /**
     * -----------------------
     * EMPTY PART
     * -----------------------
     */
    // TODO: 2018/3/15 空布局设置
    public boolean isEmptyWrapperEnable() {
        return mEmptyWrapper != null && isHasEmpty;
    }

    public void setOnEmptyClickListener(View.OnClickListener mEmptyClickListener) {
        if (isEmptyWrapperEnable())
            mEmptyWrapper.setOnEmptyClickListener(mEmptyClickListener);
    }

    public void resetEmptyDefault() {
        if(isEmptyWrapperEnable()){
            mEmptyWrapper.resetEmptyDefault();
        }
    }

    public void resetEmptyRetry() {
        if(isEmptyWrapperEnable()){
            mEmptyWrapper.resetEmptyRetry();
        }
    }

    public void resetEmptyLoading() {
        if(isEmptyWrapperEnable()){
            mEmptyWrapper.resetEmptyLoading();
        }
    }

    /**
     * -----------------------
     * HEADFOOT PART
     * -----------------------
     */
    // TODO: 2018/3/15 头脚布局设置
    public boolean isHeadFootWrapperEnable() {
        return mHeaderAndFooterWrapper != null && isHasHeaderOrFooter;
    }

    public void addHeaderView(View view) {
        if (isHeadFootWrapperEnable()) {
            mHeaderAndFooterWrapper.addHeaderView(view);
        }
    }

    public void addFootView(View view) {
        if (isHeadFootWrapperEnable()) {
            mHeaderAndFooterWrapper.addFootView(view);
        }
    }

    public void removeAllHeaderFooter() {
        if (isHeadFootWrapperEnable()) {
            mHeaderAndFooterWrapper.removeAllHeaderFooter();
        }
    }

    /**
     * -----------------------
     * LOAD MORE PART
     * -----------------------
     */
    // TODO: 2018/3/15 加载更多布局设置
    public boolean isLoadMoreWrapperEnable() {
        return mLoadMoreWrapper != null && isHasLoadMore;
    }

    public void setOnLoadMoreListener(LoadMoreWrapper.OnLoadMoreCallbackListener requestLoadMoreListener) {
        if (isLoadMoreWrapperEnable()) {
            mLoadMoreWrapper.setOnLoadMoreListener(requestLoadMoreListener);
        }
    }

    /**
     * 这个方法是用来检查是否满一屏的，所以只推荐在 setdata之后使用
     * 原理很简单，先关闭 load more，检查完了再决定是否开启
     *
     * @param recyclerView
     */
    public void disableLoadMoreIfNotFullPage(RecyclerView recyclerView) {
        if (!isLoadMoreWrapperEnable()) return;
        mLoadMoreWrapper.setHideLoadMore(true);
        if (recyclerView == null) return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;
        if (manager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) != getItemCount()) {
                        mLoadMoreWrapper.setHideLoadMore(false);
                    }
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    int pos = getTheBiggestNumber(positions) + 1;
                    if (pos != getItemCount()) {
                        mLoadMoreWrapper.setHideLoadMore(false);
                    }
                }
            }, 50);
        }
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    public void loadMoreSuccess() {
        if (!isLoadMoreWrapperEnable()) return;
        mLoadMoreWrapper.resetLoadState2Success();
    }

    public void loadMoreFail() {
        if (!isLoadMoreWrapperEnable()) return;
        mLoadMoreWrapper.resetLoadState2Fail();
    }

    public void loadMoreComplete() {
        if (!isLoadMoreWrapperEnable()) return;
        mLoadMoreWrapper.resetLoadState2Complete();
    }

    public void loadMoreReset() {
        if (!isLoadMoreWrapperEnable()) return;
        mLoadMoreWrapper.resetLoadState();
    }

    public void loadMoreStop() {
        if (!isLoadMoreWrapperEnable()) return;
        mLoadMoreWrapper.stopLoadState();
    }


    // TODO: 2018/3/16 function
    private void throwStatusException(String msg) {
        if (isDebug) {
            throw new IllegalStateException(msg);
        } else {
            LogUtils.e(msg);
        }
    }

    /**
     * -----------------
     * notify 刷新方法;
     * -----------------
     */
    public void notifyDataSetChangedWrapper() {
        if (isSwipeWrapperEnable()) {
            mSwipeAdapterWrapper.notifyDataSetChanged();
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                mEmptyWrapper.notifyDataSetChanged();
                break;
            case HEADER_FOOTER_VIEW:
                mHeaderAndFooterWrapper.notifyDataSetChanged();
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                if (isEmptyWrapperEnable() && !mLoadMoreWrapper.ismLoading()) {
                    mLoadMoreWrapper.setHideLoadMore(mDatas.isEmpty());
                }
                mLoadMoreWrapper.notifyDataSetChanged();
                break;
        }
    }

    public void notifyItemInsertedWrapper(int position) {
        if (isSwipeWrapperEnable()) {
            mSwipeAdapterWrapper.notifyDataSetChanged();
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                mEmptyWrapper.notifyItemInserted(position);
                break;
            case HEADER_FOOTER_VIEW:
                mHeaderAndFooterWrapper.notifyItemInsertedWrapper(position);
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                mLoadMoreWrapper.notifyItemInserted(position);
                break;
        }
    }

    public void notifyItemRemovedWrapper(int position) {
        if (isSwipeWrapperEnable()) {
            mSwipeAdapterWrapper.notifyDataSetChanged();
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                mEmptyWrapper.notifyItemRemoved(position);
                break;
            case HEADER_FOOTER_VIEW:
                mHeaderAndFooterWrapper.notifyItemRemovedWrapper(position);
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                mLoadMoreWrapper.notifyItemRemoved(position);
                break;
        }
    }

    public void notifyItemChangedWrapper(int position) {
        notifyItemRangeChangedWrapper(position, 1);
    }

    public void notifyItemRangeRemovedWrapper(int positionStart, int itemCount) {
        if (isSwipeWrapperEnable()) {
            mSwipeAdapterWrapper.notifyDataSetChanged();
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                mEmptyWrapper.notifyItemRangeRemoved(positionStart, itemCount);
                break;
            case HEADER_FOOTER_VIEW:
                mHeaderAndFooterWrapper.notifyItemRangeRemovedWrapper(positionStart, itemCount);
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                mLoadMoreWrapper.notifyItemRangeRemoved(positionStart, itemCount);
                break;
        }
    }

    public void notifyItemRangeChangedWrapper(int positionStart, int itemCount) {
        if (isSwipeWrapperEnable()) {
            mSwipeAdapterWrapper.notifyDataSetChanged();
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                mEmptyWrapper.notifyItemRangeChanged(positionStart, itemCount);
                break;
            case HEADER_FOOTER_VIEW:
                mHeaderAndFooterWrapper.notifyItemRangeChangedWrapper(positionStart, itemCount);
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                mLoadMoreWrapper.notifyItemRangeChanged(positionStart, itemCount);
                break;
        }
    }

    public void notifyItemRangeInsertedWrapper(int positionStart, int itemCount) {
        if (isSwipeWrapperEnable()) {
            mSwipeAdapterWrapper.notifyDataSetChanged();
            return;
        }
        switch (adapterStatus) {
            case EMPTY_VIEW:
                mEmptyWrapper.notifyItemRangeInserted(positionStart, itemCount);
                break;
            case HEADER_FOOTER_VIEW:
                mHeaderAndFooterWrapper.notifyItemRangeInsertedWrapper(positionStart, itemCount);
                break;
            case LOADING_VIEW:
            case HEADER_LOADING_VIEW:
                if (isEmptyWrapperEnable()) {
                    mLoadMoreWrapper.setHideLoadMore(mDatas.isEmpty());
                }
                mLoadMoreWrapper.notifyItemRangeInserted(positionStart, itemCount);
                break;
        }
    }

    /**
     * ------------------
     * 处理数据方法
     * ------------------
     */
    @Override
    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChangedWrapper();
    }

    @Override
    public void clearDatas() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChangedWrapper();
        }
    }

    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        mDatas.add(position, data);
        notifyItemInsertedWrapper(position);
        compatibilityDataSizeChanged(1);
    }

    public void addData(@NonNull T data) {
        mDatas.add(data);
        notifyItemInsertedWrapper(mDatas.size());
        compatibilityDataSizeChanged(1);
    }

    public void remove(@IntRange(from = 0) int position) {
        mDatas.remove(position);
        notifyItemRemovedWrapper(position);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChangedWrapper(position, mDatas.size());
    }

    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        mDatas.set(index, data);
        notifyItemRangeChangedWrapper(index, 1);
    }

    public void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends T> newData) {
        mDatas.addAll(position, newData);
        notifyItemRangeInsertedWrapper(position, newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    public void addData(@NonNull Collection<? extends T> newData) {
        mDatas.addAll(newData);
        notifyItemRangeInsertedWrapper(mDatas.size() - newData.size(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * use data to replace all item in mData. this method is different {@link #( List )},
     * it doesn't change the mData reference
     *
     * @param data data collection
     */
    public void replaceData(@NonNull Collection<? extends T> data) {
        // 不是同一个引用才清空列表
        if (data != mDatas) {
            mDatas.clear();
            mDatas.addAll(data);
        }
        notifyDataSetChangedWrapper();
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mDatas == null ? 0 : mDatas.size();
        if (dataSize == size) {
            notifyDataSetChangedWrapper();
        }
    }


    @Nullable
    public View getViewByPosition(int position, @IdRes int viewId) {
        return getViewByPosition(recyclerview, position, viewId);
    }

    @Nullable
    public View getViewByPosition(RecyclerView recyclerView, int position, @IdRes int viewId) {
        if (recyclerView == null) {
            return null;
        }
        ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.getView(viewId);
    }

    /**
     * ---------------
     * 处理收缩/展开项;
     * ---------------
     */
    private boolean hasSubItems(IExpandable item) {
        if (item == null) {
            return false;
        }
        List list = item.getSubItems();
        return list != null && list.size() > 0;
    }

    public boolean isExpandable(T item) {
        return item != null && item instanceof IExpandable;
    }

    private IExpandable getExpandableItem(int position) {
        T item = getItemData(position);
        if (isExpandable(item)) {
            return (IExpandable) item;
        } else {
            return null;
        }
    }

    private int getItemPosition(T item) {
        return item != null && mDatas != null && !mDatas.isEmpty() ? mDatas.indexOf(item) : -1;
    }

    private int recursiveExpand(int position, @NonNull List list) {
        int count = list.size();
        int pos = position + list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--, pos--) {
            if (list.get(i) instanceof IExpandable) {
                IExpandable item = (IExpandable) list.get(i);
                if (item.isExpanded() && hasSubItems(item)) {
                    List subList = item.getSubItems();
                    mDatas.addAll(pos + 1, subList);
                    int subItemCount = recursiveExpand(pos + 1, subList);
                    count += subItemCount;
                }
            }
        }
        return count;
    }

    private int recursiveCollapse(@IntRange(from = 0) int position) {
        T item = getItemData(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = 0;
        if (expandable.isExpanded()) {
            List<T> subItems = expandable.getSubItems();
            if (null == subItems) return 0;

            for (int i = subItems.size() - 1; i >= 0; i--) {
                T subItem = subItems.get(i);
                int pos = getItemPosition(subItem);
                if (pos < 0) {
                    continue;
                }
                if (subItem instanceof IExpandable) {
                    subItemCount += recursiveCollapse(pos);
                }
                mDatas.remove(pos);
                subItemCount++;
            }
        }
        return subItemCount;
    }

    public int expand(@IntRange(from = 0) int position) {
        return expand(position, true, true);
    }

    public int expand(@IntRange(from = 0) int position, boolean animate, boolean shouldNotify) {
        int parentPos = position;
        int headersCount = 0;
        if (isHasHeaderOrFooter) {
            headersCount = mHeaderAndFooterWrapper.getHeadersCount();
        }
        position -= headersCount;

        T itemData = getItemData(position);
        if (!isExpandable(itemData)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) itemData;

        if (!hasSubItems(expandable)) {
            expandable.setExpanded(true);
            notifyItemChangedWrapper(parentPos);
            return 0;
        }
        int subItemCount = 0;
        if (!expandable.isExpanded()) {
            List list = expandable.getSubItems();
            mDatas.addAll(position + 1, list);
            subItemCount += recursiveExpand(position + 1, list);

            expandable.setExpanded(true);
        }
        if (shouldNotify) {
            if (animate) {
                notifyItemChangedWrapper(parentPos);
                notifyItemRangeInsertedWrapper(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChangedWrapper();
            }
        }
        return subItemCount;
    }

    public int collapse(@IntRange(from = 0) int position, boolean animate, boolean notify) {
        int parentPos = position;
        int headersCount = 0;
        if (isHasHeaderOrFooter) {
            headersCount = mHeaderAndFooterWrapper.getHeadersCount();
        }
        position -= headersCount;

        T itemData = getItemData(position);
        if (!isExpandable(itemData)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) itemData;
        int subItemCount = recursiveCollapse(position);
        expandable.setExpanded(false);
        if (notify) {
            if (animate) {
                notifyItemChangedWrapper(parentPos);
                notifyItemRangeRemovedWrapper(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChangedWrapper();
            }
        }
        return subItemCount;
    }

    /**
     * Get the parent item position of the IExpandable item
     *
     * @return return the closest parent item position of the IExpandable.
     * if the IExpandable item's level is 0, return itself position.
     * if the item's level is negative which mean do not implement this, return a negative
     * if the item is not exist in the data list, return a negative.
     */
    public int getParentPosition(@NonNull T item) {
        int position = getItemPosition(item);
        if (position == -1) {
            return -1;
        }

        // if the item is IExpandable, return a closest IExpandable item position whose level smaller than this.
        // if it is not, return the closest IExpandable item position whose level is not negative
        int level;
        if (item instanceof IExpandable) {
            level = ((IExpandable) item).getLevel();
        } else {
            level = Integer.MAX_VALUE;
        }
        if (level == 0) {
            return position;
        } else if (level == -1) {
            return -1;
        }

        for (int i = position; i >= 0; i--) {
            T temp = mDatas.get(i);
            if (temp instanceof IExpandable) {
                IExpandable expandable = (IExpandable) temp;
                if (expandable.getLevel() >= 0 && expandable.getLevel() < level) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void isUseEmpty(boolean isUseEmpty){
        if(isEmptyWrapperEnable()){
            mEmptyWrapper.isUseEmpty(isUseEmpty);
        }
    }
}
