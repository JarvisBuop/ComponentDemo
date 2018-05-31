package com.ostak.justplayteacher.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jarvisdong.uikit.baseui.DBaseActivity;
import com.ostak.justplayteacher.R;

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
    EditText edtInputEmail;
    @BindView(R.id.edt_input_psd)
    EditText edtInputPsd;

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
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.txt_goto_regis:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
