package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.teaapp.ui.view.CustomCourseTime;
import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/5/31.
 *
 * @Description:
 * @see:
 */

public class MyAlreadyCourseFragment extends MainBaseFragment {

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
    @BindView(R2.id.img_star_icon)
    ImageView imgStarIcon;
    @BindView(R2.id.txt_star_name)
    TextView txtStarName;
    @BindView(R2.id.txt_star_age)
    TextView txtStarAge;
    @BindView(R2.id.txt_star_remain)
    TextView txtStarRemain;
    @BindView(R2.id.txt_star_time)
    TextView txtStarTime;
    @BindView(R2.id.custom_course_time_main)
    CustomCourseTime customCourseTimeMain;
    @BindView(R2.id.custom_course_time_1)
    CustomCourseTime customCourseTime1;
    @BindView(R2.id.custom_course_time_2)
    CustomCourseTime customCourseTime2;
    @BindView(R2.id.txt_line)
    TextView txtLine;
    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    ArrayList mDataLists = new ArrayList();
    CommonAdapter mAdapter;

    public static MyAlreadyCourseFragment newInstance(int containId) {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        MyAlreadyCourseFragment fragment = new MyAlreadyCourseFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_lesson, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        txtLine.setText(getActivity().getResources().getString(R.string.line_txt_course_wait));

        initRecycler();

        initFakeData();
    }

    private void initFakeData() {
        mDataLists.add(new Object());
        mDataLists.add(new Object());
        mDataLists.add(new Object());
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter(mContext, R.layout.item_course, mDataLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setVisible(R.id.course_one, false);
                holder.setText(R.id.course_two, "2018/03/20 17:00~18:33");
                holder.setText(R.id.course_three, "学生GG");
                holder.setText(R.id.course_four, "初级");
                holder.setText(R.id.course_five, "未上课");
                holder.setText(R.id.course_six, "查看课程资料 >");
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
