package com.ostak.justplayteacher.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ostak.justplayteacher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/27.
 *
 * @Description:
 * @see:
 */

public class CustomDataView extends LinearLayout {

    @BindView(R.id.txt_data_content)
    TextView txtDataContent;
    @BindView(R.id.img_data_select)
    ImageView imgDataSelect;

    public CustomDataView(Context context) {
        this(context, null);
    }

    public CustomDataView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_custom_data, this, true);
        ButterKnife.bind(this, this);
    }


}
