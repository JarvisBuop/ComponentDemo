package com.ostak.justplaystudent.ui.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jarvisdong.uikit.baseui.DBaseExtendFragmentActivty;
import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.jarvisdong.uikit.baseui.manager.FragmentParam;
import com.ostak.justplaystudent.R;
import com.ostak.justplaystudent.domain.MainActController;
import com.ostak.justplaystudent.ui.frg.CourseFragment;
import com.ostak.justplaystudent.ui.frg.CoursePublicFragment;
import com.ostak.justplaystudent.ui.frg.CoursePurChaseFragment;
import com.ostak.justplaystudent.ui.frg.MainBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainStuActivity extends DBaseExtendFragmentActivty implements MainActController {


    RadioButton currentRbtn;

    List<DBaseFragment> mFragments = null;
    String[] arr = new String[5];

    @BindView(R2.id.img_circle)
    CircleImageView imgCircle;
    @BindView(R2.id.txt_show_name)
    TextView txtShowName;
    @BindView(R2.id.radio_one)
    RadioButton radioOne;
    @BindView(R2.id.radio_two)
    RadioButton radioTwo;
    @BindView(R2.id.radio_three)
    RadioButton radioThree;
    @BindView(R2.id.radio_four)
    RadioButton radioFour;
    @BindView(R2.id.radio_five)
    RadioButton radioFive;
    @BindView(R2.id.rgp_controller)
    RadioGroup rgpLeft;
    @BindView(R2.id.layout_fragment)
    FrameLayout layoutFragment;
    @BindView(R2.id.img_circle_big)
    CircleImageView imgCircleBig;
    @BindView(R2.id.img_circle_middle)
    CircleImageView imgCircleMiddle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_general;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected void initVariable() {
        mFragments = new ArrayList<>();
        CourseFragment courseFragment = CourseFragment.newInstance(R.id.layout_fragment);
        CoursePurChaseFragment purChaseFragment = CoursePurChaseFragment.newInstance(R.id.layout_fragment);
        CoursePublicFragment publicFragment = CoursePublicFragment.newInstance(R.id.layout_fragment);


        mFragments.add(courseFragment);
        mFragments.add(purChaseFragment);
        mFragments.add(publicFragment);

        for (int i = 0; i < mFragments.size(); i++) {
            MainBaseFragment mainBaseFragment = (MainBaseFragment) mFragments.get(i);
            mainBaseFragment.setMainActController(this);
            arr[i] = mFragments.get(i).getClass().toString() + i;
        }

        showFragment(0, "0", arr[0]);
    }

    private void showFragment(int i, Object object, String uniqueKey) {
        if (mFragments.size() > i) {
            //每次去除除保留的其他的fragment;
            popToFragmentsExceptArrays(arr);

            DBaseFragment dBaseFragment = mFragments.get(i);
            pushFragmentToBackStack(dBaseFragment.getClass(), uniqueKey, object, dBaseFragment);
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @OnClick({R.id.img_circle, R.id.radio_one, R.id.radio_two, R.id.radio_three, R.id.radio_four, R.id.radio_five})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_circle:
                break;
            case R.id.radio_one:
                showPointFrag(0);
                break;
            case R.id.radio_two:
                showPointFrag(1);
                break;
            case R.id.radio_three:
                showPointFrag(2);
                break;
            case R.id.radio_four:
                showPointFrag(3);
                break;
            case R.id.radio_five:
                showPointFrag(4);
                break;
        }
    }

    private int getRadioId(int tag) {
        switch (tag) {
            case 0:
                return R.id.radio_one;
            case 1:
                return R.id.radio_two;
            case 2:
                return R.id.radio_three;
            case 3:
                return R.id.radio_four;
            case 4:
                return R.id.radio_five;
        }
        return R.id.radio_one;
    }

    private void showPointFrag(int tag) {
        RadioButton radioButton = rgpLeft.findViewById(getRadioId(tag));
        radioSetting(radioButton);
        int tagStr = Integer.parseInt((String) radioButton.getTag());
        showFragment(tagStr, String.valueOf(tag), arr[tag]);
    }

    private void radioSetting(RadioButton radioButton) {
        if (currentRbtn != null && radioButton != currentRbtn) {
            currentRbtn.setTextColor(getResources().getColor(R.color.text_nor_gray_alpha));
            currentRbtn.setChecked(false);
        }
        radioButton.setChecked(true);
        radioButton.setTextColor(getResources().getColor(R.color.color_main));

        this.currentRbtn = radioButton;
    }


    @Override
    public void switchOtherFrag(int i, String s, FragmentParam o) {
        pushFragmentToBackStack(o);
    }

    @Override
    public void switchExistFrag(int i) {
        showPointFrag(i);
    }


    public void popToFragmentsExceptArrays(String[] notPopFragTags) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            int length = notPopFragTags.length;
            int count = 0;
            for (int i = 0; i < length; i++) {
                if (notPopFragTags[i]!=null && !notPopFragTags[i].equals(fragment.getTag())) {
                    count++;
                } else {
                    break;
                }
                if (count == length) {
                    popToFragment(fragment.getTag());
                }
            }

        }
    }

    @Override
    protected boolean isReturnFinish(int count) {
        return count <= 5;
    }
}
