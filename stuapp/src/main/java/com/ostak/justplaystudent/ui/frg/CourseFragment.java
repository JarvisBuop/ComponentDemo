package com.ostak.justplaystudent.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.jarvisdong.uikit.baseui.manager.FragmentParam;
import com.ostak.justplaystudent.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class CourseFragment extends MainBaseFragment {

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.txt_msg_num)
    TextView txtMsgNum;
    @BindView(R.id.layout_msg)
    FrameLayout layoutMsg;
    @BindView(R.id.layout_quit)
    FrameLayout layoutQuit;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    ArrayList mDataList = new ArrayList();
    CommonAdapter mAdapter;

    public static CourseFragment newInstance(int containId) {

        Bundle args = new Bundle();
        CourseFragment fragment = new CourseFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_main_course, container, false);
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

        mAdapter= new CommonAdapter(mContext,R.layout.item_main_course,mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setText(R.id.txt_course_name,"Mike");
                holder.setText(R.id.txt_course_tips,"Mike");
                holder.setText(R.id.btn_course_tag,"演奏级艺术家");
                holder.setText(R.id.txt_course_time,"300min");

                holder.setOnClickListener(R.id.layout_click, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CoursePurchaseLessonFragment purchaseLessonFragment = CoursePurchaseLessonFragment.newInstance(getContainerId());
                        purchaseLessonFragment.setMainActController(mController);
                        mController.switchOtherFrag(0,"",new FragmentParam(purchaseLessonFragment,CoursePurchaseLessonFragment.class,null));
                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @OnClick({R.id.layout_msg, R.id.layout_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_msg:
                break;
            case R.id.layout_quit:
                break;
        }
    }
}
