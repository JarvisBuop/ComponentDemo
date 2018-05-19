package com.jarvisdong.uikit.adapter.itemanager;


/**
 * 根据itemtype的每个item的接口;
 *
 */
public interface ItemViewDelegate<T>
{
    /**
     * 获取每个item的布局;
     * @return
     */
    int getItemViewLayoutId();

    /**
     * 选择此item的条件;对象控制;
     */
    boolean isForViewType(T item, int position);

    /**
     * 填充每个item设置
     */
    void convert(ViewHolder holder, T t, int position);

}
