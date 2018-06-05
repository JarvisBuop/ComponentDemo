package com.ostak.justplaystudent.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.uikit.adapter.QuickFuncAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ItemViewDelegate;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.jarvisdong.uikit.baseui.manager.FragmentParam;
import com.ostak.justplaystudent.R;
import com.ostak.justplaystudent.ui.view.CustomLineVie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/6/5.
 * OverView:
 */

public class CoursePurChaseFragment extends MainBaseFragment {

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.img_msg)
    ImageView imgMsg;
    @BindView(R.id.txt_msg_num)
    TextView txtMsgNum;
    @BindView(R.id.layout_msg)
    FrameLayout layoutMsg;
    @BindView(R.id.img_quit)
    ImageView imgQuit;
    @BindView(R.id.layout_quit)
    FrameLayout layoutQuit;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    QuickFuncAdapter mAdapter;
    ArrayList<String> mDataList = new ArrayList();

    public static CoursePurChaseFragment newInstance(int containId) {

        Bundle args = new Bundle();
        CoursePurChaseFragment fragment = new CoursePurChaseFragment();
        fragment.setContainerId(containId);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_purchase_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        initRecycler();
    }

    private void initRecycler() {
        mDataList.add("演奏级");
        mDataList.add("高级");
        mDataList.add("content");
        mDataList.add("content");
        mDataList.add("content");
        mDataList.add("content");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mDataList.size() > position) {
                    if (!mDataList.get(position).equals("content")) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new QuickFuncAdapter<String, ViewHolder>(recyclerView, mDataList, QuickFuncAdapter.EMPTY_VIEW) {
            @Override
            protected int addCustomItemViewDelegate(QuickFuncAdapter adapter) {
                addItemViewDelegate(new ItemViewDelegate<String>() {
                    @Override
                    public int getItemViewLayoutId() {
                        return R.layout.item_main_course;
                    }

                    @Override
                    public boolean isForViewType(String item, int position) {
                        return item.equals("content");
                    }

                    @Override
                    public void convert(ViewHolder holder, String s, int position) {
                        holder.setText(R.id.txt_course_name, "Mike");
                        holder.setText(R.id.txt_course_tips, "Mike");
                        holder.setText(R.id.btn_course_tag, "演奏级艺术家");
                        holder.setText(R.id.txt_course_time, "300min");

                        holder.setOnClickListener(R.id.layout_click, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CourseDetailFragment courseDetailFragment = CourseDetailFragment.newInstance(getContainerId());
                                courseDetailFragment.setMainActController(mController);
                                mController.switchOtherFrag(0, "", new FragmentParam(courseDetailFragment, CourseDetailFragment.class, null));
                            }
                        });

                    }
                });

                addItemViewDelegate(new ItemViewDelegate<String>() {
                    @Override
                    public int getItemViewLayoutId() {
                        return R.layout.item_line;
                    }

                    @Override
                    public boolean isForViewType(String item, int position) {
                        return !item.equals("content");
                    }

                    @Override
                    public void convert(ViewHolder holder, String o, int position) {
                        CustomLineVie view = holder.getView(R.id.custom_line);
                        if (position == 0) {
                            view.setOrietation(false);
                        } else {
                            view.setOrietation(true);
                        }
                        view.setTxtLine(o);
                    }
                });

                return 0;
            }
        };

        mAdapter.attachRecyclerView(recyclerView);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
