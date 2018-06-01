package com.jarvisdong.uikit.baseui.manager;


import com.jarvisdong.uikit.baseui.DBaseFragment;

public class FragmentParam {

    public DBaseFragment from;
    public Class<?> cls;
    public String fragTag;
    public Object data;

    public FragmentParam() {
    }

    public FragmentParam(DBaseFragment from, Class<?> cls, String fragTag, Object data) {
        this.from = from;
        this.cls = cls;
        this.data = data;
        this.fragTag = fragTag;
    }

    public FragmentParam(DBaseFragment from, Class<?> cls, Object data) {
        this.from = from;
        this.cls = cls;
        this.data = data;
        this.fragTag = cls.toString();
    }
}