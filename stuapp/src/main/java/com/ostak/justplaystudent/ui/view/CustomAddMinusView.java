package com.ostak.justplaystudent.ui.view;

import android.content.Context;
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
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class CustomAddMinusView extends LinearLayout {
    @BindView(R2.id.txt_minus)
    TextView txtMinus;
    @BindView(R2.id.txt_content)
    TextView txtContent;
    @BindView(R2.id.txt_add)
    TextView txtAdd;

    int contentInt = 0;

    public CustomAddMinusView(Context context) {
        this(context, null);
    }

    public CustomAddMinusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomAddMinusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_leftminus_rightadd, this, true);
        ButterKnife.bind(this, this);

        contentInt= Integer.parseInt(txtContent.getText().toString());

        txtAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contentInt++;
                setContentTxt(contentInt);
            }
        });

        txtMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contentInt--;
                setContentTxt(contentInt);
            }
        });
    }

    public void setContentTxt(int contentInt){
        this.contentInt = contentInt;
        txtContent.setText(String.valueOf(this.contentInt));
    }




}
