package com.ostak.justplaystudent.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ostak.justplaystudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/6/2.
 *
 * @Description:
 * @see:
 */

public class CustomLineVie extends LinearLayout {
    @BindView(R.id.txt_line)
    TextView txtLine;
    @BindView(R.id.line_horizental)
    View lineHorizental;
    @BindView(R.id.layout_view)
    LinearLayout mLayout;

    public CustomLineVie(Context context) {
        this(context, null);
    }

    public CustomLineVie(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLineVie(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_txt_line, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomLineVie);

        String string = typedArray.getString(R.styleable.CustomLineVie_line_txt);
        txtLine.setText(string);

        int color = typedArray.getColor(R.styleable.CustomLineVie_line_color, 0);
        if (color != 0) {
            lineHorizental.setBackgroundColor(color);
            txtLine.setTextColor(color);
        }

        int anInt = typedArray.getInt(R.styleable.CustomLineVie_line_oritation, 0);
        mLayout.setOrientation(anInt == 0 ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        typedArray.recycle();
    }

    public void setTxtLine(String str) {
        txtLine.setText(str);
    }

    public void setOrietation(boolean isHori){
        if(isHori){
            mLayout.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            mLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }
}
