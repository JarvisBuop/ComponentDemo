package com.ostak.justplayteacher.ui.frg.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:基准dialog;
 */

public abstract class BaseDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(getContentId(), null);
        ButterKnife.bind(this, view);

        initViewData();
        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    protected abstract int getContentId();

    protected abstract void initViewData();
}
