package com.dong.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.dong.mobilesafe.base.BaseActivity;


/**
 * @author dyh
 */
public class SlidFinishLayout extends RelativeLayout {

    /**
     * SildingFinishLayout布局的父布局
     */
    private ViewGroup mParentView;
    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private int downX;
    /**
     * 按下点的Y坐标
     */
    private int downY;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * SildingFinishLayout的宽度
     */
    private int viewWidth;

    private boolean isSilding;

    private OnSildingFinishListener onSildingFinishListener;
    private boolean isFinish;

    private boolean isEnableSlidFinish = true;
    private BaseActivity mActivity;

    // 手指滑动时的最小速度
    private int mMinimumVelocity;

    // 手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 150;


    // 记录手指移动时的横坐标。
    private float moveX;
    private float moveY;

    // 用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    private boolean isFinished = false;

    public SlidFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSlidFinishLayout(context);
    }

    private void initSlidFinishLayout(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mScroller = new Scroller(context);
    }

    public void setEnableSlidFinish(boolean isEnableSlidFinish) {
        this.isEnableSlidFinish = isEnableSlidFinish;
    }

    public void setActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * @param isFinished the isFinished to set
     */
    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mActivity == null || !isEnableSlidFinish)
            return super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                int moveY = (int) ev.getRawY();
                // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if ((moveX - downX) > mTouchSlop
                        && Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFinished)
            return false;
        if (mActivity == null || !isEnableSlidFinish)
            return false;

        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getRawX();
                moveY = event.getRawY();
                // 向右滑动的距离
                int deltaX = (int) (moveX - downX);
                int deltaY = (int) Math.abs(moveY - downY);
                // 获取顺时速度
                int xSpeed = getScrollVelocity();
                // 当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                if (deltaX > XDISTANCE_MIN && xSpeed > mMinimumVelocity && deltaX > deltaY) {
                    if (moveX < downX) {
                        return true; //结束
                    }
                    isFinished = true;
                    mActivity.finish();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                downX = 0;
                downY = 0;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);

		/*
         * if(!isEnableSlidFinish) return false; switch (event.getAction()) {
		 * case MotionEvent.ACTION_MOVE: int moveX = (int) event.getRawX(); int
		 * deltaX = tempX - moveX; tempX = moveX; if (Math.abs(moveX - downX) >
		 * mTouchSlop && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
		 * isSilding = true; }
		 * 
		 * if (moveX - downX >= 0 && isSilding) { mParentView.scrollBy(deltaX,
		 * 0); } break; case MotionEvent.ACTION_UP: isSilding = false; if
		 * (mParentView.getScrollX() <= -viewWidth / 2) { isFinish = true;
		 * scrollRight(); } else { scrollOrigin(); isFinish = false; } break; }
		 * 
		 * return true;
		 */
    }

    // @Override
    // protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // super.onLayout(changed, l, t, r, b);
    // if (changed) {
    // // 获取SildingFinishLayout所在布局的父布局
    // mParentView = (ViewGroup) this.getParent();
    // viewWidth = this.getWidth();
    // }
    // }
    //
    // /**
    // * 设置OnSildingFinishListener, 在onSildingFinish()方法中finish Activity
    // *
    // * @param onSildingFinishListener
    // */
    // public void setOnSildingFinishListener(
    // OnSildingFinishListener onSildingFinishListener) {
    // this.onSildingFinishListener = onSildingFinishListener;
    // }
    //
    // /**
    // * 滚动出界面
    // */
    // private void scrollRight() {
    // final int delta = (viewWidth + mParentView.getScrollX());
    // // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
    // mScroller.startScroll(mParentView.getScrollX(), 0, -delta + 1, 0,
    // Math.abs(delta));
    // postInvalidate();
    // }
    //
    // /**
    // * 滚动到起始位置
    // */
    // private void scrollOrigin() {
    // int delta = mParentView.getScrollX();
    // mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0,
    // Math.abs(delta));
    // postInvalidate();
    // }
    //
    // @Override
    // public void computeScroll() {
    // // 调用startScroll的时候scroller.computeScrollOffset()返回true，
    // if (mScroller.computeScrollOffset()) {
    // mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
    // postInvalidate();
    //
    // if (mScroller.isFinished() && isFinish) {
    //
    // if (onSildingFinishListener != null) {
    // onSildingFinishListener.onSildingFinish();
    // } else {
    // // 没有设置OnSildingFinishListener，让其滚动到其实位置
    // scrollOrigin();
    // isFinish = false;
    // }
    // }
    // }
    // }

    public interface OnSildingFinishListener {
        public void onSildingFinish();
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
}
