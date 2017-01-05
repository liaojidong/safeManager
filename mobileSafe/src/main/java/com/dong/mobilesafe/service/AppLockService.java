package com.dong.mobilesafe.service;

import java.util.List;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dong.mobilesafe.AppLockActivity;
import com.dong.mobilesafe.db.dao.ApplockDao;
import com.dong.mobilesafe.engine.TaskInfoProvider;
import com.dong.mobilesafe.utils.log.LogUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

/**
 * 程序锁服务
 */
public class AppLockService extends Service {
    private boolean flag;
    private ApplockDao dao;
    private InnerReceiver innerReceiver;
    private String tempStopProtectPackname;
    private ScreenChangeReceiver screenReceiver;
    private DataChangeReceiver dataChangeReceiver;

    private List<String> protectPacknames;
    private Intent intent;
    private static final int GRAY_SERVICE_ID = 101;
    private Object lock = new Object();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isScreenOn = true;

    private class ScreenChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            synchronized (lock) {
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    LogUtils.jLog().e("屏幕锁屏了。。。");
                    tempStopProtectPackname = null;
                    isScreenOn = false;
                } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    LogUtils.jLog().e("屏幕亮了。。。");
                    isScreenOn = true;
                    lock.notifyAll();
                }
            }
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.jLog().e("接收到了临时停止保护的广播事件");
            tempStopProtectPackname = intent.getStringExtra("packname");
        }
    }

    private class DataChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.jLog().e("数据库的内容变化了。。。");
            protectPacknames = dao.findAll();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
        return START_STICKY;
    }


    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }


    @Override
    public void onCreate() {
        screenReceiver = new ScreenChangeReceiver();
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver, new IntentFilter("com.itheima.mobilesafe.tempstop"));
        dataChangeReceiver = new DataChangeReceiver();
        registerReceiver(dataChangeReceiver, new IntentFilter("com.itheima.mobilesafe.applockchange"));

        dao = new ApplockDao(this);
        protectPacknames = dao.findAll();
        flag = true;
        intent = new Intent(getApplicationContext(), AppLockActivity.class);
        // 服务是没有任务栈信息的，在服务开启activity，要指定这个activity运行的任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Runnable runnable = new Runnable() {
            public void run() {
                while (flag) {
                    synchronized (lock) {
                        while (!isScreenOn) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String packName = TaskInfoProvider.getForegroundApp();
                    if (protectPacknames.contains(packName)) {//查询内存效率高很多
                        // 判断这个应用程序是否需要临时的停止保护。
                        if (packName.equals(tempStopProtectPackname)) {

                        } else {
                            // 当前应用需要保护。蹦出来，弹出来一个输入密码的界面。
                            // 设置要保护程序的包名
                            intent.putExtra(AppLockActivity.param_package_name, packName);
                            startActivity(intent);
                        }
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        };
        GlobalThreadPool.getInstance().execute(runnable);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        flag = false;
        unregisterReceiver(innerReceiver);
        innerReceiver = null;
        unregisterReceiver(screenReceiver);
        screenReceiver = null;
        unregisterReceiver(dataChangeReceiver);
        dataChangeReceiver = null;
        super.onDestroy();
    }

}
