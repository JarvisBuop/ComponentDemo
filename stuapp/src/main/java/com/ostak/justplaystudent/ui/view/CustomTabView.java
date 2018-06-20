package com.ostak.justplaystudent.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ostak.justplaystudent.R;
import com.ostak.justplaystudent.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/6/2.
 *
 * @Description:
 * @see:
 */

public class CustomTabView extends LinearLayout {

    private static final int MODE_TAB_LOG = 0;//外部的tab
    private static final int MODE_TAB_INNER = 1;//内部的tab;
    @BindView(R2.id.radio_left)
    RadioButton radioLeft;
    @BindView(R2.id.radio_right)
    RadioButton radioRight;
    @BindView(R2.id.radio_middle)
    RadioButton radioMiddle;
    @BindView(R2.id.layout_radio_bg)
    RadioGroup layoutRadioBg;

    int mode = MODE_TAB_LOG;

    RadioButton currentBtn = null;

    public CustomTabView(Context context) {
        this(context, null);
    }

    public CustomTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_tab_button, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTabView);

        String stringLeft = typedArray.getString(R.styleable.CustomTabView_log_txt_left);
        String stringRight = typedArray.getString(R.styleable.CustomTabView_log_txt_right);
        String stringMiddle = typedArray.getString(R.styleable.CustomTabView_log_txt_middle);
        mode = typedArray.getInt(R.styleable.CustomTabView_log_tab_bg, MODE_TAB_LOG);

        if (!TextUtils.isEmpty(stringLeft))
            radioLeft.setText(stringLeft);
        if (!TextUtils.isEmpty(stringRight))
            radioRight.setText(stringRight);
        if (!TextUtils.isEmpty(stringMiddle)) {
            radioMiddle.setVisibility(VISIBLE);
            radioMiddle.setText(stringMiddle);
        } else {
            radioMiddle.setVisibility(GONE);
        }
        initView();

        typedArray.recycle();


    }

    private void initView() {
        switch (mode) {
            case MODE_TAB_LOG:
                radioLeft.setBackgroundResource(R.drawable.bg_checkbox_tab);
                radioMiddle.setBackgroundResource(R.drawable.bg_checkbox_tab);
                radioRight.setBackgroundResource(R.drawable.bg_checkbox_tab);
                break;
            case MODE_TAB_INNER:
                radioLeft.setBackgroundResource(R.drawable.bg_checkbox_tab_inner);
                radioRight.setBackgroundResource(R.drawable.bg_checkbox_tab_inner);
                radioMiddle.setBackgroundResource(R.drawable.bg_checkbox_tab_inner);
                break;
        }
    }

    public void setonCheckChangeListener(RadioGroup.OnCheckedChangeListener mListener) {
        layoutRadioBg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton viewById = group.findViewById(checkedId);

                switch (mode) {
                    case MODE_TAB_LOG:
                        if (currentBtn != null) {
                            currentBtn.setTextColor(getContext().getResources().getColor(R.color.white));
                        }
                        viewById.setTextColor(getContext().getResources().getColor(R.color.color_main));
                        break;
                    case MODE_TAB_INNER:
                        if (currentBtn != null) {
                            currentBtn.setTextColor(getContext().getResources().getColor(R.color.white));
                        }
                        viewById.setTextColor(getContext().getResources().getColor(R.color.color_main));
                        break;
                }

                if (mListener != null) {
                    mListener.onCheckedChanged(group, checkedId);
                }

                currentBtn = viewById;
            }
        });
    }
}
