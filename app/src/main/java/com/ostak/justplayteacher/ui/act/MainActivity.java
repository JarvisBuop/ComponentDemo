package com.ostak.justplayteacher.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jarvisdong.uikit.baseui.DBaseExtendFragmentActivty;
import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.ostak.justplayteacher.R;
import com.ostak.justplayteacher.ui.frg.CourseFragment;
import com.ostak.justplayteacher.ui.frg.OrderCourseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends DBaseExtendFragmentActivty {

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
        OrderCourseFragment orderCourseFragment = OrderCourseFragment.newInstance(R.id.layout_fragment);
        mFragments.add(courseFragment);
        mFragments.add(orderCourseFragment);

        showFragment(0);
    }

    private void showFragment(int i) {
        if (mFragments.size() > i) {
            switchFragment(mFragments.get(i), false);
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
                showFragment(tag);
            }
        });
    }

    @OnClick({R.id.img_circle, R.id.radio_one, R.id.radio_two, R.id.radio_three, R.id.radio_four, R.id.radio_five})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_circle:
                break;
            case R.id.radio_one:
                break;
            case R.id.radio_two:
                break;
            case R.id.radio_three:
                break;
            case R.id.radio_four:
                break;
            case R.id.radio_five:
                break;
        }
    }

    private void radioSetting(RadioButton radioButton) {
        if (currentRbtn != null) {
            currentRbtn.setTextColor(getResources().getColor(R.color.text_nor_gray_alpha));
        }
        radioButton.setTextColor(getResources().getColor(R.color.color_main));

        this.currentRbtn = radioButton;
    }
}
