package com.ostak.justplaystudent.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.jarvisdong.uikit.baseui.DBaseActivity;
import com.ostak.justplaystudent.R;
import com.ostak.justplaystudent.ui.view.CustomLogView;
import com.ostak.justplaystudent.ui.view.CustomTabView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class ForgetPsdAct extends DBaseActivity {

    @BindView(R.id.tab_top)
    CustomTabView tabTop;
    @BindView(R.id.edit_input_phone)
    CustomLogView editInputPhone;
    @BindView(R.id.edit_code)
    CustomLogView editCode;
    @BindView(R.id.edit_psd)
    CustomLogView editPsd;
    @BindView(R.id.edit_again)
    CustomLogView editAgain;
    @BindView(R.id.edit_email)
    CustomLogView editEmail;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    public int getContentViewId() {
        return R.layout.activity_log_find_psd;
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
        tabTop.setonCheckChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_left:
                        showLeft(true);
                        break;
                    case R.id.radio_right:
                        showLeft(false);
                        break;
                }
            }
        });
    }

    private void showLeft(boolean isLeft) {
        if (isLeft) {
            editInputPhone.setVisibility(View.VISIBLE);
            editCode.setVisibility(View.VISIBLE);
            editPsd.setVisibility(View.VISIBLE);
            editAgain.setVisibility(View.VISIBLE);

            editEmail.setVisibility(View.GONE);
        }else {
            editInputPhone.setVisibility(View.GONE);
            editCode.setVisibility(View.GONE);
            editPsd.setVisibility(View.GONE);
            editAgain.setVisibility(View.GONE);

            editEmail.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.btn_register)
    public void onViewClicked() {
    }
}
