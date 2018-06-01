package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ostak.justplayteacher.R;
import com.ostak.justplayteacher.ui.view.CustomGroupTxtImg;
import com.ostak.justplayteacher.ui.view.CustomSpannable;
import com.ostak.justplayteacher.ui.view.CustomUploadFile;
import com.ostak.justplayteacher.ui.view.CustomUploadFileStyle2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public class MyPersonMsgFragment extends MainBaseFragment {

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
    @BindView(R.id.img_female)
    ImageView imgFemale;
    @BindView(R.id.edit_input_name)
    EditText editInputName;
    @BindView(R.id.edit_input_phone)
    EditText editInputPhone;
    @BindView(R.id.edit_input_email)
    EditText editInputEmail;
    @BindView(R.id.edit_input_age)
    EditText editInputAge;
    @BindView(R.id.edit_input_techschool)
    EditText editInputTechschool;
    @BindView(R.id.edit_input_graduteschool)
    EditText editInputGraduteschool;
    @BindView(R.id.spannable_country)
    CustomSpannable spannableCountry;
    @BindView(R.id.spannable_time_tech)
    CustomSpannable spannableTimeTech;
    @BindView(R.id.upload_file_head)
    CustomGroupTxtImg uploadFileHead;
    @BindView(R.id.upload_file_teacher)
    CustomUploadFile uploadFileTeacher;
    @BindView(R.id.upload_file_person)
    CustomUploadFile uploadFilePerson;
    @BindView(R.id.upload_file_otherfile)
    CustomUploadFile uploadFileOtherfile;
    @BindView(R.id.upload_style2_recommend)
    CustomUploadFileStyle2 uploadStyle2Recommend;
    @BindView(R.id.upload_style2_idcard)
    CustomUploadFileStyle2 uploadStyle2Idcard;
    @BindView(R.id.upload_style2_passport)
    CustomUploadFileStyle2 uploadStyle2Passport;
    @BindView(R.id.btn_updata)
    Button btnUpdata;
    Unbinder unbinder;

    public static MyPersonMsgFragment newInstance(int containId) {

        Bundle args = new Bundle();
        MyPersonMsgFragment fragment = new MyPersonMsgFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_person_msg, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @OnClick({R.id.img_msg, R.id.img_quit, R.id.btn_updata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_msg:
                break;
            case R.id.img_quit:
                break;
            case R.id.btn_updata:
                break;
        }
    }


}
