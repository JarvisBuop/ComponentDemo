package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
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

public class LookCourseFragment extends MainBaseFragment {

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
    @BindView(R.id.img_coursefile_icon)
    ImageView imgCoursefileIcon;
    @BindView(R.id.txt_coursefile_name)
    TextView txtCoursefileName;
    @BindView(R.id.txt_coursefile_level_label)
    TextView txtCoursefileLevelLabel;
    @BindView(R.id.txt_coursefile_level)
    TextView txtCoursefileLevel;
    @BindView(R.id.txt_coursefile_reserve_label)
    TextView txtCoursefileReserveLabel;
    @BindView(R.id.txt_coursefile_reserve)
    TextView txtCoursefileReserve;
    @BindView(R.id.txt_line)
    TextView txtLine;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
//    @BindView(R.id.swipe)
//    SwipeRefreshLayout swipe;
    @BindView(R.id.btn_left_remind)
    Button btnLeftRemind;
    @BindView(R.id.btn_right_printf)
    Button btnRightPrintf;
    @BindView(R.id.btn_right_storage)
    Button btnRightStorage;
    Unbinder unbinder;

    ArrayList mDataList = new ArrayList();
    CommonAdapter mAdapter;

    public static LookCourseFragment newInstance(int containId) {

        Bundle args = new Bundle();
        LookCourseFragment fragment = new LookCourseFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.frag_look_course, container, false);
        unbinder = ButterKnife.bind(this, viewRoot);
        return viewRoot;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        txtLine.setText(MyApp.getAppInstansce().getResources().getString(R.string.line_txt_stu_book));

        initRecycler();

        initFakeData();
    }

    private void initFakeData() {
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {
//        swipe.setEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new CommonAdapter(mContext, R.layout.item_only_imageview, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setImageResource(R.id.img_display, R.mipmap.m_normal_info);
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
