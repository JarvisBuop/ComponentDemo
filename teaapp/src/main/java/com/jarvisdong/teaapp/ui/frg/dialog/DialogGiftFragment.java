package com.jarvisdong.teaapp.ui.frg.dialog;

import android.os.Bundle;
import android.view.View;

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.teaapp.ui.frg.MyCommentStuFragment;

import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public class DialogGiftFragment extends BaseDialogFragment {
    MyCommentStuFragment mTarget;


    public static DialogGiftFragment newInstance() {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        DialogGiftFragment fragment = new DialogGiftFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.dialog_star;
    }

    @Override
    protected void initViewData() {

    }

    public void setTargetParent(MyCommentStuFragment mTarget) {
        this.mTarget = mTarget;
    }

    @OnClick({R2.id.btn_yes, R2.id.btn_no})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_yes) {
            dismiss();
            mTarget.completeStar();
        } else if (id == R.id.btn_no) {

        }
    }
}
