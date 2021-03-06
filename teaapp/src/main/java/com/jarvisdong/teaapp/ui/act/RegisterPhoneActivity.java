package com.jarvisdong.teaapp.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterPhoneActivity extends DBaseActivity {
    @BindView(R2.id.edit_input_phone)
    CustomLogView editInputPhone;
    @BindView(R2.id.edit_code)
    CustomLogView editCode;
    //    @BindView(R.id.txt_validCode)
//    TextView txtValidCode;
    @BindView(R2.id.edit_psd)
    CustomLogView editPsd;
    @BindView(R2.id.edit_email)
    CustomLogView editEmail;
    @BindView(R2.id.checkbox_user)
    CheckBox checkboxUser;
    @BindView(R2.id.btn_register)
    Button btnRegister;

    @Override
    public int getContentViewId() {
        return R.layout.activity_log_register;
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


    @OnClick({R2.id.btn_register})
    public void onViewClicked(View view) {
        if(view.getId()== R.id.btn_register){

        }
    }
}
