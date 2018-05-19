package com.jarvisdong.uikit.badge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jarvisdong.uikit.util.ScreenUtils.dp2px;
import static com.jarvisdong.uikit.util.ScreenUtils.px2dp;


/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class QBadgeView extends View implements Badge {
    //style setting
    protected int mColorBackground;
    protected int mColorBadgeText;
    protected Drawable mDrawableBackground;
    protected float mBadgeTextSize;
    protected float mBadgePadding;
    //border
    protected int mColorBackgroundBorder;
    protected float mBackgroundBorderWidth;
    //msg
    protected int mBadgeNumber;
    protected String mBadgeText;
    //clip
    protected Bitmap mBitmapClip;
    protected boolean mDrawableBackgroundClip;

    protected boolean mDraggable;//是否可拖拽;
    protected boolean mDragging;//是否正在拖拽;
    protected boolean mExact;
    protected int mBadgeGravity;
    protected float mGravityOffsetX;
    protected float mGravityOffsetY;

    protected float mDefalutRadius;//拖拽中变小的圆;
    protected float mFinalDragDistance;//最大拖拽距离;
    protected boolean mDragOutOfRange;//是否超出最大距离;
    private boolean isFirstOutRange = false;
    protected float mOorThreshold = 1.5f;
    protected float mDefaultDraggingRadius;//拖动的圆大小;
    //阴影;
    protected boolean mShowShadow;
    protected int mDragQuadrant;

    protected RectF mBadgeTextRect;
    protected RectF mBadgeBackgroundRect;
    protected Path mDragPath;

    protected Paint.FontMetrics mBadgeTextFontMetrics;

    protected PointF mBadgeCenter;//view的中心;
    protected PointF mRowBadgeCenter;//view中心加屏幕位置的坐标;
    protected PointF mDragCenter;//android坐标系 ,记录触摸位置;
    protected PointF mControlPoint;

    protected List<PointF> mInnertangentPoints;

    protected View mTargetView;

    protected int mWidth;
    protected int mHeight;
    //画笔;
    protected TextPaint mBadgeTextPaint;
    protected Paint mBadgeBackgroundPaint;
    protected Paint mBadgeBackgroundBorderPaint;

    protected BadgeFrameAnimator mFrameAnimator;

    protected OnDragStateChangedListener mDragStateChangedListener;

    protected ViewGroup mActivityRoot;
    private boolean useEndAnim = true;

    public QBadgeView(Context context) {
        this(context, null);
    }

    private QBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private QBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBadgeTextRect = new RectF();
        mBadgeBackgroundRect = new RectF();
        mDragPath = new Path();
        mBadgeCenter = new PointF();
        mDragCenter = new PointF();
        mRowBadgeCenter = new PointF();
        mControlPoint = new PointF();
        mInnertangentPoints = new ArrayList<>();

        mBadgeTextPaint = new TextPaint();
        mBadgeTextPaint.setAntiAlias(true);
        mBadgeTextPaint.setSubpixelText(true);//lcd效果;
        mBadgeTextPaint.setFakeBoldText(true);//文本仿粗体;
        mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mBadgeBackgroundPaint = new Paint();
        mBadgeBackgroundPaint.setAntiAlias(true);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);

        mBadgeBackgroundBorderPaint = new Paint();
        mBadgeBackgroundBorderPaint.setAntiAlias(true);
        mBadgeBackgroundBorderPaint.setStyle(Paint.Style.STROKE);

        mColorBackground = 0xFFE84E40;
        mColorBadgeText = 0xFFFFFFFF;
        mBadgeTextSize = dp2px(getContext(), 10);
        mBadgePadding = dp2px(getContext(), 4);
        mBadgeNumber = 0;
        mBadgeGravity = Gravity.END | Gravity.TOP;
        mGravityOffsetX = dp2px(getContext(), 1);
        mGravityOffsetY = dp2px(getContext(), 1);
        mFinalDragDistance = dp2px(getContext(), 90);
        mShowShadow = true;
        mDrawableBackgroundClip = false;
        mDefalutRadius = dp2px(getContext(), 7);
        mDefaultDraggingRadius = dp2px(getContext(),5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslationZ(1000);
        }
    }

    @Override
    public Badge bindTarget(final View targetView) {
        if (targetView == null) {
            throw new IllegalStateException("targetView can not be null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent targetParent = targetView.getParent();
        if (targetParent != null && targetParent instanceof ViewGroup) {
            mTargetView = targetView;
            if (targetParent instanceof BadgeContainer) {
                ((BadgeContainer) targetParent).addView(this);
            } else {
                // TODO: 2018/3/25 用BadgeContainer 替换掉 targetView;
                // todo BadgeContainer包含targetView和 QBadgeView;
                ViewGroup targetContainer = (ViewGroup) targetParent;
                int index = targetContainer.indexOfChild(targetView);
                ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
                targetContainer.removeView(targetView);
                final BadgeContainer badgeContainer = new BadgeContainer(getContext());
                if (targetContainer instanceof RelativeLayout) {
                    badgeContainer.setId(targetView.getId());
                }
                targetContainer.addView(badgeContainer, index, targetParams);
                badgeContainer.addView(targetView);
                badgeContainer.addView(this);
            }
        } else {
            throw new IllegalStateException("targetView must have a parent");
        }
        return this;
    }

    @Override
    public View getTargetView() {
        return mTargetView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mActivityRoot == null) findViewRoot(mTargetView);
    }

    private void findViewRoot(View view) {
        mActivityRoot = (ViewGroup) view.getRootView();
        if (mActivityRoot == null) {
            findActivityRoot(view);
        }
    }

    private void findActivityRoot(View view) {
        if (view.getParent() != null && view.getParent() instanceof View) {
            findActivityRoot((View) view.getParent());
        } else if (view instanceof ViewGroup) {
            mActivityRoot = (ViewGroup) view;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN://多点检测;
                float x = event.getX();
                float y = event.getY();
                if (mDraggable && event.getPointerId(event.getActionIndex()) == 0
                        && (x > mBadgeBackgroundRect.left && x < mBadgeBackgroundRect.right &&
                        y > mBadgeBackgroundRect.top && y < mBadgeBackgroundRect.bottom)
                        && mBadgeText != null) {
                    initRowBadgeCenter();
                    mDragging = true;
                    updataListener(OnDragStateChangedListener.STATE_START);
                    getParent().requestDisallowInterceptTouchEvent(true);//父view不允许拦截;
                    screenFromWindow(true);
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (event.getPointerId(event.getActionIndex()) == 0 && mDragging) {
                    onPointerUp();
                }
                break;
        }
        return mDragging || super.onTouchEvent(event);
    }

    private void onPointerUp() {
        if (mDragOutOfRange) {
            animateHide(mDragCenter);
            updataListener(OnDragStateChangedListener.STATE_SUCCEED);
        } else {
            if (useEndAnim && !isFirstOutRange) {
                startSpringAnim();
            } else {
                resetUp();
            }
            updataListener(OnDragStateChangedListener.STATE_CANCELED);
        }
    }

    private void startSpringAnim() {
        ValueAnimator springAnim = ValueAnimator.ofFloat(1.0f);
        final PointF startPoint = new PointF(mDragCenter.x, mDragCenter.y);
        final PointF endPoint = new PointF(mRowBadgeCenter.x, mRowBadgeCenter.y);
        LogUtils.e("endPoint:" + endPoint.toString() + "/startPoint:" + startPoint.toString());
        springAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator mAnim) {
                // 0.0 -> 1.0f
                float percent = mAnim.getAnimatedFraction();
                PointF p = new PointF(evaluate(percent, startPoint.x, endPoint.x),
                        evaluate(percent, startPoint.y, endPoint.y));
                mDragCenter.set(p.x, p.y);
                postInvalidate();
            }
        });
        springAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                resetUp();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                resetUp();
            }
        });
        springAnim.setInterpolator(new OvershootInterpolator(5));
        springAnim.setDuration(200);
        springAnim.start();
    }

    public static Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * down->加入到根布局中或者加入到指定容器;
     * 模拟全屏拉动,即是加在avtivity根布局中;
     *
     * @param screen
     */
    protected void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            bindTarget(mTargetView);
        }
    }

    // TODO: 2018/3/25 处理阴影;
    private void showShadowImpl(boolean showShadow) {
        int x = dp2px(getContext(), 1);
        int y = dp2px(getContext(), 1.5f);
        switch (mDragQuadrant) {
            case 1:
                x = dp2px(getContext(), 1);
                y = dp2px(getContext(), -1.5f);
                break;
            case 2:
                x = dp2px(getContext(), -1);
                y = dp2px(getContext(), -1.5f);
                break;
            case 3:
                x = dp2px(getContext(), -1);
                y = dp2px(getContext(), 1.5f);
                break;
            case 4:
                x = dp2px(getContext(), 1);
                y = dp2px(getContext(), 1.5f);
                break;
        }
        mBadgeBackgroundPaint.setShadowLayer(showShadow ? dp2px(getContext(), 2f)
                : 0, x, y, 0x33000000);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFrameAnimator != null && mFrameAnimator.isExplosionAnimStart()) {
            mFrameAnimator.drawExplosionAnimation(canvas,mDragCenter);
            return;
        }
        if (mBadgeText != null) {
            initPaints();
            float badgeRadius = getBadgeCircleRadius();
            float startCircleRadius = mDefalutRadius * (1 - MathUtil.getPointDistance
                    (mRowBadgeCenter, mDragCenter) / mFinalDragDistance);
            if (mDraggable && mDragging) {
                mDragQuadrant = MathUtil.getQuadrant(mDragCenter, mRowBadgeCenter);
                showShadowImpl(mShowShadow);
                mDragOutOfRange = startCircleRadius < dp2px(getContext(), mOorThreshold);
                if (mDragOutOfRange) {
                    isFirstOutRange = true;
                    updataListener(OnDragStateChangedListener.STATE_DRAGGING_OUT_OF_RANGE);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                } else {
                    updataListener(OnDragStateChangedListener.STATE_DRAGGING);
                    if (!isFirstOutRange) {
                        drawDragging(canvas, startCircleRadius, badgeRadius);
                    }
                    drawBadge(canvas, mDragCenter, badgeRadius);
                }
            } else {
                findBadgeCenter();
                drawBadge(canvas, mBadgeCenter, badgeRadius);
            }
        }
    }

    private void initPaints() {
        showShadowImpl(mShowShadow);
        mBadgeBackgroundPaint.setColor(mColorBackground);
        mBadgeBackgroundBorderPaint.setColor(mColorBackgroundBorder);
        mBadgeBackgroundBorderPaint.setStrokeWidth(mBackgroundBorderWidth);
        mBadgeTextPaint.setColor(mColorBadgeText);
        mBadgeTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 绘制路径;
     *
     * @param canvas
     * @param startRadius 原位置圆的半径;
     * @param badgeRadius 绘制圆的半径;
     */
    private void drawDragging(Canvas canvas, float startRadius, float badgeRadius) {
        float dy = mDragCenter.y - mRowBadgeCenter.y;//触摸位置距离view中心位置,变化位置;
        float dx = mDragCenter.x - mRowBadgeCenter.x;
        mInnertangentPoints.clear();
        // TODO: 2018/3/25 算四个外切点;
        if (dx != 0) {
            double k1 = dy / dx;
            double k2 = -1 / k1;
            MathUtil.getInnertangentPoints(mDragCenter, mDefaultDraggingRadius, k2, mInnertangentPoints);
            MathUtil.getInnertangentPoints(mRowBadgeCenter, startRadius, k2, mInnertangentPoints);
        } else {
            MathUtil.getInnertangentPoints(mDragCenter, badgeRadius, 0d, mInnertangentPoints);
            MathUtil.getInnertangentPoints(mRowBadgeCenter, startRadius, 0d, mInnertangentPoints);
        }
        mDragPath.reset();
        mDragPath.addCircle(mRowBadgeCenter.x, mRowBadgeCenter.y, startRadius,
                mDragQuadrant == 1 || mDragQuadrant == 2 ? Path.Direction.CCW : Path.Direction.CW);
        // TODO: 2018/3/25 触摸位置和原view位置的中心为控制点;
        mControlPoint.x = (mRowBadgeCenter.x + mDragCenter.x) / 2.0f;
        mControlPoint.y = (mRowBadgeCenter.y + mDragCenter.y) / 2.0f;
        mDragPath.moveTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(0).x, mInnertangentPoints.get(0).y);
        mDragPath.lineTo(mInnertangentPoints.get(1).x, mInnertangentPoints.get(1).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(3).x, mInnertangentPoints.get(3).y);
        mDragPath.lineTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.close();
        canvas.drawPath(mDragPath, mBadgeBackgroundPaint);

        //draw dragging border
        if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
            mDragPath.reset();
            mDragPath.moveTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
            mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(0).x, mInnertangentPoints.get(0).y);
            mDragPath.moveTo(mInnertangentPoints.get(1).x, mInnertangentPoints.get(1).y);
            mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(3).x, mInnertangentPoints.get(3).y);
            // TODO: 2018/3/25 另一边用弧代替;
            float startY;
            float startX;
            if (mDragQuadrant == 1 || mDragQuadrant == 2) {
                startX = mInnertangentPoints.get(2).x - mRowBadgeCenter.x;
                startY = mRowBadgeCenter.y - mInnertangentPoints.get(2).y;
            } else {
                startX = mInnertangentPoints.get(3).x - mRowBadgeCenter.x;
                startY = mRowBadgeCenter.y - mInnertangentPoints.get(3).y;
            }
            float startAngle = 360 - (float) MathUtil.radianToAngle(MathUtil.getTanRadian(Math.atan(startY / startX),
                    mDragQuadrant - 1 == 0 ? 4 : mDragQuadrant - 1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mDragPath.addArc(mRowBadgeCenter.x - startRadius, mRowBadgeCenter.y - startRadius,
                        mRowBadgeCenter.x + startRadius, mRowBadgeCenter.y + startRadius, startAngle,
                        180);
            } else {
                mDragPath.addArc(new RectF(mRowBadgeCenter.x - startRadius, mRowBadgeCenter.y - startRadius,
                        mRowBadgeCenter.x + startRadius, mRowBadgeCenter.y + startRadius), startAngle, 180);
            }
            canvas.drawPath(mDragPath, mBadgeBackgroundBorderPaint);
        }
    }

    /**
     * 有无背景,是否画圆还是圆角矩形,在画文字;
     *
     * @param canvas 画布;
     * @param center 触摸位置;
     * @param radius 绘制圆半径
     */
    private void drawBadge(Canvas canvas, PointF center, float radius) {
        if (center.x == -1000 && center.y == -1000) {
            return;
        }
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBadgeBackgroundRect.left = center.x - (int) radius;
            mBadgeBackgroundRect.top = center.y - (int) radius;
            mBadgeBackgroundRect.right = center.x + (int) radius;
            mBadgeBackgroundRect.bottom = center.y + (int) radius;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundBorderPaint);
                }
            }
        } else {
            mBadgeBackgroundRect.left = center.x - (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.top = center.y - (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            mBadgeBackgroundRect.right = center.x + (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.bottom = center.y + (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            radius = mBadgeBackgroundRect.height() / 2f;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundBorderPaint);
                }
            }
        }
        if (!mBadgeText.isEmpty()) {
            canvas.drawText(mBadgeText, center.x,
                    (mBadgeBackgroundRect.bottom + mBadgeBackgroundRect.top
                            - mBadgeTextFontMetrics.bottom - mBadgeTextFontMetrics.top) / 2f,
                    mBadgeTextPaint);
        }
    }

    /**
     * 画背景,裁切时画圆;
     *
     * @param canvas
     */
    private void drawBadgeBackground(Canvas canvas) {
        mBadgeBackgroundPaint.setShadowLayer(0, 0, 0, 0);
        int left = (int) mBadgeBackgroundRect.left;
        int top = (int) mBadgeBackgroundRect.top;
        int right = (int) mBadgeBackgroundRect.right;
        int bottom = (int) mBadgeBackgroundRect.bottom;
        if (mDrawableBackgroundClip) {
            right = left + mBitmapClip.getWidth();
            bottom = top + mBitmapClip.getHeight();
            canvas.saveLayer(left, top, right, bottom, null, Canvas.ALL_SAVE_FLAG);
        }
        mDrawableBackground.setBounds(left, top, right, bottom);
        mDrawableBackground.draw(canvas);
        if (mDrawableBackgroundClip) {
            //切背景;
            mBadgeBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mBitmapClip, left, top, mBadgeBackgroundPaint);
            canvas.restore();
            mBadgeBackgroundPaint.setXfermode(null);
            if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
                canvas.drawCircle(mBadgeBackgroundRect.centerX(), mBadgeBackgroundRect.centerY(),
                        mBadgeBackgroundRect.width() / 2f, mBadgeBackgroundBorderPaint);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect,
                        mBadgeBackgroundRect.height() / 2, mBadgeBackgroundRect.height() / 2,
                        mBadgeBackgroundBorderPaint);
            }
        } else {
            canvas.drawRect(mBadgeBackgroundRect, mBadgeBackgroundBorderPaint);
        }
    }

    private void createClipLayer() {
        if (mBadgeText == null) {
            return;
        }
        if (!mDrawableBackgroundClip) {
            return;
        }
        if (mBitmapClip != null && !mBitmapClip.isRecycled()) {
            mBitmapClip.recycle();
        }
        float radius = getBadgeCircleRadius();
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBitmapClip = Bitmap.createBitmap((int) radius * 2, (int) radius * 2,
                    Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            srcCanvas.drawCircle(srcCanvas.getWidth() / 2f, srcCanvas.getHeight() / 2f,
                    srcCanvas.getWidth() / 2f, mBadgeBackgroundPaint);
        } else {
            mBitmapClip = Bitmap.createBitmap((int) (mBadgeTextRect.width() + mBadgePadding * 2),
                    (int) (mBadgeTextRect.height() + mBadgePadding), Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                srcCanvas.drawRoundRect(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight(), srcCanvas.getHeight() / 2f,
                        srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            } else {
                srcCanvas.drawRoundRect(new RectF(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight()),
                        srcCanvas.getHeight() / 2f, srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            }
        }
    }

    // TODO: 2018/3/25 获取绘制圆的半径;
    private float getBadgeCircleRadius() {
        if (mBadgeText.isEmpty()) {
            return mBadgePadding;
        } else if (mBadgeText.length() == 1) {
            return mBadgeTextRect.height() > mBadgeTextRect.width() ?
                    mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f :
                    mBadgeTextRect.width() / 2f + mBadgePadding * 0.5f;
        } else {
            return mBadgeBackgroundRect.height() / 2f;
        }
    }

    private void findBadgeCenter() {
        float rectWidth = mBadgeTextRect.height() > mBadgeTextRect.width() ?
                mBadgeTextRect.height() : mBadgeTextRect.width();
        switch (mBadgeGravity) {
            case Gravity.START | Gravity.TOP:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.START | Gravity.BOTTOM:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.END | Gravity.TOP:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.END | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.TOP:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.CENTER | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER | Gravity.START:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.END:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight / 2f;
                break;
        }
        initRowBadgeCenter();
    }

    private void measureText() {
        mBadgeTextRect.left = 0;
        mBadgeTextRect.top = 0;
        if (TextUtils.isEmpty(mBadgeText)) {
            mBadgeTextRect.right = 0;
            mBadgeTextRect.bottom = 0;
        } else {
            mBadgeTextPaint.setTextSize(mBadgeTextSize);
            mBadgeTextRect.right = mBadgeTextPaint.measureText(mBadgeText);
            mBadgeTextFontMetrics = mBadgeTextPaint.getFontMetrics();
            mBadgeTextRect.bottom = mBadgeTextFontMetrics.descent - mBadgeTextFontMetrics.ascent;
        }
        createClipLayer();
    }

    // TODO: 2018/3/25 获取badge的中心位置;
    private void initRowBadgeCenter() {
        int[] screenPoint = new int[2];
        getLocationOnScreen(screenPoint);
        mRowBadgeCenter.x = mBadgeCenter.x + screenPoint[0];
        mRowBadgeCenter.y = mBadgeCenter.y + screenPoint[1];
    }

    protected void animateHide(PointF center) {
        if (mBadgeText == null) {
            return;
        }
        if (mFrameAnimator == null || !mFrameAnimator.isExplosionAnimStart()) {
            screenFromWindow(true);
            mFrameAnimator = new BadgeFrameAnimator(this);
            mFrameAnimator.initExplosionAnimation();
            mFrameAnimator.setExplosionAnimStart(true);
            setBadgeNumber(0);
        }
    }

    public void resetUp() {
        isFirstOutRange = false;
        mDragging = false;
        mDragCenter.x = -1000;
        mDragCenter.y = -1000;
        mDragQuadrant = 4;
        screenFromWindow(false);
        getParent().requestDisallowInterceptTouchEvent(false);
        invalidate();
    }

    @Override
    public void hide(boolean animate) {
        if (animate && mActivityRoot != null) {
            initRowBadgeCenter();
            animateHide(mRowBadgeCenter);
        } else {
            setBadgeNumber(0);
        }
    }

    /**
     * @param badgeNumber equal to zero badge will be hidden, less than zero show dot
     */
    @Override
    public Badge setBadgeNumber(int badgeNumber) {
        mBadgeNumber = badgeNumber;
        if (mBadgeNumber < 0) {
            mBadgeText = "";
        } else if (mBadgeNumber > 99) {
            mBadgeText = mExact ? String.valueOf(mBadgeNumber) : "99+";
        } else if (mBadgeNumber > 0 && mBadgeNumber <= 99) {
            mBadgeText = String.valueOf(mBadgeNumber);
        } else if (mBadgeNumber == 0) {
            mBadgeText = null;
        }
        measureText();
        invalidate();
        return this;
    }

    @Override
    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    @Override
    public Badge setBadgeText(String badgeText) {
        mBadgeText = badgeText;
        mBadgeNumber = 1;
        measureText();
        invalidate();
        return this;
    }

    @Override
    public String getBadgeText() {
        return mBadgeText;
    }

    @Override
    public Badge setExactMode(boolean isExact) {
        mExact = isExact;
        if (mBadgeNumber > 99) {
            setBadgeNumber(mBadgeNumber);
        }
        return this;
    }

    @Override
    public boolean isExactMode() {
        return mExact;
    }

    @Override
    public Badge setShowShadow(boolean showShadow) {
        mShowShadow = showShadow;
        invalidate();
        return this;
    }

    @Override
    public boolean isShowShadow() {
        return mShowShadow;
    }

    @Override
    public Badge setBadgeBackgroundColor(int color) {
        mColorBackground = color;
        if (mColorBackground == Color.TRANSPARENT) {
            mBadgeTextPaint.setXfermode(null);
        } else {
            mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
        invalidate();
        return this;
    }

    @Override
    public Badge stroke(int color, float width, boolean isDpValue) {
        mColorBackgroundBorder = color;
        mBackgroundBorderWidth = isDpValue ? dp2px(getContext(), width) : width;
        invalidate();
        return this;
    }

    @Override
    public int getBadgeBackgroundColor() {
        return mColorBackground;
    }

    @Override
    public Badge setBadgeBackground(Drawable drawable) {
        return setBadgeBackground(drawable, false);
    }

    @Override
    public Badge setBadgeBackground(Drawable drawable, boolean clip) {
        mDrawableBackgroundClip = clip;
        mDrawableBackground = drawable;
        createClipLayer();
        invalidate();
        return this;
    }

    @Override
    public Drawable getBadgeBackground() {
        return mDrawableBackground;
    }

    @Override
    public Badge setBadgeTextColor(int color) {
        mColorBadgeText = color;
        invalidate();
        return this;
    }

    @Override
    public int getBadgeTextColor() {
        return mColorBadgeText;
    }

    @Override
    public Badge setBadgeTextSize(float size, boolean isSpValue) {
        mBadgeTextSize = isSpValue ? dp2px(getContext(), size) : size;
        measureText();
        invalidate();
        return this;
    }

    @Override
    public float getBadgeTextSize(boolean isSpValue) {
        return isSpValue ? px2dp(getContext(), mBadgeTextSize) : mBadgeTextSize;
    }

    @Override
    public Badge setBadgePadding(float padding, boolean isDpValue) {
        mBadgePadding = isDpValue ? dp2px(getContext(), padding) : padding;
        createClipLayer();
        invalidate();
        return this;
    }

    @Override
    public float getBadgePadding(boolean isDpValue) {
        return isDpValue ? px2dp(getContext(), mBadgePadding) : mBadgePadding;
    }

    @Override
    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM ,
     *                Gravity.CENTER , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ,
     *                Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END
     */
    @Override
    public Badge setBadgeGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER) ||
                gravity == (Gravity.CENTER | Gravity.TOP) ||
                gravity == (Gravity.CENTER | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER | Gravity.START) ||
                gravity == (Gravity.CENTER | Gravity.END)) {
            mBadgeGravity = gravity;
            invalidate();
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER" +
                    " , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ," +
                    "Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END");
        }
        return this;
    }

    @Override
    public int getBadgeGravity() {
        return mBadgeGravity;
    }

    @Override
    public Badge setGravityOffset(float offset, boolean isDpValue) {
        return setGravityOffset(offset, offset, isDpValue);
    }

    @Override
    public Badge setGravityOffset(float offsetX, float offsetY, boolean isDpValue) {
        mGravityOffsetX = isDpValue ? dp2px(getContext(), offsetX) : offsetX;
        mGravityOffsetY = isDpValue ? dp2px(getContext(), offsetY) : offsetY;
        invalidate();
        return this;
    }

    @Override
    public float getGravityOffsetX(boolean isDpValue) {
        return isDpValue ? px2dp(getContext(), mGravityOffsetX) : mGravityOffsetX;
    }

    @Override
    public float getGravityOffsetY(boolean isDpValue) {
        return isDpValue ? px2dp(getContext(), mGravityOffsetY) : mGravityOffsetY;
    }


    private void updataListener(int state) {
        if (mDragStateChangedListener != null)
            mDragStateChangedListener.onDragStateChanged(state, this, mTargetView);
    }

    @Override
    public Badge setOnDragStateChangedListener(OnDragStateChangedListener l) {
        mDraggable = l != null;
        mDragStateChangedListener = l;
        return this;
    }

    @Override
    public float getBadgeWidth() {
        return mBadgeTextRect.width() + mBadgePadding;
    }

    @Override
    public float getBadgeHeight() {
        return mBadgeTextRect.width() + mBadgePadding;
    }

    @Override
    public PointF getDragCenter() {
        if (mDraggable && mDragging) return mDragCenter;
        return null;
    }

    private class BadgeContainer extends ViewGroup {

        @Override
        protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
            if (!(getParent() instanceof RelativeLayout)) {
                super.dispatchRestoreInstanceState(container);
            }
        }

        public BadgeContainer(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            View targetView = null, badgeView = null;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!(child instanceof QBadgeView)) {
                    targetView = child;
                } else {
                    badgeView = child;
                }
            }
            if (targetView == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                targetView.measure(widthMeasureSpec, heightMeasureSpec);
                if (badgeView != null) {
                    badgeView.measure(MeasureSpec.makeMeasureSpec(targetView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(targetView.getMeasuredHeight(), MeasureSpec.EXACTLY));
                }
                setMeasuredDimension(targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mFrameAnimator!=null){
            mFrameAnimator.recycleBitmap();
        }
    }

//    protected Bitmap createBadgeBitmap() {
//        Bitmap bitmap = Bitmap.createBitmap((int) mBadgeBackgroundRect.width() + dp2px(getContext(), 3),
//                (int) mBadgeBackgroundRect.height() + dp2px(getContext(), 3), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawBadge(canvas, new PointF(canvas.getWidth() / 2f, canvas.getHeight() / 2f), getBadgeCircleRadius());
//        return bitmap;
//    }

}
