package com.jarvisdong.uikit.badge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.jarvisdong.uikit.R;

import java.lang.ref.WeakReference;

/**
 * Created by JarvisDong on 2018/3/26.
 * OverView:
 */

public class BadgeFrameAnimator {
    private static final int EXPLOSION_ANIM_FRAME_INTERVAL = 50;
    private Bitmap[] explosionAnim; // 爆裂动画位图
    private boolean explosionAnimStart; // 爆裂动画是否开始
    private int explosionAnimNumber; // 爆裂动画帧的个数
    private int curExplosionAnimIndex; // 爆裂动画当前帧
    private int explosionAnimWidth; // 爆裂动画帧的宽度
    private int explosionAnimHeight; // 爆裂动画帧的高度
    private int[] explosionResIds = new int[]{
            R.drawable.explosion_one,
            R.drawable.explosion_two,
            R.drawable.explosion_three,
            R.drawable.explosion_four,
            R.drawable.explosion_five
    };

    private WeakReference<QBadgeView> mWeakBadge;

    public BadgeFrameAnimator(QBadgeView badge) {
        mWeakBadge = new WeakReference<>(badge);
    }

    public boolean isExplosionAnimStart() {
        return explosionAnimStart;
    }

    public void setExplosionAnimStart(boolean explosionAnimStart) {
        this.explosionAnimStart = explosionAnimStart;
    }

    /**
     * ************************* 爆炸动画(帧动画) *************************
     */
    public void initExplosionAnimation() {
        if (explosionAnim == null && mWeakBadge != null) {
            QBadgeView badgeView = mWeakBadge.get();
            explosionAnimNumber = explosionResIds.length;
            explosionAnim = new Bitmap[explosionAnimNumber];
            for (int i = 0; i < explosionAnimNumber; i++) {
                explosionAnim[i] = BitmapFactory.decodeResource(badgeView.getContext().getResources(), explosionResIds[i]);
            }
            explosionAnimHeight = explosionAnimWidth = explosionAnim[0].getWidth(); // 每帧长宽都一致
        }
    }


    public void drawExplosionAnimation(Canvas canvas, PointF mTouchPoint) {
        QBadgeView badgeView = mWeakBadge.get();
        if (badgeView == null || !badgeView.isShown() || !explosionAnimStart || explosionAnim==null) {
            return;
        }

        if (curExplosionAnimIndex < explosionAnimNumber) {
            canvas.drawBitmap(explosionAnim[curExplosionAnimIndex],
                    mTouchPoint.x - explosionAnimWidth / 2, mTouchPoint.y - explosionAnimHeight / 2, null);
            curExplosionAnimIndex++;
            // 每隔固定时间执行
            badgeView.postInvalidateDelayed(EXPLOSION_ANIM_FRAME_INTERVAL);
        } else {
            // 动画结束
            explosionAnimStart = false;
            curExplosionAnimIndex = 0;
            if (badgeView != null) {
                badgeView.resetUp();
            }
        }
    }

    public void recycleBitmap() {
        if (explosionAnim != null && explosionAnim.length != 0) {
            for (int i = 0; i < explosionAnim.length; i++) {
                if (explosionAnim[i] != null && !explosionAnim[i].isRecycled()) {
                    explosionAnim[i].recycle();
                    explosionAnim[i] = null;
                }
            }
            explosionAnim = null;
        }
    }


}
