package com.ostak.justplaystudent.ui.frg;

import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.ostak.justplaystudent.domain.MainActController;

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
