package com.jarvisdong.teaapp.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.util.JustPlayUtils;
import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/6/1.
 * OverView:
 */

public class CustomLogView extends RelativeLayout {

    private static final int MODE_TXT = 0;
    private static final int MODE_ARROW = 1;
    @BindView(R.id.img_log)
    ImageView imgLog;
    @BindView(R.id.edit_input)
    EditText editInput;
    @BindView(R.id.txt_validCode)
    TextView txtValidCode;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;
    @BindView(R.id.layout_input)
    View layout;

    int mode = MODE_TXT;

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

    public CustomLogView(Context context) {
        this(context, null);
    }

    public CustomLogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_log_func, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomLogView);

        boolean aBoolean = typedArray.getBoolean(R.styleable.CustomLogView_editFocus, true);
        String string = typedArray.getString(R.styleable.CustomLogView_editHint);
        int resourceId = typedArray.getResourceId(R.styleable.CustomLogView_left_icon, 0);
        mode = typedArray.getInt(R.styleable.CustomLogView_input_mode, MODE_TXT);
        String rightString = typedArray.getString(R.styleable.CustomLogView_right_txt);
        int rightColor = typedArray.getColor(R.styleable.CustomLogView_right_txt_color, context.getResources().getColor(R.color.color_main));

        if (editInput != null) {

            editInput.setFocusable(aBoolean);
            editInput.setFocusableInTouchMode(aBoolean);

            int anInt = typedArray.getInt(R.styleable.CustomLogView_inputType, 0);
            editInput.setInputType(anInt == 0 ? EditorInfo.TYPE_CLASS_TEXT : EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);

            editInput.setHint(string);
        }

        if (resourceId != 0 && imgLog != null) {
            imgLog.setImageResource(resourceId);
        }
        if (txtValidCode != null) {

            txtValidCode.setText(rightString);
            txtValidCode.setTextColor(rightColor);
        }
        initMode();
        typedArray.recycle();


    }

    private void initMode() {
        if (txtValidCode == null || imgArrow == null) return;
        switch (mode) {
            case MODE_TXT:
                txtValidCode.setVisibility(VISIBLE);
                imgArrow.setVisibility(GONE);
                break;
            case MODE_ARROW:
                txtValidCode.setVisibility(GONE);
                imgArrow.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initPop();
        if (mode == MODE_ARROW) {
            editInput.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle();
                }
            });
        }
    }

    private void initPop() {
        mInnerAdapter = new CommonAdapter(getContext(), R.layout.item_spannable_txt, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                if (o instanceof String) {
                    String str = (String) o;
                    holder.setText(R.id.txt_span_item, str);
                    holder.setOnClickListener(R.id.layout_txt_item, v -> {
                        current = str;
                        editInput.setText(str);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
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
        } else {
            showPop();
            ObjectAnimator.ofFloat(imgArrow, "rotationX", 0f, 180f).start();
        }
    }

    private void showPop() {
        popupWindow = new PopupWindow(mRecycler, editInput.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ObjectAnimator.ofFloat(imgArrow, "rotationX", 180f, 0f).start();
            }
        });
        popupWindow.showAsDropDown(editInput);
    }
}
