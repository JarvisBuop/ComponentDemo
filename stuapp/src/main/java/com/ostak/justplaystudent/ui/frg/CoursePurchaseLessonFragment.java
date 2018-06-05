package com.ostak.justplaystudent.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvisdong.uikit.baseui.manager.FragmentParam;
import com.ostak.justplaystudent.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:订购-上课时间;
 */

public class CoursePurchaseLessonFragment extends MainBaseFragment {


    public static CoursePurchaseLessonFragment newInstance(int containId) {

        Bundle args = new Bundle();
        CoursePurchaseLessonFragment fragment = new CoursePurchaseLessonFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_purchase_selectedtime, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        CourseCalendarFragment purchaseLessonFragment = CourseCalendarFragment.newInstance(getContainerId());
        purchaseLessonFragment.setMainActController(mController);
        mController.switchOtherFrag(0,"",new FragmentParam(purchaseLessonFragment,CourseCalendarFragment.class,null));

    }
}
