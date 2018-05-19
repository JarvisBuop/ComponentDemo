package com.jarvisdong.uikit.adapter.wrapper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.jarvisdong.uikit.adapter.utils.WrapperUtils;


/**
 * 装饰类-空布局
 */
public class EmptyWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter mInnerAdapter;
    private View mEmptyView;
    private int mEmptyLayoutId = R.layout.recyclerview_empty;
    private boolean mIsUseEmpty = true;

    public static final int EMPTY_DEFAULT = 0;//暂无数据;
    public static final int EMPTY_RETRY = 1;//点击重试;
    public static final int EMPTY_LOADING = 2;//加载中;
    private int mCurrentLoadingStatus = EMPTY_RETRY;
    private boolean isCustomFoot = false;//是否是自定义;
    private Context mContext;

    public void isUseEmpty(boolean mIsUseEmpty) {
        this.mIsUseEmpty = mIsUseEmpty;
    }

    public EmptyWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.getItemCount() == 0 && mIsUseEmpty;
    }

    public boolean isOriginDataEmpty() {
        return mInnerAdapter.getItemCount() == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (isEmpty()) {
            ViewHolder holder;
            if (mEmptyView == null) {
                mEmptyView = LayoutInflater.from(parent.getContext()).inflate(mEmptyLayoutId, parent, false);
                ViewGroup.LayoutParams layoutParams = mEmptyView.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mEmptyView.setLayoutParams(layoutParams);
            }
            holder = ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);

        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isEmpty()) {
                    return gridLayoutManager.getSpanCount();
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
        if (isEmpty()) {
            WrapperUtils.setFullSpan(holder);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isEmpty()) {
            setDefaultLoadingStatus(mCurrentLoadingStatus);
            mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emptyClickListener != null && mCurrentLoadingStatus == EMPTY_RETRY) {
                        emptyClickListener.onClick(v);
                    }else if(mCurrentLoadingStatus == EMPTY_DEFAULT){
                        setDefaultLoadingStatus(mCurrentLoadingStatus);
                        return;
                    }
                    mCurrentLoadingStatus = EMPTY_LOADING;
                    setDefaultLoadingStatus(mCurrentLoadingStatus);
                }
            });
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) return 1;
        return mInnerAdapter.getItemCount();
    }


    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        isCustomFoot = true;
    }

    public void setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
        isCustomFoot = true;
    }

    public void setDefaultLoadingStatus(int mCurrentLoadingStatus) {
        this.mCurrentLoadingStatus = mCurrentLoadingStatus;
        if (mEmptyLayoutId == R.layout.recyclerview_empty && mEmptyView != null && !isCustomFoot) {
            ImageView imgEmpty = (ImageView) mEmptyView.findViewById(R.id.img_empty);
            ProgressBar progressBar = (ProgressBar) mEmptyView.findViewById(R.id.progressBar);
            TextView txtRetry = (TextView) mEmptyView.findViewById(R.id.text_retry);
            switch (this.mCurrentLoadingStatus) {
                case EMPTY_DEFAULT:
                    txtRetry.setText(mContext.getString(R.string.loading_nodata));
                    progressBar.setVisibility(View.GONE);
                    break;
                case EMPTY_LOADING:
                    txtRetry.setText(mContext.getString(R.string.loading));
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case EMPTY_RETRY:
                    txtRetry.setText(mContext.getString(R.string.loading_retry));
                    progressBar.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }

    public void resetEmptyDefault() {
        mIsUseEmpty = true;
        mCurrentLoadingStatus = EMPTY_DEFAULT;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }

    public void resetEmptyRetry() {
        mIsUseEmpty = true;
        mCurrentLoadingStatus = EMPTY_RETRY;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }

    public void resetEmptyLoading() {
        mIsUseEmpty = true;
        mCurrentLoadingStatus = EMPTY_LOADING;
        setDefaultLoadingStatus(mCurrentLoadingStatus);
    }


    private View.OnClickListener emptyClickListener;

    public void setOnEmptyClickListener(View.OnClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
    }

}
