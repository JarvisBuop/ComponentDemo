package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jarvisdong.uikit.baseui.manager.FragmentParam;
import com.ostak.justplayteacher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/6/1.
 * OverView:
 */

public class WaitMsgFragment extends MainBaseFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.txt_msg_num)
    TextView txtMsgNum;
    @BindView(R.id.layout_msg)
    FrameLayout layoutMsg;
    @BindView(R.id.layout_quit)
    FrameLayout layoutQuit;
    Unbinder unbinder;

    public static WaitMsgFragment newInstance(int containId) {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        WaitMsgFragment fragment = new WaitMsgFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_my_submitmsg, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_updata_person, R.id.btn_setting_lesson, R.id.btn_setting_practice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_updata_person:
                break;
            case R.id.btn_setting_lesson:
                enterLogic(new FragmentParam(PublicLessonFragment.newInstance(getContainerId()), PublicLessonFragment.class, null));
                break;
            case R.id.btn_setting_practice:
                enterLogic(new FragmentParam(PublicLessonFragment.newInstance(getContainerId()), PublicLessonFragment.class, null));
                break;
        }
    }
    private void enterLogic(FragmentParam t) {
        mController.switchOtherFrag(0, "", t);
    }

}
