package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.uikit.baseui.manager.FragmentParam;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/6/1.
 * OverView:
 */

public class WaitMsgFragment extends MainBaseFragment {

    @BindView(R2.id.txt_title)
    TextView txtTitle;
    @BindView(R2.id.edit_search)
    EditText editSearch;
    @BindView(R2.id.txt_msg_num)
    TextView txtMsgNum;
    @BindView(R2.id.layout_msg)
    FrameLayout layoutMsg;
    @BindView(R2.id.layout_quit)
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

    @OnClick({R2.id.btn_updata_person, R2.id.btn_setting_lesson, R2.id.btn_setting_practice})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_updata_person) {

        } else if (id == R.id.btn_setting_lesson) {
            enterLogic(new FragmentParam(PublicLessonFragment.newInstance(getContainerId()), PublicLessonFragment.class, null));
        } else if (id == R.id.btn_setting_practice) {
            enterLogic(new FragmentParam(PublicLessonFragment.newInstance(getContainerId()), PublicLessonFragment.class, null));
        }
    }

    private void enterLogic(FragmentParam t) {
        mController.switchOtherFrag(0, "", t);
    }

}
