package com.jarvisdong.uikit.baseui.manager;


import com.jarvisdong.uikit.baseui.DBaseFragment;

public class FragmentParam {

    public DBaseFragment from;
    public Class<?> cls;
    public Object data;
    public String extraMark;

    public FragmentParam() {
    }

    public FragmentParam(DBaseFragment from, Class<?> cls, Object data) {
        this.from = from;
        this.cls = cls;
        this.data = data;
    }
}