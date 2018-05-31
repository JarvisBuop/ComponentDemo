package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ostak.justplayteacher.MyApp;
import com.ostak.justplayteacher.R;

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

public class OrderCourseFragment extends MainBaseFragment {

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

    public static OrderCourseFragment newInstance(int containId) {

        Bundle args = new Bundle();

        OrderCourseFragment fragment = new OrderCourseFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        unbinder =ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        txtTitle.setText(MyApp.getAppInstansce().getResources().getString( R.string.radio_text3));
        loadDatas();
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
