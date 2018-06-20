package com.ostak.justplaystudent.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.ostak.justplaystudent.R;
import com.ostak.justplaystudent.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class CourseCalendarFragment extends MainBaseFragment {

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    ArrayList mDataList = new ArrayList();
    CommonAdapter mAdapter;

    public static CourseCalendarFragment newInstance(int containId) {

        Bundle args = new Bundle();
        CourseCalendarFragment fragment = new CourseCalendarFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_course_lesson_time, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRecycler();
    }

    private void initRecycler() {
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter(mContext, R.layout.item_calendar_controller, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setText(R.id.txt_calendar_time, "10AM");
                holder.setText(R.id.txt_calendar_state, "释放");
            }
        };
        recyclerView.setAdapter(mAdapter);
    }
}
