package com.jarvisdong.teaapp.domain.impl;

import com.jarvisdong.uikit.baseui.manager.FragmentParam;

/**
 * Created by JarvisDong on 2018/5/31.
 * OverView:
 */

public interface MainActController {

    //切换frag;
    void switchOtherFrag(int i, String s, FragmentParam t);

    void switchExistFrag(int i);
}
