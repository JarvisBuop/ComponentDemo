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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class CourseDetailFragment extends MainBaseFragment {
    @BindView(R2.id.recycler_view)
    RecyclerView mRecycler;

    CommonAdapter mAdapter;
    ArrayList mDataList = new ArrayList();

    public static CourseDetailFragment newInstance(int containId) {

        Bundle args = new Bundle();
        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_purchase_course, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        initRecycler();
    }

    private void initRecycler() {
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mAdapter = new CommonAdapter(mContext, R.layout.item_only_imageview2, mDataList) {

            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setImageResource(R.id.img_display,R.mipmap.m_normal_info);
            }
        };
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
