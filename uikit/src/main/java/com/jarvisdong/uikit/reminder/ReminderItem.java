package com.jarvisdong.uikit.reminder;

import java.io.Serializable;

public class ReminderItem implements Serializable {
    private static final long serialVersionUID = -2101649256143239157L;
    protected final int id;//即设置的唯一tagid,int类型,获取相应的tab,改变数据;
    private int unread;
    private boolean indicator;

    public ReminderItem(int id) {
        this.id = id;
        this.unread = 0;
    }

    public int getId() {
        return id;
    }

    public int unread() {
        return unread;
    }

    public boolean indicator() {
        return indicator;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    public ReminderItem copy() {
        ReminderItem item = new ReminderItem(id);
        copyData(item);
        return item;
    }

    protected void copyData(ReminderItem item) {
        item.unread = this.unread;
        item.indicator = this.indicator;
    }
}
