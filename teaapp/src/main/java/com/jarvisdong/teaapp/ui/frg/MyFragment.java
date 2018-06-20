package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.teaapp.ui.view.CustomMyItem;
import com.jarvisdong.uikit.baseui.manager.FragmentParam;
import com.jarvisdong.uikit.baseui.manager.LifeCycleComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView: 我的;
 */

public class MyFragment extends MainBaseFragment {

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
    @BindView(R2.id.img_my_icon)
    ImageView imgMyIcon;
    @BindView(R2.id.txt_my_name)
    TextView txtMyName;
    @BindView(R2.id.txt_head_warnning)
    TextView txtHeadWarnning;
    @BindView(R2.id.txt_my_level_label)
    TextView txtMyLevelLabel;
    @BindView(R2.id.txt_my_level)
    TextView txtMyLevel;
    @BindView(R2.id.seekbar_my)
    SeekBar seekbarMy;
    @BindView(R2.id.txt_my_reserve)
    TextView txtMyReserve;
    @BindView(R2.id.txt_my_total_score)
    TextView txtMyTotalScore;
    @BindView(R2.id.layout_score)
    LinearLayout layoutScore;
    @BindView(R2.id.btn_edit_msg)
    Button btnEditMsg;
    @BindView(R2.id.cm_my_pre_course)
    CustomMyItem cmMyPreCourse;
    @BindView(R2.id.cm_my_time)
    CustomMyItem cmMyTime;
    @BindView(R2.id.cm_my_already_course)
    CustomMyItem cmMyAlreadyCourse;
    @BindView(R2.id.cm_my_comment)
    CustomMyItem cmMyComment;
    @BindView(R2.id.cm_my_pre_pratice)
    CustomMyItem cmMyPrePratice;
    @BindView(R2.id.cm_my_wallet)
    CustomMyItem cmMyWallet;
    @BindView(R2.id.cm_my_star)
    CustomMyItem cmMyStar;
    @BindView(R2.id.cm_my_publiclesson)
    CustomMyItem cmMyPubliclesson;
    Unbinder unbinder;

    public static MyFragment newInstance(int containId) {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        MyFragment fragment = new MyFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        addComponent(new LifeCycleComponent() {
            @Override
            public void onBecomesPartiallyInvisible() {

            }

            @Override
            public void onBecomesVisible() {

            }

            @Override
            public void onBecomesTotallyInvisible() {

            }

            @Override
            public void onBecomesVisibleFromTotallyInvisible() {
                //onback;

            }

            @Override
            public void onDestroy() {

            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initFakeData();
    }

    private void initFakeData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.layout_msg, R2.id.layout_quit, R2.id.btn_edit_msg, R2.id.cm_my_pre_course, R2.id.cm_my_time, R2.id.cm_my_already_course, R2.id.cm_my_comment, R2.id.cm_my_pre_pratice, R2.id.cm_my_wallet, R2.id.cm_my_star, R2.id.cm_my_publiclesson})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_edit_msg) {
            //编辑资料
            MyPersonMsgFragment myPersonMsgFragment = MyPersonMsgFragment.newInstance(getContainerId());
            myPersonMsgFragment.setMainActController(mController);
            enterLogic(new FragmentParam(myPersonMsgFragment, MyPersonMsgFragment.class, null));
        } else if (id == R.id.cm_my_pre_course) {
            //预定
            mController.switchExistFrag(0);

        } else if (id == R.id.cm_my_time) {
            mController.switchExistFrag(2);
        } else if (id == R.id.cm_my_already_course) {
            MyAlreadyCourseFragment myAlreadyCourseFragment = MyAlreadyCourseFragment.newInstance(getContainerId());
            myAlreadyCourseFragment.setMainActController(mController);
            enterLogic(new FragmentParam(myAlreadyCourseFragment, MyAlreadyCourseFragment.class, null));
        } else if (id == R.id.cm_my_comment) {
            MyCommentStuFragment myCommentStuFragment = MyCommentStuFragment.newInstance(getContainerId());
            myCommentStuFragment.setMainActController(mController);
            enterLogic(new FragmentParam(myCommentStuFragment, MyCommentStuFragment.class, null));
        } else if (id == R.id.cm_my_pre_pratice) {
            mController.switchExistFrag(1);
        } else if (id == R.id.cm_my_wallet) {
            mController.switchExistFrag(3);
        } else if (id == R.id.cm_my_star) {
            MyStarDetailFragment myStarDetailFragment = MyStarDetailFragment.newInstance(getContainerId());
            myStarDetailFragment.setMainActController(mController);
            enterLogic(new FragmentParam(myStarDetailFragment, MyStarDetailFragment.class, null));
        } else if (id == R.id.cm_my_publiclesson) {
            WaitMsgFragment waitMsgFragment = WaitMsgFragment.newInstance(getContainerId());
            waitMsgFragment.setMainActController(mController);
            enterLogic(new FragmentParam(waitMsgFragment, WaitMsgFragment.class, null));
        }
    }

    private void enterLogic(FragmentParam t) {
        mController.switchOtherFrag(0, "", t);
    }


}
