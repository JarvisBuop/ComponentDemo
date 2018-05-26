package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.uikit.adapter.QuickFuncAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.jarvisdong.uikit.adapter.wrapper.LoadMoreWrapper;
import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.ostak.justplayteacher.MyApp;
import com.ostak.justplayteacher.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/5/21.
 *
 * @Description: 课程Fragment;
 * @see:
 */

public class CourseFragment extends DBaseFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.img_msg)
    ImageView imgMsg;
    @BindView(R.id.img_quit)
    ImageView imgQuit;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    Unbinder unbinder;

    QuickFuncAdapter mAdapter;
    List mDataLists = new ArrayList();
    int page = 0;
    private TextView txtHeadLeft;
    private TextView txtHeadRight;

    public static CourseFragment newInstance(int containId, int i) {

        Bundle args = new Bundle();
        args.putInt("pos", i);
        CourseFragment fragment = new CourseFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_course, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        editSearch.clearFocus();
        txtTitle.setText(MyApp.getAppInstansce().getString(getArguments().getInt("pos") == 1 ? R.string.radio_text2 : R.string.radio_text1));
        initRecycler();
        loadDatas(false);
    }


    private void loadDatas(boolean clear) {
        recyclerView.postDelayed(() -> {
            if (swipe != null)
                swipe.setRefreshing(false);
            if (clear) {
                mDataLists.clear();
            }
            mDataLists.add(page);
            mDataLists.add(page);
            mDataLists.add(page);
            mDataLists.add(page);
            mDataLists.add(page);
            mAdapter.notifyDataSetChangedWrapper();
            page++;
            if (page == 5) {
                mAdapter.resetEmptyDefault();
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.resetEmptyRetry();
                mAdapter.loadMoreSuccess();
            }
        }, 1500);

        txtHeadRight.setText("取消课程 >");
        String count = mDataLists.size()+"";
        SpannableStringBuilder ssb = new SpannableStringBuilder("共计" + count + "次公开课");
        ssb.setSpan(new ForegroundColorSpan(MyApp.getAppInstansce().getResources().getColor(R.color.color_main)),
                2, count.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtHeadLeft.setText(ssb);

    }

    private void initRecycler() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                mAdapter.loadMoreReset();
                loadDatas(true);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        mAdapter = new QuickFuncAdapter(recyclerView, mDataLists, QuickFuncAdapter.HEADER_LOADING_VIEW) {
            @Override
            protected int addCustomItemViewDelegate(QuickFuncAdapter adapter) {
                return R.layout.item_course;
            }

            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                super.convert(holder, o, position);
                holder.setText(R.id.course_one, "一堂美妙的盛乐" + o);
                holder.setText(R.id.course_two, "2018/03/10 18:00~18:45");
                holder.setText(R.id.course_three, "适合中级");
                holder.setText(R.id.course_four, "审核通过");
                holder.setText(R.id.course_five, "未开始");
                holder.setText(R.id.course_six, "查看 >");
            }
        };
        mAdapter.setOnEmptyClickListener(v -> {
            page = 0;
            mAdapter.loadMoreReset();
            loadDatas(false);//empty
        });
        mAdapter.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreCallbackListener() {
            @Override
            public void onLoadMoreRequested() {
                loadDatas(false);//loadmore
            }

            @Override
            public void onfooterStatus(int status) {

            }
        });

        View headView = LayoutInflater.from(mContext).inflate(R.layout.include_txt_left_right, recyclerView, false);
        txtHeadLeft = (TextView) headView.findViewById(R.id.txt_left_count);
        txtHeadRight = (TextView) headView.findViewById(R.id.txt_right_cancel);
        mAdapter.addHeaderView(headView);
        mAdapter.attachRecyclerView(recyclerView);
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
