package com.dong.mobilesafe.utils;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/2/25 0025.
 */
public class AnimationUtils {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static MyRunnable mr;
    private static AnimationDrawable anim;
    public interface OnAnimationStopListener {
        public void onStop(View view);
    }


    /**
     * 播放帧动画
     * @param animId
     * @param view
     *
     */
    public static void playFrameAnim(int animId,ImageView view) {
        view.setImageResource(animId);
        anim = (AnimationDrawable) view.getDrawable();
        anim.start();
    }


    /**
     * 播放帧动画
     * @param animId
     * @param view
     *
     */
    public static void playFrameAnim(int animId,final ImageView view, final OnAnimationStopListener listener,int duration) {
        view.setImageResource(animId);
        anim = (AnimationDrawable) view.getDrawable();
        mr = new MyRunnable(view,listener);
        anim.start();
        mHandler.postDelayed(mr,duration);
    }

    public static class MyRunnable implements Runnable{

        private OnAnimationStopListener listener;
        private ImageView view;
        public MyRunnable(ImageView view,OnAnimationStopListener listener){
            this.listener = listener;
            this.view = view;
        }
        @Override
        public void run() {
            anim.stop();
            if(listener != null) {
                listener.onStop(view);
            }
        }
    }

    public static void startAnim(){
        if(anim != null){
            anim.start();
        }
    }

    public static void stopAnim(){
        if(mr != null){
            mHandler.removeCallbacks(mr);
        }

    }

    public static boolean isRunning(){
        if(anim != null){
            return anim.isRunning();
        }
        return false;
    }




}
