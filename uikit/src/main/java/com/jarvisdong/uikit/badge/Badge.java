package com.jarvisdong.uikit.badge;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public interface Badge {

    Badge setBadgeNumber(int badgeNum);

    int getBadgeNumber();

    Badge setBadgeText(String badgeText);

    String getBadgeText();

    Badge setExactMode(boolean isExact);

    boolean isExactMode();

    Badge setShowShadow(boolean showShadow);

    boolean isShowShadow();

    Badge setBadgeBackgroundColor(int color);

    Badge stroke(int color, float width, boolean isDpValue);

    int getBadgeBackgroundColor();

    Badge setBadgeBackground(Drawable drawable);

    Badge setBadgeBackground(Drawable drawable, boolean clip);

    Drawable getBadgeBackground();

    Badge setBadgeTextColor(int color);

    int getBadgeTextColor();

    Badge setBadgeTextSize(float size, boolean isSpValue);

    float getBadgeTextSize(boolean isSpValue);

    Badge setBadgePadding(float padding, boolean isDpValue);

    float getBadgePadding(boolean isDpValue);

    boolean isDraggable();

    Badge setBadgeGravity(int gravity);

    int getBadgeGravity();

    Badge setGravityOffset(float offset, boolean isDpValue);

    Badge setGravityOffset(float offsetX, float offsetY, boolean isDpValue);

    float getGravityOffsetX(boolean isDpValue);

    float getGravityOffsetY(boolean isDpValue);

    Badge setOnDragStateChangedListener(OnDragStateChangedListener l);

    float getBadgeWidth();
    float getBadgeHeight();

    /**
     * 获取触摸位置;
     * @return
     */
    PointF getDragCenter();

    /**
     * 修饰的目标;
     * @param view
     * @return
     */
    Badge bindTarget(View view);

    /**
     * 获取修饰的内容;
     * @return
     */
    View getTargetView();

    void hide(boolean animate);

    interface OnDragStateChangedListener {
        /**
         * 1:down事件的状态;
         * 2:正在拖拽中,且没有超出指定拖拽范围;
         * 3:拖拽超出指定距离
         * 4:未拖出,返回reset;
         * 5:拖出,成功,播放动画;
         */
        int STATE_START = 1;
        int STATE_DRAGGING = 2;
        int STATE_DRAGGING_OUT_OF_RANGE = 3;
        int STATE_CANCELED = 4;
        int STATE_SUCCEED = 5;

        void onDragStateChanged(int dragState, Badge badge, View targetView);
    }
}
