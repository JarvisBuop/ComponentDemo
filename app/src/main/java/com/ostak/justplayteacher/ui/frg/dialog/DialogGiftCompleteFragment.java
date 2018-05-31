package com.ostak.justplayteacher.ui.frg.dialog;

import android.os.Bundle;

import com.ostak.justplayteacher.R;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public class DialogGiftCompleteFragment extends BaseDialogFragment {

    public static DialogGiftCompleteFragment newInstance() {

        Bundle args = new Bundle();
//        args.putInt("pos", i);
        DialogGiftCompleteFragment fragment = new DialogGiftCompleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.dialog_star_complete;
    }

    @Override
    protected void initViewData() {

    }
}
