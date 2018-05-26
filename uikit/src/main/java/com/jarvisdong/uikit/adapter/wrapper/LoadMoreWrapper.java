package com.jarvisdong.uikit.adapter.wrapper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.jarvisdong.uikit.adapter.utils.WrapperUtils;


/**
 * 装饰类-加载更多;
 */
public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //load status
    private boolean isCustomFoot = false;//是否是自定义;
    private boolean mLoading = false;//当前是否是加载状态;
    //    private boolean mNextLoadEnable = false;//下一页加载更多是否开启;
    private boolean isUseLoadMore = true; //是否使用加载更多
    //加载更多状态
    public static final int STATUS_LOADING = 2;//加载中;
    public static final int STATUS_FAIL = 3;//加载失败;
    public static final int STATUS_DEFAULT = 1;//全部不显示;
    public static final int STATUS_END = 4;//加载完成,显示底部布局;
    public static final int STATUS_END_COMPLETE = 5;//加载完成,显示文字;

    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId = R.layout.recyclerview_refresh_footer;
    private int mCurrentLoadingStatus = STATUS_DEFAULT;
    private RecyclerView mRecyclerView;
    private OnLoadMoreCallbackListener mOnLoadMoreListener;
    private Context mContext;
    private int mPreLoadNumber = 0;

    public void setPreLoadNumber(int preLoadNumber) {
        if (preLoadNumber > 0) {
            mPreLoadNumber = preLoadNumber;
        }
    }

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return (mLoadMoreView != null || mLoadMoreLayoutId != 0) && isUseLoadMore;
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount() - mPreLoadNumber);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView == null) {
                mLoadMoreView = LayoutInflater.from(mContext).inflate(mLoadMoreLayoutId, parent, false);
                setDefaultLoadingStatus(mCurrentLoadingStatus);
            }
            holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (!mLoading) {
                mLoading = true;
                mCurrentLoadingStatus = STATUS_LOADING;
                setDefaultLoadingStatus(mCurrentLoadingStatus);
                if (mOnLoadMoreListener != null && holder.getItemViewType() == ITEM_TYPE_LOAD_MORE) {
                    mOnLoadMoreListener.onfooterStatus(mCurrentLoadingStatus);
                }
                invokeLoadMore();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    private void invokeLoadMore() {
        if (getRecyclerView() != null) {
            getRecyclerView().post(new Runnable() {
                @Override
                public void run() {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMoreRequested();
                    }
                }
            });
        } else {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
        }
    }


    public void setDefaultLoadingStatus(int mCurrentLoadingStatus) {
        this.mCurrentLoadingStatus = mCurrentLoadingStatus;
        if (mLoadMoreLayoutId == R.layout.recyclerview_refresh_footer && mLoadMoreView != null && !isCustomFoot) {
            ViewGroup layoutLoad = (ViewGroup) mLoadMoreView.findViewById(R.id.layout_loading);
            ViewGroup llStart = (ViewGroup) mLoadMoreView.findViewById(R.id.ll_start);
            ViewGroup llEnd = (ViewGroup) mLoadMoreView.findViewById(R.id.ll_end);
            View pbView = mLoadMoreView.findViewById(R.id.pb_loading);
            TextView txtLoading = (TextView) mLoadMoreView.findViewById(R.id.tv_loading);
            layoutLoad.setVisibility(View.VISIBLE);
            txtLoading.setOnClickListener(null);
            switch (this.mCurrentLoadingStatus) {
                case STATUS_DEFAULT:
                    layoutLoad.setVisibility(View.GONE);
                    break;
                case STATUS_FAIL:
                    llStart.setVisibility(View.VISIBLE);
                    llEnd.setVisibility(View.GONE);
                    pbView.setVisibility(View.GONE);
                    txtLoading.setText(mContext.getResources().getString(R.string.loading_fail));
                    txtLoading.setTextColor(mContext.getResources().getColor(R.color.goo_red));
                    txtLoading.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setHideLoadMore(false);//此时接口的page应放在接口调成功后;
//                            notifyDataSetChanged();
                            invokeLoadMore();
                        }
                    });
                    break;
                case STATUS_LOADING:
                    llStart.setVisibility(View.VISIBLE);
                    llEnd.setVisibility(View.GONE);
                    pbView.setVisibility(View.VISIBLE);
                    txtLoading.setText(mContext.getResources().getString(R.string.loading));
                    txtLoading.setTextColor(mContext.getResources().getColor(R.color.white));
                    break;
                case STATUS_END:
                    llStart.setVisibility(View.GONE);
                    llEnd.setVisibility(View.VISIBLE);
                    break;
                case STATUS_END_COMPLETE:
                    llStart.setVisibility(View.VISIBLE);
                    llEnd.setVisibility(View.GONE);
                    pbView.setVisibility(View.GONE);
                    txtLoading.setText(mContext.getResources().getString(R.string.loading_complete));
                    txtLoading.setTextColor(mContext.getResources().getColor(R.color.goo_green));
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isShowLoadMore(holder.getLayoutPosition())) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    @Override
    public int getItemCount() {
        if(isOriginDataEmpty()){
            return mInnerAdapter.getItemCount();
        }
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    /**
     * 原始数据是否为空;不加载,显示空布局;
     * @return
     */
    private boolean isOriginDataEmpty() {
        if ((mInnerAdapter instanceof EmptyWrapper && ((EmptyWrapper)mInnerAdapter).isOriginDataEmpty())
                ||(mInnerAdapter instanceof HeaderAndFooterWrapper && ((HeaderAndFooterWrapper)mInnerAdapter).isOriginDataEmpty())) {
            return true;
        }
        return false;
    }

    /**
     * 用自定义view取代;
     *
     * @param loadMoreView
     * @return
     */
    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        isCustomFoot = true;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        isCustomFoot = true;
        return this;
    }


    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreCallbackListener loadMoreListener) {
        mOnLoadMoreListener = loadMoreListener;
        mLoading = false;
        return this;
    }

    public interface OnLoadMoreCallbackListener {
        void onLoadMoreRequested();

        void onfooterStatus(int status);
    }

    /**
     * 辅助方法
     */
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    /**
     * @return true:can not refresh;
     */
    public boolean ismLoading() {
        return mLoading;
    }

    /**
     * 调完接口需要重新设置
     * 1.加载完成,还能继续加载;STATUS_END_COMPLETE
     * 2.加载完成,到最后一页,不能继续加载;STATUS_END
     * 3.加载失败,还能继续加载;STATUS_FAIL;
     */
    public void resetLoadState2Success() {
        this.mLoading = false;
        mCurrentLoadingStatus = STATUS_END_COMPLETE;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }

    public void resetLoadState2Complete() {
        this.mLoading = true;
        mCurrentLoadingStatus = STATUS_END;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }

    public void resetLoadState2Fail() {
        this.mLoading = true;
        mCurrentLoadingStatus = STATUS_FAIL;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }

    public void resetLoadState() {
        this.mLoading = false;
        this.isUseLoadMore = true;
    }

    /**
     * 不能加载,显示当前状态;
     */
    public void stopLoadState() {
        this.mLoading = true;
    }

    /**
     * true隐藏加载布局,不使用加载更多;
     *
     * @param isHide
     */
    public void setHideLoadMore(boolean isHide) {
        this.mLoading = isHide;
        this.isUseLoadMore = !isHide;

        mCurrentLoadingStatus = isHide ? STATUS_DEFAULT : STATUS_LOADING;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }


}
