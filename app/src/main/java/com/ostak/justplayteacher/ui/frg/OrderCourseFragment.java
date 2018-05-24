package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.ostak.justplayteacher.R;

import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/24.
 *
 * @Description:
 * @see:
 */

public class OrderCourseFragment extends DBaseFragment {

    public static OrderCourseFragment newInstance(int containId) {

        Bundle args = new Bundle();

        OrderCourseFragment fragment = new OrderCourseFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
