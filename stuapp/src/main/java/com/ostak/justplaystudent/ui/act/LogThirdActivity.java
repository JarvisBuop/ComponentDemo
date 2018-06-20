package com.ostak.justplaystudent.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.uikit.baseui.DBaseActivity;
import com.ostak.justplaystudent.R;
import com.ostak.justplaystudent.R2;
import com.ostak.justplaystudent.ui.view.CustomLogView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class LogThirdActivity extends DBaseActivity {
    @BindView(R2.id.edit_input_name)
    CustomLogView editInputName;
    @BindView(R2.id.edit_input_psd)
    CustomLogView editInputPsd;
    @BindView(R2.id.edit_input_psd_again)
    CustomLogView editInputPsdAgain;
    @BindView(R2.id.checkbox_user)
    CheckBox checkboxUser;
    @BindView(R2.id.btn_register)
    Button btnRegister;
    @BindView(R2.id.txt_more_way)
    TextView txtMoreWay;
    @BindView(R2.id.img_winxin)
    ImageView imgWinxin;
    @BindView(R2.id.img_qq)
    ImageView imgQq;
    @BindView(R2.id.img_weibo)
    ImageView imgWeibo;
    @BindView(R2.id.img_wangyi)
    ImageView imgWangyi;

    @Override
    public int getContentViewId() {
        return R.layout.activity_log;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @OnClick({R2.id.btn_register, R2.id.txt_more_way, R2.id.img_winxin, R2.id.img_qq, R2.id.img_weibo, R2.id.img_wangyi})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_register) {
            startActivity(new Intent(mContext, Register2Act.class));
        } else if (id == R.id.txt_more_way) {
            startActivity(new Intent(mContext, RegisterAct.class));
        }
    }
}
