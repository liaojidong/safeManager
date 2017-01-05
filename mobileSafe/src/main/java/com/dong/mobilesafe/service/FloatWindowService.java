package com.dong.mobilesafe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.ui.trafficFloatToast;
import com.dong.mobilesafe.utils.log.LogUtils;

public class FloatWindowService extends BaseService {
    private static final int STATE_UPDATE_SPEED = 1;
    private trafficFloatToast floatToast;
    private long speed;
    private long currentRx;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isFinish;
    private ScreenChangeReceiver screenReceiver;
    @Override
    protected void initParams() {
        super.initParams();

        screenReceiver = new ScreenChangeReceiver();
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        isFinish = false;
        floatToast = new trafficFloatToast(this, R.layout.toast_traffic);
        floatToast.showToast();
        currentRx = TrafficStats.getTotalRxBytes();
        new SpeedMonitorThread().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    private Object lock = new Object();
    private boolean isScreenOn = true;

    private class ScreenChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            synchronized (lock) {
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    LogUtils.jLog().e("屏幕锁屏了。。。");
                    isScreenOn = false;
                } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    LogUtils.jLog().e("屏幕亮了。。。");
                    isScreenOn = true;
                    lock.notifyAll();
                }
            }
        }
    }


    /**
     * 计算当前网速
     */
    class SpeedMonitorThread extends Thread {
        @Override
        public void run() {
            while (!isFinish) {
                synchronized (lock) {
                    while (!isScreenOn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    speed = (TrafficStats.getTotalRxBytes() - currentRx) / 2;
                    if (speed <= 0) {
                        continue;
                    }
                    if (speed < 1024 && speed > 0) {
                        speed = 0;
                    }
                    currentRx = TrafficStats.getTotalRxBytes();
                    final String strSpeed = (speed == 0 ? speed + "Kb" : Formatter.formatShortFileSize(FloatWindowService.this, speed))+"/s";
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            floatToast.changeContent(strSpeed);
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        isFinish = true;
        if (floatToast != null) {
            floatToast.hideToast();
        }
    }

}
