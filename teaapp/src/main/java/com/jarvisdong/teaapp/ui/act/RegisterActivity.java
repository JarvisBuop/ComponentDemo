package com.jarvisdong.teaapp.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.teaapp.ui.view.CustomLogView;
import com.jarvisdong.uikit.baseui.DBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/5/21.
 *
 * @Description:
 * @see:
 */

public class RegisterActivity extends DBaseActivity {
    @BindView(R2.id.edit_input_email)
    CustomLogView editInputEmail;
    @BindView(R2.id.edit_input_psd)
    CustomLogView editInputPsd;
    @BindView(R2.id.edit_input_psd_again)
    CustomLogView editInputPsdAgain;
    @BindView(R2.id.checkbox_user)
    CheckBox checkboxUser;

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

        } else if (id == R.id.txt_more_way) {
            startActivity(new Intent(this, RegisterPhoneActivity.class));
        }
    }
}
