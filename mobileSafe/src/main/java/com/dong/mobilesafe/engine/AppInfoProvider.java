package com.dong.mobilesafe.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.dong.mobilesafe.db.dao.AppTrafficInfoDao;
import com.dong.mobilesafe.domain.AppInfo;
import com.dong.mobilesafe.domain.AppTrafficInfo;
import com.dong.mobilesafe.utils.DateUtil;

/**
 * 业务方法，提供手机里面安装的所有的应用程序信息
 */
public class AppInfoProvider {

    /**
     * 获取所有的安装的应用程序信息。
     *
     * @param context 上下文
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context) {
        return getAppInfos(context, false,false);
    }


    /**
     * 获取所有的安装的应用程序信息。
     *
     * @param context    上下文
     * @param isInternet 是否包含没有具有网络的APP
     * @param  isContainTraffic 是否包换网络流量信息
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context, boolean isInternet,boolean isContainTraffic) {
        AppTrafficInfoDao appTrafficInfoDao = new AppTrafficInfoDao(context);
        PackageManager pm = context.getPackageManager();
        //所有的安装在系统上的应用程序包信息。
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packInfo : packInfos) {
            final String packageName = packInfo.packageName;
            if (isInternet &&
                    PackageManager.PERMISSION_DENIED == pm.checkPermission(Manifest.permission.INTERNET, packageName)) {
                continue;
            }
            AppInfo appInfo = new AppInfo();
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            String name = packInfo.applicationInfo.loadLabel(pm).toString();
            int flags = packInfo.applicationInfo.flags;    //应用程序信息的标记 相当于用户提交的答卷
            int uid = packInfo.applicationInfo.uid;    //操作系统分配给应用系统的一个固定的编号。一旦应用程序被装到手机 id就固定不变了。
            appInfo.setUid(uid);
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //用户程序
                appInfo.setUserApp(true);
            } else {
                //系统程序
                appInfo.setUserApp(false);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
                //手机的内存
                appInfo.setInRom(true);
            } else {
                //手机外存储设备
                appInfo.setInRom(false);
            }
            appInfo.setPackname(packageName);
            appInfo.setIcon(icon);
            appInfo.setName(name);
            appInfos.add(appInfo);

            if(!isContainTraffic) continue;
            List<AppTrafficInfo> appTrafficInfos = appTrafficInfoDao.findByPackage(packageName);
            long mobileTraffic = 0;
            long wifiTraffic = 0;
            for (int i = 0; appTrafficInfos != null && i < appTrafficInfos.size(); i++) {
                AppTrafficInfo traffic = appTrafficInfos.get(i);
                mobileTraffic += (traffic.getTxMobileCount() + traffic.getRxMobileCount());
                wifiTraffic += (traffic.getRxWifiCount() + traffic.getTxWifiCount());
            }
            AppTrafficInfo todayTraffic = appTrafficInfoDao.findByPackageAndDate(
                    packageName, DateUtil.toYYYYMMDD(new Date()));
            if (todayTraffic != null) {
                appInfo.setTodayMobileTraffic(todayTraffic.getTxMobileCount() + todayTraffic.getRxMobileCount());
                appInfo.setTodayWifiTraffic(todayTraffic.getRxWifiCount() + todayTraffic.getTxWifiCount());
            }

            appInfo.setMobileTraffic(mobileTraffic);
            appInfo.setWifiTraffic(wifiTraffic);

        }
        return appInfos;
    }


    public static Drawable getAppIcon(Context context, String pakName) {
        PackageManager pm = context.getPackageManager();
        Drawable icon = null;
        try {
            icon = pm.getApplicationIcon(pakName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }


}
