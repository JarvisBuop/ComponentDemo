package com.jarvisdong.teaapp.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.teaapp.ui.frg.dialog.DialogGiftCompleteFragment;
import com.jarvisdong.teaapp.ui.frg.dialog.DialogGiftFragment;
import com.jarvisdong.teaapp.ui.view.CustomSeekViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public class MyCommentStuFragment extends MainBaseFragment {

    @BindView(R2.id.img_back)
    ImageView imgBack;
    @BindView(R2.id.txt_title)
    TextView txtTitle;
    @BindView(R2.id.img_close)
    ImageView imgClose;
    @BindView(R2.id.img_circle)
    CircleImageView imgCircle;
    @BindView(R2.id.txt_show_name)
    TextView txtShowName;
    @BindView(R2.id.seekbar_total)
    CustomSeekViewGroup seekbarTotal;
    @BindView(R2.id.seekbar_skill)
    CustomSeekViewGroup seekbarSkill;
    @BindView(R2.id.seekbar_proficiency)
    CustomSeekViewGroup seekbarProficiency;
    @BindView(R2.id.seekbar_emotion)
    CustomSeekViewGroup seekbarEmotion;
    @BindView(R2.id.seekbar_serious)
    CustomSeekViewGroup seekbarSerious;
    @BindView(R2.id.txt_averager_content)
    TextView txtAveragerContent;
    @BindView(R2.id.txt_line)
    TextView viewLine;
    @BindView(R2.id.txt_comment_content)
    TextView txtCommentContent;
    @BindView(R2.id.img_audio_play)
    ImageView imgAudioPlay;
    @BindView(R2.id.txt_audio_play)
    TextView txtAudioPlay;
    @BindView(R2.id.txt_audio_record)
    TextView txtAudioRecord;
    @BindView(R2.id.cbox_star)
    CheckBox cboxStar;
    @BindView(R2.id.btn_submit_star)
    Button btnSubmitStar;
    Unbinder unbinder;

    public static MyCommentStuFragment newInstance(int containId) {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        MyCommentStuFragment fragment = new MyCommentStuFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_grade_stu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        viewLine.setText(getActivity().getResources().getString(R.string.line_txt_msg_comment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R2.id.img_back, R2.id.img_close, R2.id.txt_audio_record, R2.id.btn_submit_star})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_submit_star) {
            DialogGiftFragment dialogGiftFragment = DialogGiftFragment.newInstance();
            dialogGiftFragment.setTargetParent(this);
            dialogGiftFragment.show(getChildFragmentManager(), DialogGiftFragment.class.toString());
        }
    }

    public void completeStar() {
        DialogGiftCompleteFragment giftCompleteFragment = DialogGiftCompleteFragment.newInstance();
        giftCompleteFragment.show(getChildFragmentManager(), giftCompleteFragment.getClass().toString());
    }
}
