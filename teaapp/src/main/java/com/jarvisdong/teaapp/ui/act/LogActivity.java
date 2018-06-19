package com.jarvisdong.teaapp.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jarvisdong.teaapp.R;
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

public class LogActivity extends DBaseActivity {
    @BindView(R.id.edt_input_email)
    CustomLogView edtInputEmail;
    @BindView(R.id.edt_input_psd)
    CustomLogView edtInputPsd;

    @Override
    public int getContentViewId() {
        return R.layout.activity_log_dir;
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


    @OnClick({R.id.txt_forget_psd, R.id.btn_login, R.id.txt_goto_regis})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_forget_psd:
                startActivity(new Intent(this,BindPhoneActivity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(this,MainTeaActivity.class));
                finish();
                break;
            case R.id.txt_goto_regis:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
