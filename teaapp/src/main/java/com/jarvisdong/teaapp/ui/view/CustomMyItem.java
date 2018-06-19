package com.jarvisdong.teaapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/29.
 *
 * @Description:
 * @see:
 */

public class CustomMyItem extends RelativeLayout {
    @BindView(R.id.img_left_icon)
    ImageView imgLeftIcon;
    @BindView(R.id.txt_middle_content)
    TextView txtMiddleContent;
    @BindView(R.id.img_right_icon)
    ImageView imgRightIcon;

    public CustomMyItem(Context context) {
        this(context, null);
    }

    public CustomMyItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMyItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_my_item, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomMyItem);

        String string = typedArray.getString(R.styleable.CustomMyItem_middleName);
        int color = typedArray.getColor(R.styleable.CustomMyItem_middleTxt_color, context.getResources().getColor(R.color.white));
        float dimension = typedArray.getDimension(R.styleable.CustomMyItem_middleTxt_dimen, 0);
        int resourceIdLeft = typedArray.getResourceId(R.styleable.CustomMyItem_leftIcon, 0);
        int resourceIdRight = typedArray.getResourceId(R.styleable.CustomMyItem_rightIcon, R.mipmap.icon_right_arrow);

        typedArray.recycle();

        if (!TextUtils.isEmpty(string)) {
            txtMiddleContent.setText(string);
        }
        txtMiddleContent.setTextColor(color);
        if (dimension != 0)
            txtMiddleContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension);

        imgLeftIcon.setImageResource(resourceIdLeft);
        imgRightIcon.setImageResource(resourceIdRight);
    }


}
