package com.ostak.justplaystudent.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
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

public class LogActivity extends DBaseActivity {
    @BindView(R2.id.edit_input_name)
    CustomLogView editInputName;
    @BindView(R2.id.edt_input_psd)
    CustomLogView edtInputPsd;
    @BindView(R2.id.txt_forget_psd)
    TextView txtForgetPsd;
    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.txt_goto_regis)
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

    @OnClick({R2.id.btn_login, R2.id.txt_goto_regis, R2.id.txt_forget_psd, R2.id.txt_click_intent})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            startActivity(new Intent(mContext, MainStuActivity.class));
        } else if (id == R.id.txt_goto_regis) {
            startActivity(new Intent(mContext, LogThirdActivity.class));
        } else if (id == R.id.txt_forget_psd) {
            startActivity(new Intent(mContext, ForgetPsdAct.class));
        } else if (id == R.id.txt_click_intent) {
            /**
             * 多组件通信中:
             * 注意多个组件中的布局名称不能相同,否则出现 {@link NoSuchFieldError} 错误;
             */
            ARouter.getInstance().build("/tea/log").navigation();
        }
    }
}
