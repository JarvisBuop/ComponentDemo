package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.teaapp.MyApp;
import com.jarvisdong.teaapp.R;
import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/5/24.
 *
 * @Description:
 * @see:
 */

public class OrderCourseFragment2 extends MainBaseFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.img_msg)
    ImageView imgMsg;
    @BindView(R.id.img_quit)
    ImageView imgQuit;
    @BindView(R.id.img_person_icon)
    ImageView imgPersonIcon;
    @BindView(R.id.txt_person_name)
    TextView txtPersonName;
    @BindView(R.id.txt_person_age)
    TextView txtPersonAge;
    @BindView(R.id.txt_person_country)
    TextView txtPersonCountry;
    @BindView(R.id.txt_person_level)
    TextView txtPersonLevel;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.txt_remain_time)
    TextView txtRemainTime;
    @BindView(R.id.txt_release_time)
    TextView txtReleaseTime;
    Unbinder unbinder;

    @BindView(R.id.txt_line1)
    View lineView1;
    @BindView(R.id.txt_line2)
    View lineView2;
    @BindView(R.id.btn_time_45)
    Button btn45;
    @BindView(R.id.btn_time_30)
    Button btn30;

    ArrayList mDataList = new ArrayList();
    CommonAdapter mAdapter;

    public static OrderCourseFragment2 newInstance(int containId) {

        Bundle args = new Bundle();

        OrderCourseFragment2 fragment = new OrderCourseFragment2();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        txtTitle.setText(MyApp.getAppInstansce().getResources().getString(R.string.radio_text3));
        lineSetting(false);


        initRecycler();


        loadDatas();
        for (int i = 0; i < 10; i++) {
            mDataList.add(new Object());
        }
        mAdapter.notifyDataSetChanged();
    }

    public void lineSetting(boolean isSingle) {
        ((TextView) lineView1.findViewById(R.id.txt_line)).setText(MyApp.getAppInstansce().getResources().getString(R.string.line_txt_selectecd_time));
        ((TextView) lineView2.findViewById(R.id.txt_line1)).setText(MyApp.getAppInstansce().getResources().getString(R.string.line_txt_selectecd_coursetime));
        ((TextView) lineView2.findViewById(R.id.txt_line2)).setText(MyApp.getAppInstansce().getResources().getString(R.string.line_txt_selectecd_time));
        if (isSingle) {
            lineView1.setVisibility(View.VISIBLE);
            lineView2.setVisibility(View.GONE);
        } else {
            lineView2.setVisibility(View.VISIBLE);
            lineView1.setVisibility(View.GONE);

            btn45.setVisibility(View.VISIBLE);
            btn30.setVisibility(View.VISIBLE);
        }
    }

    private void initRecycler() {
        mAdapter = new CommonAdapter(mContext, R.layout.item_calendar_controller, mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setText(R.id.txt_calendar_time, "10am D24:10am");
                holder.setText(R.id.txt_calendar_state, "释放");
                holder.setOnClickListener(R.id.layout_calendar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setNestedScrollingEnabled(true);
    }

    private void loadDatas() {
        txtPersonName.setText("MIke");
        txtPersonAge.setText("37岁");
        txtPersonCountry.setText("美国");
        txtPersonLevel.setText("中级");

        txtRemainTime.setText("19");
        txtReleaseTime.setText("49");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_msg, R.id.img_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_msg:
                break;
            case R.id.img_quit:
                break;
        }
    }
}
