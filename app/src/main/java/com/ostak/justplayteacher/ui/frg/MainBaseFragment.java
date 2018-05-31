package com.ostak.justplayteacher.ui.frg;

import com.jarvisdong.uikit.baseui.DBaseFragment;
import com.ostak.justplayteacher.domain.impl.MainActController;

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
