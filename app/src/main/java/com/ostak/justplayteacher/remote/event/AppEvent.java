package com.ostak.justplayteacher.remote.event;

/**
 * Created by alex on 16/8/20.
 */
public class AppEvent {
    public static enum EVENT_TYPE {
        // 用户注销
        EVENT_LOGOUT,
        // API返回token error
        EVENT_TOKEN_ERROR,
    }

    public EVENT_TYPE type;
    public Object content;

    public AppEvent(EVENT_TYPE type, Object content) {
        this.type = type;
        this.content = content;
    }
}
