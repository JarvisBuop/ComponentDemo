package com.ostak.justplayteacher.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.ostak.justplayteacher.R;
import com.ostak.justplayteacher.util.JustPlayUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public class CustomSpannable extends LinearLayout {
    @BindView(R.id.txt_span)
    TextView txtSpan;
    @BindView(R.id.img_span)
    ImageView imgSpan;
    @BindView(R.id.layout_span)
    LinearLayout layoutSpan;

    PopupWindow popupWindow = null;
    private RecyclerView mRecycler;
    private CommonAdapter mInnerAdapter;

    ArrayList mDataList = new ArrayList() {
        {
            add("111");
            add("222");
            add("333");
            add("444");
            add("555");
            add("666");
        }
    };
    Object current = null;

    public CustomSpannable(Context context) {
        this(context, null);
    }

    public CustomSpannable(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSpannable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.include_spanable, this, true);
        ButterKnife.bind(this, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initPop();

        layoutSpan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void initPop() {
        mInnerAdapter = new CommonAdapter(getContext(), R.layout.item_spannable_txt, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                if (o instanceof String) {
                    String str = (String) o;
                    holder.setText(R.id.txt_span_item, str);
                    holder.setOnClickListener(R.id.layout_txt_item, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            current = str;
                            txtSpan.setText(str);
                            if (popupWindow != null) {
                                popupWindow.dismiss();
                            }
                        }
                    });
                }
            }
        };

        mRecycler = new RecyclerView(getContext());
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mInnerAdapter);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRecycler.setLayoutParams(layoutParams);
    }


    public void setDataList(ArrayList mDataList) {
        if (JustPlayUtils.isListNotNull(mDataList)) {
            this.mDataList.addAll(mDataList);
        }
        mInnerAdapter.notifyDataSetChanged();
    }


    @SuppressLint("ObjectAnimatorBinding")
    public void toggle() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            ObjectAnimator.ofInt(imgSpan, "rotationY", 0, -180).start();
        } else {
            showPop();

            ObjectAnimator.ofInt(imgSpan, "rotationY", 0, 180).start();
        }
    }

    private void showPop() {
        popupWindow = new PopupWindow(mRecycler, txtSpan.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        popupWindow.showAsDropDown(txtSpan);
    }

}
