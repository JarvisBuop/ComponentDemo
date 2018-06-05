package com.ostak.justplaystudent.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LogActivity extends DBaseActivity {
    @BindView(R.id.edit_input_name)
    CustomLogView editInputName;
    @BindView(R.id.edt_input_psd)
    CustomLogView edtInputPsd;
    @BindView(R.id.txt_forget_psd)
    TextView txtForgetPsd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_goto_regis)
    TextView txtGotoRegis;

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

    @OnClick({R.id.btn_login, R.id.txt_goto_regis,R.id.txt_forget_psd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(mContext,MainActivity.class));
                finish();
                break;
            case R.id.txt_goto_regis:
                startActivity(new Intent(mContext,LogThirdActivity.class));
                break;
            case R.id.txt_forget_psd:
                startActivity(new Intent(mContext,ForgetPsdAct.class));
                break;
        }
    }
}
