package com.dong.mobilesafe.ui;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dong.mobilesafe.utils.DensityUtil;

import java.util.Random;

/**
 * Created by liaojd on 2016/7/4.
 */
public class AnimRelativeLayout extends RelativeLayout {

    public AnimRelativeLayout(Context context) {
        super(context);
    }

    public AnimRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void startImageView(final ImageView view) {
        LayoutParams params = new LayoutParams(DensityUtil.dip2px(getContext(),30), DensityUtil.dip2px(getContext(),30));
        view.setX(genRandomX(view));
        view.setY(genRandomY(view));
        this.addView(view, params);
        view.animate()
                .alpha(0.2f)
                .translationX(getWidth() / 2f)
                .translationY(getHeight() / 2f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        removeView(view);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).start();
    }

    private int genRandomX(View view) {
        Random random = new Random();
        return random.nextInt(getWidth() - view.getWidth());
    }

    private int genRandomY(View view) {
        Random random = new Random();
        return random.nextInt(getHeight() - view.getHeight());
    }

}
