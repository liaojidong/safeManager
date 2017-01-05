package com.dong.mobilesafe.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SlideViewPager extends LazyViewPager {

    public SlideViewPager(Context context) {
        super(context);
    }

    public SlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (getCurrentItem() != 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {

        if (getCurrentItem() != 0) {
            super.onTouchEvent(arg0);
            return true;
        }
        return super.onTouchEvent(arg0);
    }

}
