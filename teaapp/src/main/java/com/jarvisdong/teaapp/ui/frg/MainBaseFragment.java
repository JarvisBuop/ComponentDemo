package com.jarvisdong.teaapp.ui.frg;

import com.jarvisdong.teaapp.domain.impl.MainActController;
import com.jarvisdong.uikit.baseui.DBaseFragment;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public abstract class MainBaseFragment extends DBaseFragment {
    protected MainActController mController;

    public final void setMainActController(MainActController mController) {
        this.mController = mController;
    }


}
