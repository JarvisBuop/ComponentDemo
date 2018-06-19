package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/6/1.
 *
 * @Description:
 * @see:
 */

public class MineCourseFragment extends MainBaseFragment {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.txt_left_count)
    TextView txtLeftCount;
    @BindView(R.id.txt_right_total)
    TextView txtRightTotal;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    Unbinder unbinder;

    ArrayList mDataList = new ArrayList();
    CommonAdapter mAdapter;

    public static MineCourseFragment newInstance(int containId) {

        Bundle args = new Bundle();
        MineCourseFragment fragment = new MineCourseFragment();
        fragment.setContainerId(containId);
        return fragment;
    }


    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_mine_course, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initRecycler();

        initFakeData();
    }

    private void initFakeData() {
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {
        mAdapter = new CommonAdapter(mContext, R.layout.item_mine_course, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setText(R.id.txt_course_time, "2018-19-33 ~~~~~~~");
                holder.setText(R.id.txt_course_remain_time, "距离上课 : 2222");
                holder.setOnClickListener(R.id.btn_course_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                if (position % 2 == 0) {
                    holder.setVisible(R.id.line_left,false);
                }else {
                    holder.setVisible(R.id.line_left,true);
                }
            }
        };
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_back, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                break;
            case R.id.img_close:
                break;
        }
    }
}
