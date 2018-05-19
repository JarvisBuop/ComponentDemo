package com.jarvisdong.uikit.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.LogUtils;
import com.jarvisdong.uikit.adapter.animation.AlphaInAnimation;
import com.jarvisdong.uikit.adapter.animation.BaseAnimation;
import com.jarvisdong.uikit.adapter.animation.ScaleInAnimation;
import com.jarvisdong.uikit.adapter.animation.SlideInBottomAnimation;
import com.jarvisdong.uikit.adapter.animation.SlideInLeftAnimation;
import com.jarvisdong.uikit.adapter.animation.SlideInRightAnimation;
import com.jarvisdong.uikit.adapter.impl.IEventListener;
import com.jarvisdong.uikit.adapter.impl.OnConvertViewClickListener;
import com.jarvisdong.uikit.adapter.itemanager.ItemViewDelegate;
import com.jarvisdong.uikit.adapter.itemanager.ItemViewDelegateManager;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;

/**
 * 多功能适配器;
 *
 * @param <T>
 */
public class MultiItemTypeAdapter<T,K extends ViewHolder> extends RecyclerView.Adapter<K> {
    protected boolean isDebug = true;
    protected Context mContext;
    protected List<T> mDatas;

    protected ItemViewDelegateManager mItemViewDelegateManager;
    protected OnConvertViewClickListener mOnConvertViewClickListener;
    protected IEventListener<T> mChildViewClickListener;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }


    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return (K) holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    //可屏蔽某一类的点击事件;
    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConvertViewClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnConvertViewClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnConvertViewClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnConvertViewClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
        if (mChildViewClickListener != null) {
            viewHolder.setOnChildEventListener(mChildViewClickListener);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * ------------------------------
     * 多类型适配器的添加itemtype代理类;
     * ------------------------------
     */
    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    /**
     * ------------------------------
     * 事件-convertview的点击和子view的点击;
     * -------------------------------
     */
    public void setOnConvertViewClickListener(OnConvertViewClickListener onItemClickListener) {
        this.mOnConvertViewClickListener = onItemClickListener;
    }

    public void setOnChildEventListener(IEventListener mChildViewClickListener) {
        this.mChildViewClickListener = mChildViewClickListener;
    }

    /**
     * ---------
     * 适配器操作;
     * ---------
     */
    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void clearDatas() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public T getItemData(int pos) {
        if (mDatas != null && mDatas.size() > pos) {
            return mDatas.get(pos);
        } else {
            return null;
        }
    }

    /**
     * ---------------
     * 显示和收缩效果;
     * ---------------
     *
     * @param position   刷新的范围开始位置 [positon+1]
     * @param collection 添加或删除的数据;
     */
    public void addExpandAll(int position, Collection collection) {
        if (isDebug)
            LogUtils.e("pos-add" + position);
        if (isEffectDatas(position) && mDatas.addAll(position, collection)) {
            if (position - 1 >= 0)
                notifyItemChanged(position - 1);
            notifyItemRangeInserted(position, collection.size());
        }
    }

    public void removeExpandAll(int position, Collection collection) {
        if (isDebug)
            LogUtils.e("pos-remove" + position);
        if (isEffectDatas(position) && mDatas.removeAll(collection)) {
            if (position - 1 >= 0)
                notifyItemChanged(position - 1);
            notifyItemRangeRemoved(position, collection.size());
        }
    }

    private boolean isEffectDatas(int position) {
        return mDatas != null && position < mDatas.size();
    }

    /**
     * ---------------
     * 添加item动画
     * ---------------
     */
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    private boolean mFirstOnlyEnable = true;//是否只显示一次;
    private boolean mOpenAnimationEnable = false; //是否打开item动画效果
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    private int mLastPosition = -1;

    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow((K) holder);
        addAnimation(holder);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setInterpolator(mInterpolator);
        anim.setDuration(mDuration).start();
    }

    /**
     * Set the view animation type.
     *
     * @param animationType One of {@link #ALPHAIN}, {@link #SCALEIN}, {@link #SLIDEIN_BOTTOM}, {@link #SLIDEIN_LEFT}, {@link #SLIDEIN_RIGHT}.
     */
    public void openLoadAnimation(@MultiItemTypeAdapter.AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    /**
     * {@link #addAnimation(RecyclerView.ViewHolder)}
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }
}
