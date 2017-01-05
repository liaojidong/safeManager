package com.dong.mobilesafe.service;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.IBinder;

import com.dong.mobilesafe.db.dao.AppTrafficInfoDao;
import com.dong.mobilesafe.db.dao.TrafficInfoDao;
import com.dong.mobilesafe.domain.AppTrafficInfo;
import com.dong.mobilesafe.domain.TrafficInfo;
import com.dong.mobilesafe.utils.DateUtil;
import com.dong.mobilesafe.utils.HttpUtil;
import com.dong.mobilesafe.utils.MyLogger;
import com.dong.mobilesafe.utils.log.LogUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import java.util.Date;
import java.util.List;

public class TrafficMonitorService extends BaseService {
    public static final String ACTION_TRAFFIC_CHANGE = "com.dong.mobilesafe.traffic";
    private static final long PERIOD_TIME = 20 * 1000;  //一分钟统计一次
    private int networkState;
    private NetworkReceiver networkReceiver;
    private Object lock = new Object();
    private boolean isScreenOn = true;
    private boolean isRunning;
    private ScreenChangeReceiver screenReceiver;

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


    @Override
    protected void initParams() {
        super.initParams();
        isRunning = true;
        registerScreenReciver();
        registerNetworkReceiver();
        networkState = HttpUtil.getNetWorkState(this);
        GlobalThreadPool.getInstance().execute(statsTrafficTask);
    }

    private void registerScreenReciver() {
        screenReceiver = new ScreenChangeReceiver();
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }


