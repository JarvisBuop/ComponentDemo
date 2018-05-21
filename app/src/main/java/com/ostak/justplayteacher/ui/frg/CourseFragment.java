package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.ostak.justplayteacher.R;

/**
 * Created by JarvisDong on 2018/5/21.
 *
 * @Description:
 * @see:
 */

public class CourseFragment extends DBaseFragment {

    public static CourseFragment newInstance(int containId) {

        Bundle args = new Bundle();

        CourseFragment fragment = new CourseFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_course, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
