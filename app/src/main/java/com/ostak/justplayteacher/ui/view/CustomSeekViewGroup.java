package com.ostak.justplayteacher.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ostak.justplayteacher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/26.
 *
 * @Description:
 * @see:
 */

public class CustomSeekViewGroup extends LinearLayout {

    @BindView(R.id.txt_left_tips)
    TextView txtLeftTips;
    @BindView(R.id.seekbar_content)
    SeekBar seekbarContent;
    @BindView(R.id.txt_right_content)
    TextView txtRightContent;

    public CustomSeekViewGroup(Context context) {
        this(context, null);
    }

    public CustomSeekViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.view_seek_group, this, true);
        ButterKnife.bind(this, view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSeekViewGroup);

        String leftName = typedArray.getString(R.styleable.CustomSeekViewGroup_leftName);

        if (!TextUtils.isEmpty(leftName)) {
            txtLeftTips.setText(leftName);
        }
        typedArray.recycle();
        seekbarContent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRightContent.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void setTxtLeftTips(TextView txtLeftTips) {
        this.txtLeftTips = txtLeftTips;
    }


}