    private Runnable statsTrafficTask = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                synchronized (lock) {
                    while (!isScreenOn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (networkState != HttpUtil.STATE_INVALIDE_NETWORK) {
                    LogUtils.jLog().e("正在统计流量 ");
                    statsTrafficCount();
                    statsTrafficAppCount();
                    sendTrafficBroadcast();
                }
                try {
                    Thread.sleep(PERIOD_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    /**
     * 注册网络广播
     */
    private void registerNetworkReceiver() {
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);
    }


    private void sendTrafficBroadcast() {
        Intent intent = new Intent(ACTION_TRAFFIC_CHANGE);
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 统计每个app流量的使用情况
     */
    private void statsTrafficAppCount() {
        AppTrafficInfoDao appTrafficInfoDao = new AppTrafficInfoDao(TrafficMonitorService.this);
        PackageManager pm = TrafficMonitorService.this.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        for (PackageInfo packInfo : packInfos) {
            final String packageName = packInfo.packageName;
            if (PackageManager.PERMISSION_DENIED == pm.checkPermission(Manifest.permission.INTERNET, packageName)) {
                continue;
            }
            final int uid = packInfo.applicationInfo.uid;
            final long bootRxCount = TrafficStats.getUidRxBytes(uid);
            final long bootTxCount = TrafficStats.getUidTxBytes(uid);
            AppTrafficInfo appTrafficInfo = appTrafficInfoDao.findByPackageAndDate(packageName, DateUtil.toYYYYMMDD(new Date()));
            if (appTrafficInfo == null) {
                appTrafficInfo = new AppTrafficInfo();
                appTrafficInfo.setStatisticsDate(DateUtil.toYYYYMMDD(new Date()));
                appTrafficInfo.setPackageName(packageName);
                appTrafficInfo.setBootTxCount(bootTxCount);
                appTrafficInfo.setBootRxCount(bootRxCount);
                appTrafficInfo.setTxCount(0L);
                appTrafficInfo.setRxCount(0L);
                appTrafficInfo.setRxWifiCount(0L);
                appTrafficInfo.setTxWifiCount(0L);
                appTrafficInfo.setTxMobileCount(0L);
                appTrafficInfo.setRxMobileCount(0L);
                appTrafficInfoDao.save(appTrafficInfo);

            } else {

                long addRxCount = bootRxCount - appTrafficInfo.getBootRxCount();
                long addTxCount = bootTxCount - appTrafficInfo.getBootTxCount();
                if (addRxCount < 0) {
                    addRxCount = 0;
                }
                if (addTxCount < 0) {
                    addTxCount = 0;
                }
                switch (networkState) {
                    case HttpUtil.STATE_MOBILE_NETWORK:
                        appTrafficInfo.setRxMobileCount(appTrafficInfo.getRxMobileCount() + addRxCount);
                        appTrafficInfo.setTxMobileCount(appTrafficInfo.getTxMobileCount() + addTxCount);
                        break;

                    case HttpUtil.STATE_WIFI_NETWORK:
                        appTrafficInfo.setRxWifiCount(appTrafficInfo.getRxWifiCount() + addRxCount);
                        appTrafficInfo.setTxWifiCount(appTrafficInfo.getTxWifiCount() + addTxCount);
                        break;
                }
                appTrafficInfo.setRxCount(appTrafficInfo.getRxCount() + addRxCount);
                appTrafficInfo.setTxCount(appTrafficInfo.getTxCount() + addTxCount);
                appTrafficInfo.setBootTxCount(bootTxCount);
                appTrafficInfo.setBootRxCount(bootRxCount);
                appTrafficInfoDao.update(appTrafficInfo);

            }

        }

    }


    /**
     * 网络改变接受者
     */
    class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = HttpUtil.getNetWorkState(TrafficMonitorService.this);
            switch (type) {
                case HttpUtil.STATE_INVALIDE_NETWORK:
                    LogUtils.jLog().e("无网络");
                    networkState = HttpUtil.STATE_INVALIDE_NETWORK;
                    break;

                case HttpUtil.STATE_WIFI_NETWORK:
                    LogUtils.jLog().e("wifi网络");
                    if (networkState != HttpUtil.STATE_WIFI_NETWORK) {
                        statsTrafficAppCount();
                        networkState = HttpUtil.STATE_WIFI_NETWORK;
                    }
                    break;
                case HttpUtil.STATE_MOBILE_NETWORK:
                    MyLogger.jLog().e("移动网络");
                    if (networkState != HttpUtil.STATE_MOBILE_NETWORK) {
                        statsTrafficAppCount();
                        networkState = HttpUtil.STATE_MOBILE_NETWORK;
                    }
                    break;

            }
        }

    }


    /**
     * 统计全部流量
     */
    protected void statsTrafficCount() {
        TrafficInfoDao dao = new TrafficInfoDao(TrafficMonitorService.this);

        final long bootMobileRx = TrafficStats.getMobileRxBytes();
        final long bootMobileTx = TrafficStats.getMobileTxBytes();
        final long bootRxCount = TrafficStats.getTotalRxBytes();
        final long bootTxCount = TrafficStats.getTotalTxBytes();


        TrafficInfo info = dao.findByDate(DateUtil.toYYYYMMDD(new Date()));
        if (info == null) {
            info = new TrafficInfo();
            info.setMobileRx(0L);
            info.setMobileTx(0L);
            info.setRxCount(0L);
            info.setTxCount(0L);
            info.setMobileRx(0L);
            info.setMobileTx(0L);
            info.setRxCount(0L);
            info.setTxCount(0L);
            info.setBootMobileRx(bootMobileRx);
            info.setBootMobileTx(bootMobileTx);
            info.setBootRxCount(bootRxCount);
            info.setBootTxCount(bootTxCount);
            info.setStatisticsDate(DateUtil.toYYYYMMDD(new Date()));
            dao.save(info);
        } else {
            long addMobileRx = bootMobileRx - info.getBootMobileRx();
            long addMobileTx = bootMobileTx - info.getBootMobileTx();
            long addRxCount = bootRxCount - info.getBootRxCount();
            long addTxCount = bootTxCount - info.getBootTxCount();

            if (addRxCount < 0) {
                addRxCount = 0;
            }

            if (addTxCount < 0) {
                addTxCount = 0;
            }

            if (addMobileRx < 0) {
                addMobileRx = 0;
            }

            if (addMobileTx < 0) {
                addMobileTx = 0;
            }

            info.setRxCount(info.getRxCount() + addRxCount);
            info.setTxCount(info.getTxCount() + addTxCount);
            info.setMobileRx(info.getMobileRx() + addMobileRx);
            info.setMobileTx(info.getMobileTx() + addMobileTx);

            info.setBootMobileRx(bootMobileRx);
            info.setBootMobileTx(bootMobileTx);
            info.setBootRxCount(bootRxCount);
            info.setBootTxCount(bootTxCount);

            dao.update(info);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.jLog().d("onDestroy");
        isRunning = false;
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
