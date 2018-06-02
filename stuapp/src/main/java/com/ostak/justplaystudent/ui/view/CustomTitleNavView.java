package com.ostak.justplaystudent.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ostak.justplaystudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/6/2.
 *
 * @Description:
 * @see:
 */

public class CustomTitleNavView extends LinearLayout {
    @BindView(R.id.btn_nav1)
    Button btnNav1;
    @BindView(R.id.img_nav1)
    ImageView imgNav1;
    @BindView(R.id.btn_nav2)
    Button btnNav2;
    @BindView(R.id.img_nav2)
    ImageView imgNav2;
    @BindView(R.id.btn_nav3)
    Button btnNav3;
    @BindView(R.id.img_nav3)
    ImageView imgNav3;

    public CustomTitleNavView(Context context) {
        this(context,null);
    }

    public CustomTitleNavView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTitleNavView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_main_tab_button, this, true);
        ButterKnife.bind(this, this);

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleNavView);
//        typedArray.recycle();
//
        selectPlace(0);
    }


    @OnClick({R.id.btn_nav1, R.id.btn_nav2, R.id.btn_nav3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_nav1:
                selectPlace(0);
                break;
            case R.id.btn_nav2:
                selectPlace(1);
                break;
            case R.id.btn_nav3:
                selectPlace(2);
                break;
        }
    }

    public void selectPlace(int pos) {
        switch (pos) {
            case 0:
                imgNav1.setVisibility(VISIBLE);
                imgNav2.setVisibility(INVISIBLE);
                imgNav3.setVisibility(INVISIBLE);
                break;
            case 1:
                imgNav1.setVisibility(INVISIBLE);
                imgNav2.setVisibility(VISIBLE);
                imgNav3.setVisibility(INVISIBLE);
                break;
            case 2:
                imgNav1.setVisibility(INVISIBLE);
                imgNav2.setVisibility(INVISIBLE);
                imgNav3.setVisibility(VISIBLE);
                break;
        }

        if (mListerner != null) {
            mListerner.change(pos);
        }
    }


    private onChangePageListener mListerner;

    public void setOnChangePageListener(onChangePageListener mListerner) {
        this.mListerner = mListerner;
    }

    public interface onChangePageListener {
        void change(int pos);
    }
}
