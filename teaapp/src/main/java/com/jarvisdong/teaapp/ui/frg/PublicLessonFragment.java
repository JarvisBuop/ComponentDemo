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
 * OverView:
 */

public class PublicLessonFragment extends MainBaseFragment {


    Unbinder unbinder;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.txt_msg_num)
    TextView txtMsgNum;
    @BindView(R.id.layout_msg)
    FrameLayout layoutMsg;
    @BindView(R.id.layout_quit)
    FrameLayout layoutQuit;

    @BindView(R.id.part_one)
    View part1;
    @BindView(R.id.part_two)
    View part2;

    TextView part1Left;
    TextView part1Right;
    RecyclerView part1Recycler;

    TextView part2Left;
    TextView part2Right;
    RecyclerView part2Recycler;


    ArrayList mDataList1 = new ArrayList();
    CommonAdapter mAdapter1;
    ArrayList mDataList2 = new ArrayList();
    CommonAdapter mAdapter2;

    public static PublicLessonFragment newInstance(int containId) {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        PublicLessonFragment fragment = new PublicLessonFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_my_delay_lesson, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        part1Recycler = ((RecyclerView) part1.findViewById(R.id.recycler_view));
        part1Left = ((TextView) part1.findViewById(R.id.txt_left_count));
        part1Right = ((TextView) part1.findViewById(R.id.txt_right_total));

        part2Recycler = ((RecyclerView) part2.findViewById(R.id.recycler_view));
        part2Left = ((TextView) part2.findViewById(R.id.txt_left_count));
        part2Right = ((TextView) part2.findViewById(R.id.txt_right_total));
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRecycler();

        mDataList1.add(new Object());
        mDataList1.add(new Object());
        mDataList1.add(new Object());
        mAdapter1.notifyDataSetChanged();

        mDataList2.add(new Object());
        mDataList2.add(new Object());
        mDataList2.add(new Object());
        mAdapter2.notifyDataSetChanged();
    }

    private void initRecycler() {
        mAdapter1 = new CommonAdapter(mContext,R.layout.item_course,mDataList1) {
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
        part1Recycler.setLayoutManager(new LinearLayoutManager(mContext));
        part1Recycler.setAdapter(mAdapter1);

        mAdapter2 = new CommonAdapter(mContext,R.layout.item_course,mDataList2) {
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
        part2Recycler.setLayoutManager(new LinearLayoutManager(mContext));
        part2Recycler.setAdapter(mAdapter2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_setting_lesson, R.id.btn_setting_practice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_setting_lesson:
                break;
            case R.id.btn_setting_practice:
                break;
        }
    }
}
