package com.ostak.justplaystudent.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class CoursePublicFragment extends MainBaseFragment {

    public static CoursePublicFragment newInstance(int containId) {

        Bundle args = new Bundle();
        CoursePublicFragment fragment = new CoursePublicFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
