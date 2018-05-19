/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
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

package com.jarvisdong.uikit.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarvisdong.uikit.R;
import com.jarvisdong.uikit.badge.Badge;
import com.jarvisdong.uikit.badge.QBadgeView;
import com.jarvisdong.uikit.reminder.ReminderItem;
import com.jarvisdong.uikit.util.ScreenUtils;

import java.util.Locale;

import static com.jarvisdong.uikit.util.ScreenUtils.dp2px;


public class PagerSlidingTabStrip extends HorizontalScrollView implements OnPageChangeListener {
    private static final String TAG = PagerSlidingTabStrip.class.getSimpleName();
    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    private final Context mContext;
    // @formatter:on
    private LinearLayout.LayoutParams scrollableTabLayoutParams;
    private LinearLayout.LayoutParams fixTabLayoutParams;

    //target
    private ViewPager pager;
    //tab scrollerView中的根布局容器;
    private LinearLayout tabsContainer;
    private int tabCount;
    //当前选中,和当前偏移;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    //底部条和相隔线画笔;
    private Paint rectPaint;
    private Paint dividerPaint;
    //style;
    private int underlineColor = getResources().getColor(R.color.line_ccc);
    private int underlineHeight = 2;
    private int indicatorColor = getResources().getColor(R.color.color_blue_0888ff);
    private int indicatorHeight = 5;
    private int dividerColor = getResources().getColor(R.color.line_a7);
    private int dividerWidth = 1;
    private int dividerPadding = 12;
    private int checkedTextColor = getResources().getColor(R.color.color_blue_0888ff);
    private int unCheckedTextColor = getResources().getColor(R.color.color_grey_888888);

    private boolean tabMode = false;//scrollable or fixed; true:fixed;
    private boolean textAllCaps = true;//字母大写;
    private int tabPadding = 24;

    private int tabTextSize = 14;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int scrollOffset = 52;
    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.background_tab;

    private Locale locale;

    private OnTabClickListener onTabClickListener = null;

    private OnTabDoubleTapListener onTabDoubleTapListener = null;

    private OnDragStateChangedListener onDragStateChangedListener = null;

    private OnCustomTabListener onCustomTabListener = null;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setFillViewport(true);
        setWillNotDraw(false);

        //scrollerview中的第一个view;
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        //初始化设置;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        unCheckedTextColor = a.getColor(1, unCheckedTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        checkedTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsTextColor, checkedTextColor);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        tabMode = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTabMode, tabMode);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        scrollableTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        fixTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogUtils.e(TAG, "pos:" + position + "/offset:" + positionOffset + "/pixels:" + positionOffsetPixels);
        currentPosition = position;
        currentPositionOffset = positionOffset;

        scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        setChooseTabViewTextColor(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            scrollToChild(pager.getCurrentItem(), 0);
        }

    }

    /**
     * @param pager
     */
    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        checkAdapterNotNull(true);

        pager.addOnPageChangeListener(this);

        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (pager == null || pager.getAdapter() == null || pager.getAdapter().getCount() == 0) {
            return 0;
        } else {
            tabCount = pager.getAdapter().getCount();
            return pager.getAdapter().getCount();
        }
    }

    private boolean checkAdapterNotNull(boolean isException) {
        if (pager == null || pager.getAdapter() == null) {
            if (!isException) {
                return false;
            } else {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
        } else {
            return true;
        }
    }

    /**
     * 先去除所有的tab,在添加,滑动;
     */
    public void notifyDataSetChanged() {
        checkAdapterNotNull(true);

        tabsContainer.removeAllViews();

        for (int i = 0; i < getItemCount(); i++) {
            addTabView(i, pager.getAdapter().getPageTitle(i).toString());
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                setChooseTabViewTextColor(currentPosition);
                scrollToChild(currentPosition, 0);
            }
        });
    }

    private void setChooseTabViewTextColor(int position) {
        int childCount = tabsContainer.getChildCount();
        ViewGroup tabView;
        TextView textView;
        for (int i = 0; i < childCount; ++i) {
            tabView = (ViewGroup) tabsContainer.getChildAt(i);
            textView = (TextView) tabView.findViewById(R.id.tab_title_label);
            if (i == position) {
                textView.setTextColor(checkedTextColor);
            } else {
                textView.setTextColor(unCheckedTextColor);
            }
        }
    }

    private void addTabView(int position, String title) {
        View tabView = null;
        boolean screenAdaptation = false;
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        if (this.onCustomTabListener != null) {
            int tabResId = onCustomTabListener.getTabLayoutResId(position);
            if (tabResId != 0) {
                tabView = inflater.inflate(tabResId, null);
            } else {
                tabView = onCustomTabListener.getTabLayoutView(inflater, position);
            }
            screenAdaptation = onCustomTabListener.screenAdaptation();
        }
        if (tabView == null) {
            tabView = inflater.inflate(R.layout.tab_layout_main_badge, null);
        }
        TextView titleTV = ((TextView) tabView.findViewById(R.id.tab_title_label));
        final boolean needAdaptation = ScreenUtils.density <= 1.5 && screenAdaptation;
        final Resources resources = getContext().getResources();
        if (titleTV != null) {
            titleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, needAdaptation ?
                    resources.getDimensionPixelSize(R.dimen.dimen_size_11sp) :
                    resources.getDimensionPixelSize(R.dimen.dimen_size_15sp));
            titleTV.setText(title);
        }
        addTab(position, tabView);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == position && onTabClickListener != null) {
                    onTabClickListener.onCurrentTabClicked(position);
                } else {
                    pager.setCurrentItem(position);
                }
            }
        });
        addTabDoubleTapListener(position, tab);
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, tabMode ? fixTabLayoutParams : scrollableTabLayoutParams);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);

            updataInnerView(v);
        }
    }

    private void updataInnerView(View v) {
        TextView titleTV = ((TextView) v.findViewById(R.id.tab_title_label));
        if (titleTV != null) {
            titleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
            titleTV.setTypeface(tabTypeface, tabTypefaceStyle);

            // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
            if (textAllCaps) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    titleTV.setAllCaps(true);
                } else {
                    titleTV.setText(titleTV.getText().toString().toUpperCase(locale));
                }
            }
        }
    }

    public void updateTab(final int index, final ReminderItem item, boolean isUseBadge) {
        ViewGroup tabView = (ViewGroup) tabsContainer.getChildAt(index);
        ImageView indicatorView = (ImageView) tabView.findViewById(R.id.tab_title_indicator);
        TextView titleView = ((TextView) tabView.findViewById(R.id.tab_title_label));

        if (item == null || titleView == null || indicatorView == null) {
            indicatorView.setVisibility(View.GONE);
            return;
        }
        if (item.indicator()) {
            if (!isUseBadge) {
                indicatorView.setVisibility(item.getUnread() > 0 ? View.VISIBLE : View.GONE);
            } else {
                Badge badge = null;
                if (titleView.getTag() != null) {
                    badge = (Badge) titleView.getTag();
                } else {
                    badge = new QBadgeView(mContext).bindTarget(tabView);
                    badge.setBadgeGravity(Gravity.TOP | Gravity.END);
                    badge.setBadgeTextSize(9, true);
                    badge.setBadgePadding(4, true);
                    titleView.setTag(badge);
                }
                float offsetX = (tabView.getMeasuredWidth() - titleView.getMeasuredWidth()) / 2 - dp2px(mContext, 12);
                float offsetY = (tabView.getMeasuredHeight() - titleView.getMeasuredHeight()) / 2 - dp2px(mContext, 8);
                badge.setGravityOffset(offsetX, offsetY, false);
                badge.setBadgeNumber(item.getUnread());
                badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (onDragStateChangedListener != null) {
                            onDragStateChangedListener.onDragStateChanged(dragState, index, item);
                        }
                    }
                });
            }
        } else {
            if (!isUseBadge) {
                indicatorView.setVisibility(View.GONE);
            } else {
                if (titleView.getTag() != null) {
                    Badge badge = (Badge) titleView.getTag();
                    badge.setBadgeNumber(0);
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        newScrollX -= scrollOffset;
        if (position > 0 || offset > 0) {
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        //draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw variable indicator line
        rectPaint.setColor(indicatorColor);

        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        float indicatorLeft = lineLeft;
        float indicatorRight = lineRight;

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            if (tabMode) {
                //仿微博;
                if (currentPositionOffset <= 0.5) {
                    indicatorLeft = lineLeft;
                    indicatorRight = lineRight + (nextTabRight - lineRight) * 2 * currentPositionOffset;
                } else {
                    indicatorLeft = nextTabLeft - (nextTabLeft - lineLeft) * (1 - currentPositionOffset) * 2;
                    indicatorRight = nextTabRight;
                }
            } else {
                indicatorLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
                indicatorRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
            }
        }

        canvas.drawRect(indicatorLeft, height - indicatorHeight, indicatorRight, height, rectPaint);


        // draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }


    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public void setTabMode(boolean tabMode) {
        this.tabMode = tabMode;
        requestLayout();
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
        updateTabStyles();
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public void setUnCheckedTextColorResource(int resId) {
        this.unCheckedTextColor = getResources().getColor(resId);
        setChooseTabViewTextColor(currentPosition);
    }

    public void setCheckedTextColorResource(int resId) {
        this.checkedTextColor = getResources().getColor(resId);
        setChooseTabViewTextColor(currentPosition);
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
        updateTabStyles();
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        requestLayout();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * ****************- listener -******************
     */

    /**
     * TAB 的点击监�?
     */
    public interface OnTabClickListener {

        public void onCurrentTabClicked(int position);
    }

    /**
     * Tab 的拖拽监听;
     */
    public interface OnDragStateChangedListener {
        void onDragStateChanged(int dragStatus, int pos, final ReminderItem item);
    }

    /**
     * TAB 的双击监�?
     */
    public interface OnTabDoubleTapListener {

        public void onCurrentTabDoubleTap(int position);
    }

    /**
     * 获取每个TAB的自定义布局
     */
    public static class OnCustomTabListener {

        /**
         * �?要自定义TAB的布�?�?
         * 重写该方法，返回对应的layout id
         *
         * @param position
         * @return
         */
        public int getTabLayoutResId(int position) {
            return 0;
        }

        /**
         * �?要自定义TAB的布�?
         * 重写该方法，直接返回对应的view
         *
         * @param inflater
         * @param position
         * @return
         */
        public View getTabLayoutView(LayoutInflater inflater, int position) {
            return null;
        }

        /**
         * 是否�?要小屏幕适配,只在存在多个tab，且tab的布�?较紧时�?�配,现在只用于主界面
         *
         * @return
         */
        public boolean screenAdaptation() {
            return false;
        }


    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    public void setOnTabDoubleTapListener(OnTabDoubleTapListener onTabDoubleTapListener) {
        this.onTabDoubleTapListener = onTabDoubleTapListener;
    }

    public void setOnDragStateChangedListener(OnDragStateChangedListener onDragStateChangedListener) {
        this.onDragStateChangedListener = onDragStateChangedListener;
    }

    /**
     * must invoke before setViewPager
     *
     * @param onCustomTabListener
     */
    public void setOnCustomTabListener(OnCustomTabListener onCustomTabListener) {
        this.onCustomTabListener = onCustomTabListener;
    }

    private void addTabDoubleTapListener(final int position, View tab) {
        final GestureDetector gd = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (onTabDoubleTapListener != null)
                    onTabDoubleTapListener.onCurrentTabDoubleTap(position);

                return true;
            }
        });

        tab.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
    }
}
