package com.jarvisdong.uikit.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by JarvisDong on 2017/7/26.
 * OverView: 动画工具类;
 */

public class AnimationUtils {

    public static long lasttime = 0;

    //去除双点击;
    public static boolean canDoubleClick() {
        long currenttime = System.currentTimeMillis();
        if (currenttime - lasttime > 1000) {
            lasttime = currenttime;
            return true;
        } else {
            return false;
        }
    }

    //去除双点击;
    public static boolean canDoubleClick(long settingMsTime) {
        long currenttime = System.currentTimeMillis();
        if (currenttime - lasttime > settingMsTime) {
            lasttime = currenttime;
            return true;
        } else {
            return false;
        }
    }

    /**
     * homeactivity
     * 设置任务弹出动画;
     */
    public static void showAnimation(Context mContext, final View view, long delayms) {
        view.setVisibility(View.INVISIBLE);
        ObjectAnimator leftAnimator1 = ObjectAnimator.ofFloat(view, "translationY", ScreenUtils.getScreenHeight(), view.getHeight());
        leftAnimator1.setStartDelay(delayms);
        leftAnimator1.setDuration(1000);
        leftAnimator1.setInterpolator(new AnticipateOvershootInterpolator());
        leftAnimator1.start();
        leftAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * homeactivity
     * 任务旋转动画;
     *
     * @param view
     * @param degree
     */
    public static void rotateAnimation(View view, int degree, int enddegree) {
//        RotateAnimation fabRotate = new RotateAnimation(degree, enddegree, view.getPivotX(), view.getPivotY());
        RotateAnimation fabRotate = new RotateAnimation(degree, enddegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fabRotate.setDuration(500);
        fabRotate.setFillAfter(true);
        view.startAnimation(fabRotate);
    }


    public static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View mTarget) {
            this.mTarget = mTarget;
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }
    }
}
