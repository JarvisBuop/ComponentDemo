package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public class MyStarDetailFragment extends MainBaseFragment {
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
    @BindView(R2.id.txt_line)
    TextView txtLine;
    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    CommonAdapter mAdapter;
    ArrayList mDataList = new ArrayList();


    public static MyStarDetailFragment newInstance(int containId) {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        MyStarDetailFragment fragment = new MyStarDetailFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_star_send, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        txtLine.setText(getActivity().getResources().getString(R.string.line_txt_msg_gift));

        initRecycler();

        initFakeData();
    }

    private void initFakeData() {
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 5);
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new CommonAdapter(mContext, R.layout.item_inner_star, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

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
