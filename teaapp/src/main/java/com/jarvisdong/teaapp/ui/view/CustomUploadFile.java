package com.jarvisdong.teaapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/30.
 * OverView:
 */

public class CustomUploadFile extends LinearLayout {
    @BindView(R.id.txt_line)
    TextView txtLine;
    @BindView(R.id.txt_ifnot_mark)
    TextView txtMarkTitle;
    @BindView(R.id.btn_switch)
    SwitchCompat btnSwitch;
    @BindView(R.id.btn_selected_file)
    Button btnSelectedFile;
    @BindView(R.id.layout_first)
    CustomGroupTxtImg layoutFirst;
    @BindView(R.id.layout_second)
    CustomGroupTxtImg layoutSecond;
    @BindView(R.id.layout_selected)
    View layoutSelected;

    public CustomUploadFile(Context context) {
        this(context, null);
    }

    public CustomUploadFile(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomUploadFile(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_upload_video, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomUploadFile);

        String line = typedArray.getString(R.styleable.CustomUploadFile_txt_topline);
        String title = typedArray.getString(R.styleable.CustomUploadFile_txt_toptitle);
        String upload = typedArray.getString(R.styleable.CustomUploadFile_btn_middleUpload);
        boolean isShowLayout = typedArray.getBoolean(R.styleable.CustomUploadFile_layout_ifnot,true);
        boolean isShowTwoUpload = typedArray.getBoolean(R.styleable.CustomUploadFile_layout_first,true);

        if (!TextUtils.isEmpty(line)) {
            txtLine.setText(line);
        }

        if (!TextUtils.isEmpty(title)) {
            txtMarkTitle.setText(title);
        }
        if (!TextUtils.isEmpty(upload)) {
            btnSelectedFile.setText(upload);
        }
        layoutSelected.setVisibility(isShowLayout?VISIBLE:GONE);
        layoutFirst.setVisibility(isShowTwoUpload?VISIBLE:GONE);

        typedArray.recycle();
    }

    public void setFirstGroupImgListener(OnClickListener mClickListener){
        layoutFirst.getImageview().setOnClickListener(mClickListener);
    }

    public void setFirstGroupTxtListener(OnClickListener mClickListener){
        layoutFirst.getTextView().setOnClickListener(mClickListener);
    }

    public void setSecondGroupImgListener(OnClickListener mClickListener){
        layoutSecond.getImageview().setOnClickListener(mClickListener);
    }

    public void setSecondGroupTxtListener(OnClickListener mClickListener){
        layoutSecond.getTextView().setOnClickListener(mClickListener);
    }
}
