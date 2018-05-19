package com.jarvisdong.uikit.baseui.manager;

/**
 * Created by JarvisDong on 2017/10/30.
 * OverView: client 生命周期管理类;
 */

public interface LifeCycleComponent {
    /**
     * The UI becomes partially invisible.
     * like {@link android.app.Activity#onPause}
     */
    public void onBecomesPartiallyInvisible();

    /**
     * The UI becomes visible from partially or totally invisible.
     * like {@link android.app.Activity#onResume}
     */
    public void onBecomesVisible();

    /**
     * The UI becomes totally invisible.
     * like {@link android.app.Activity#onStop}
     */
    public void onBecomesTotallyInvisible();

    /**
     * The UI becomes visible from totally invisible.
     * like {@link android.app.Activity#onRestart}
     */
    public void onBecomesVisibleFromTotallyInvisible();

    /**
     * like {@link android.app.Activity#onDestroy}
     */
    public void onDestroy();
}
