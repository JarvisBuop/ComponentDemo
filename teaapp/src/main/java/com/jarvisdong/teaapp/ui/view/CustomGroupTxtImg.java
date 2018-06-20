package com.jarvisdong.teaapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/30.
 * OverView:
 */

public class CustomGroupTxtImg extends RelativeLayout {
    @BindView(R2.id.img_show_top)
    ImageView imgShowTop;
    @BindView(R2.id.txt_show_bottom)
    TextView txtShowBottom;
    @BindView(R2.id.layout_group)
    LinearLayout layoutGroup;

    public CustomGroupTxtImg(Context context) {
        this(context, null);
    }

    public CustomGroupTxtImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGroupTxtImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_group_txt_img, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomGroupTxtImg);
        String string = typedArray.getString(R.styleable.CustomGroupTxtImg_txt_bottom);
        int resourceId = typedArray.getResourceId(R.styleable.CustomGroupTxtImg_img_top, 0);
        if (!TextUtils.isEmpty(string)) {
            txtShowBottom.setText(string);
        }
        if(resourceId!=0){
            imgShowTop.setImageResource(resourceId);
        }
        typedArray.recycle();
    }

    public void setImageView(int resourceId){
        imgShowTop.setImageResource(resourceId);
    }

    public ImageView getImageview(){
        return imgShowTop;
    }

    public void setTextView(String str){
        txtShowBottom.setText(str);
    }

    public TextView getTextView(){
        return txtShowBottom;
    }
}
