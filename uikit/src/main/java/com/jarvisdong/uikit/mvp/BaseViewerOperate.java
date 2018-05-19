package com.jarvisdong.uikit.mvp;

/**
 * Created by JarvisDong on 2018/1/31.
 * OverView:base VIEW 层
 */

public interface BaseViewerOperate<T extends BasePresenterOperate, E, W> {
    /**
     * 将view层和persenter层建立联系;
     *
     * @param presenter
     */
    void setPresenter(T presenter);


    /**
     * 将persenter (model)的动作发送到view层执行;
     * 统一管理发送
     *
     * @param mActionData
     */
    void fillView(VMessage<E> mActionData);

    /**
     * 将view层的数据收集到persenter
     * 统一管理发送
     *
     * @return
     */
    VMessage<W> fetchView();
}
