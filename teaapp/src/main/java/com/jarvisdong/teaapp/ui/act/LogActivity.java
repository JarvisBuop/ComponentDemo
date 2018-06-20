package com.jarvisdong.teaapp.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.teaapp.ui.view.CustomLogView;
import com.jarvisdong.uikit.baseui.DBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/5/20.
 *
 * @Description:
 * @see:
 */
@Route(path = "/tea/log")
public class LogActivity extends DBaseActivity {
    @BindView(R2.id.edt_input_email)
    CustomLogView edtInputEmail;
    @BindView(R2.id.edt_input_psd)
    CustomLogView edtInputPsd;

    @Override
    public int getContentViewId() {
        return R.layout.activity_log_dir_tea;
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


    @OnClick({R2.id.txt_forget_psd, R2.id.btn_login, R2.id.txt_goto_regis})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.txt_forget_psd) {
            startActivity(new Intent(this, BindPhoneActivity.class));
        } else if (id == R.id.btn_login) {
            startActivity(new Intent(this, MainTeaActivity.class));
            finish();
        } else if (id == R.id.txt_goto_regis) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
