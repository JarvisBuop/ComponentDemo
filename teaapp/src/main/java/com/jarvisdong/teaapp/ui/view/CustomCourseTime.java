package com.jarvisdong.teaapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/31.
 *
 * @Description:
 * @see:
 */

public class CustomCourseTime extends RelativeLayout {
    @BindView(R2.id.txt_show_label)
    TextView txtShowLabel;
    @BindView(R2.id.txt_show_time)
    TextView txtShowTime;
    @BindView(R2.id.txt_show_level)
    TextView txtShowLevel;

    public CustomCourseTime(Context context) {
        this(context, null);
    }

    public CustomCourseTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCourseTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.include_show_lesson_time, this, true);
        ButterKnife.bind(this, this);

    }

    public void setLableTxt(String labelTxt) {
        txtShowLabel.setText(labelTxt);
    }

    public TextView getLabel(){
        return txtShowLabel;
    }

    public void setShowTxt(String showTxt){
        txtShowTime.setText(showTxt);
    }

    public TextView getShowTime(){
        return txtShowTime;
    }

    public void setLevel(String levelTxt){
        txtShowLevel.setText(levelTxt);
    }

    public TextView getLevel(){
        return txtShowLevel;
    }
}
