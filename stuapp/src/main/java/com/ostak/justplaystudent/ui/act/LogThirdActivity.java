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
import com.ostak.justplaystudent.ui.view.CustomLogView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class LogThirdActivity extends DBaseActivity {
    @BindView(R.id.edit_input_name)
    CustomLogView editInputName;
    @BindView(R.id.edit_input_psd)
    CustomLogView editInputPsd;
    @BindView(R.id.edit_input_psd_again)
    CustomLogView editInputPsdAgain;
    @BindView(R.id.checkbox_user)
    CheckBox checkboxUser;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.txt_more_way)
    TextView txtMoreWay;
    @BindView(R.id.img_winxin)
    ImageView imgWinxin;
    @BindView(R.id.img_qq)
    ImageView imgQq;
    @BindView(R.id.img_weibo)
    ImageView imgWeibo;
    @BindView(R.id.img_wangyi)
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

    @OnClick({R.id.btn_register, R.id.txt_more_way, R.id.img_winxin, R.id.img_qq, R.id.img_weibo, R.id.img_wangyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(mContext,Register2Act.class));
                break;
            case R.id.txt_more_way:
                startActivity(new Intent(mContext,RegisterAct.class));
                break;
            case R.id.img_winxin:
                break;
            case R.id.img_qq:
                break;
            case R.id.img_weibo:
                break;
            case R.id.img_wangyi:
                break;
        }
    }
}
