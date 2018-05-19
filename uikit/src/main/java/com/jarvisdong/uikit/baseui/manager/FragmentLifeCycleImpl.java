package com.jarvisdong.uikit.baseui.manager;

/**
 * Created by JarvisDong on 2017/10/30.
 * OverView:
 *  fragment生命周期管理 ,类似于activity;
 */

public interface FragmentLifeCycleImpl {
    /**
     * pass the data from to  pushFragmentToBackStack this fragment
     *
     * @param data
     */
    void onEnter(Object data);

    void onLeave();

    void onBack();

    void onBackWithData(Object data);

    /**
     * process the return back logic
     * return true if back pressed event has been processed and should stay in current view
     *
     * @return
     */
    boolean processBackPressed();
}
