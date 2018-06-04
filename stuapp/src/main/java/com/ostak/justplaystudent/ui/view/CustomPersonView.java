package com.ostak.justplaystudent.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ostak.justplaystudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/6/4.
 * OverView:
 */

public class CustomPersonView extends LinearLayout {
    private final int arrowMode;
    @BindView(R.id.txt_main_left)
    TextView txtMainLeft;
    @BindView(R.id.switch_person)
    SwitchCompat switchPerson;
    @BindView(R.id.txt_main_right)
    TextView txtMainRight;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;

    public CustomPersonView(Context context) {
        this(context, null);
    }

    public CustomPersonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPersonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_person_common, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomPersonView);

        String stringMain = typedArray.getString(R.styleable.CustomPersonView_main_label);
        String stringContent = typedArray.getString(R.styleable.CustomPersonView_content_label);
        arrowMode = typedArray.getInt(R.styleable.CustomPersonView_img_arrow, 0);
        boolean aBoolean = typedArray.getBoolean(R.styleable.CustomPersonView_switch_gone, false);
        txtMainLeft.setText(stringMain);
        txtMainRight.setText(stringContent);
        switchPerson.setVisibility(aBoolean?VISIBLE:GONE);
        typedArray.recycle();

        initArrow();
    }

    private void initArrow() {
        switch (arrowMode) {
            case 0:
                imgArrow.setVisibility(VISIBLE);
                break;
            case 1:
                imgArrow.setVisibility(INVISIBLE);
                break;
            case 2:
                imgArrow.setVisibility(VISIBLE);
                ObjectAnimator.ofFloat(imgArrow, "rotation", 0, 90);
                break;
        }

    }


}
