package com.ostak.justplayteacher.ui.act;

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
import com.ostak.justplayteacher.R;
import com.ostak.justplayteacher.domain.impl.MainActController;
import com.ostak.justplayteacher.ui.frg.CourseFragment;
import com.ostak.justplayteacher.ui.frg.MainBaseFragment;
import com.ostak.justplayteacher.ui.frg.MyFragment;
import com.ostak.justplayteacher.ui.frg.OrderCourseFragment;
import com.ostak.justplayteacher.ui.frg.WalletFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends DBaseExtendFragmentActivty implements MainActController {

    @BindView(R.id.img_circle)
    CircleImageView imgCircle;
    @BindView(R.id.txt_show_name)
    TextView txtShowName;
    @BindView(R.id.radio_one)
    RadioButton radioOne;
    @BindView(R.id.radio_two)
    RadioButton radioTwo;
    @BindView(R.id.radio_three)
    RadioButton radioThree;
    @BindView(R.id.radio_four)
    RadioButton radioFour;
    @BindView(R.id.radio_five)
    RadioButton radioFive;
    @BindView(R.id.layout_fragment)
    FrameLayout layoutFragment;
    @BindView(R.id.rgp_controller)
    RadioGroup rgpLeft;

    RadioButton currentRbtn;

    List<DBaseFragment> mFragments = null;
    String[] arr = new String[5];

    @Override
    public int getContentViewId() {
        return R.layout.activity_general;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        setBackOrStay(true);
    }

    @Override
    protected void initVariable() {
        mFragments = new ArrayList<>();
        CourseFragment courseFragment = CourseFragment.newInstance(R.id.layout_fragment);
        CourseFragment playerFragment = CourseFragment.newInstance(R.id.layout_fragment);
        OrderCourseFragment orderCourseFragment = OrderCourseFragment.newInstance(R.id.layout_fragment);
        WalletFragment walletFragment = WalletFragment.newInstance(R.id.layout_fragment);
        MyFragment myfragment = MyFragment.newInstance(R.id.layout_fragment);


        mFragments.add(courseFragment);
        mFragments.add(playerFragment);
        mFragments.add(orderCourseFragment);
        mFragments.add(walletFragment);
        mFragments.add(myfragment);

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
        rgpLeft.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                radioSetting(radioButton);
                int tag = Integer.parseInt((String) radioButton.getTag());
                showFragment(tag, String.valueOf(tag), arr[tag]);
            }
        });
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

    private void showPointFrag(int tag){
//        showFragment(tag, String.valueOf(tag), arr[tag]);
    }

    private void radioSetting(RadioButton radioButton) {
        if (currentRbtn != null) {
            currentRbtn.setTextColor(getResources().getColor(R.color.text_nor_gray_alpha));
        }
        radioButton.setTextColor(getResources().getColor(R.color.color_main));

        this.currentRbtn = radioButton;
    }


    @Override
    public void switchOtherFrag(int i, String s, FragmentParam o) {
        pushFragmentToBackStack(o);
    }


    public void popToFragmentsExceptArrays(String[] notPopFragTags) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            int length = notPopFragTags.length;
            int count = 0;
            for (int i = 0; i < length; i++) {
                if (!notPopFragTags[i].equals(fragment.getTag())) {
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

}
