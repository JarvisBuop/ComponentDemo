package com.ostak.justplayteacher.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ostak.justplayteacher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/30.
 * OverView:
 */

public class CustomUploadFileStyle2 extends LinearLayout {
    @BindView(R.id.txt_line)
    TextView txtLine;
    @BindView(R.id.txt_label1)
    TextView txtLabel1;
    @BindView(R.id.edt_upload_num)
    EditText edtUploadNum;
    @BindView(R.id.txt_label2)
    TextView txtLabel2;
    @BindView(R.id.btn_selected_file)
    Button btnSelectedFile;
    @BindView(R.id.layout_first)
    CustomGroupTxtImg layoutFirst;
    @BindView(R.id.layout_second)
    CustomGroupTxtImg layoutSecond;
    @BindView(R.id.txt_upload_thing)
    TextView txtUploadThing;

    public CustomUploadFileStyle2(Context context) {
        this(context, null);
    }

    public CustomUploadFileStyle2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomUploadFileStyle2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_upload_video2, this, true);
        ButterKnife.bind(this, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomUploadFileStyle2);
        String string = typedArray.getString(R.styleable.CustomUploadFileStyle2_edit_hint);
        edtUploadNum.setHint(string);

        String string1 = typedArray.getString(R.styleable.CustomUploadFileStyle2_txt_edit_label);
        txtLabel1.setText(string1);

        String string2 = typedArray.getString(R.styleable.CustomUploadFileStyle2_txt_btn_label);
        txtLabel2.setText(string2);

        String string3 = typedArray.getString(R.styleable.CustomUploadFileStyle2_txt_topline2);
        txtLine.setText(string3);

        boolean bool = typedArray.getBoolean(R.styleable.CustomUploadFileStyle2_txt_thing, false);
        txtUploadThing.setVisibility(bool ? VISIBLE : GONE);


        typedArray.recycle();
    }
}
