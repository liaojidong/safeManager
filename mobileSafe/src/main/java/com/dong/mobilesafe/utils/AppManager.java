package com.dong.mobilesafe.utils;

import java.io.File;
import java.util.List;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.bean.AppInfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;


public class AppManager {
	private static AppManager manager;
	private AppInfo appInfo;
	private static Context context;
	
	private AppManager() {
		try {
			appInfo = new AppInfo();
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			appInfo.setVersion(packageInfo.versionName);
			String name = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
			appInfo.setName(name);
			appInfo.setPakageName(context.getPackageName());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getAppName() {
		return appInfo.getName();
	}
	
	public static AppManager getInstance(Context c) {
		context = c;
		if(null == manager) {
			synchronized(AppManager.class) {
				if(null == manager) {
					manager = new AppManager();
				}
			}
		}
		return manager;
	}
	
	public AppInfo getAppInfo() {
		return appInfo;
	}
	
	
	/**
	 * 
	 * 安装APK
	 */
	public static void  installAPK(File appFile) {
		if(!appFile.exists()) {
			throw new IllegalArgumentException("apk安装文件不存");
		}
		
		if(context == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(appFile),"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	
	/**
	 * 判断某个应用是否正在运行
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppRuning(Context context, String packageName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskinfos = manager.getRunningTasks(100);
		for(RunningTaskInfo task:taskinfos) {
			if(task.baseActivity.getPackageName().equals(packageName) 
					&& task.topActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * 打开应用
	 * @param packageName 应用包名
	 */
	public static void startApplication(Context context,String packageName) { 
		PackageManager pm = context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(packageName);
		if (intent != null) {
			context.startActivity(intent);
		} else {
			Toast.makeText(context, R.string.toast_not_open_app, Toast.LENGTH_LONG).show();
		}
	}
	
	
	
	/**
	 * 卸载应用
	 */
	public static void uninstallAppliation(Activity ac,String packageName) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setAction(Intent.ACTION_DELETE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + packageName));
		ac.startActivityForResult(intent, 0);
	}
	
	
	
	
	/**
	 * 分享一个应用程序
	 */
	public static void shareApplication(Context context,String appName) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件,名称叫："+appName);
		context.startActivity(intent);
	}
	
	
	/**
	 * 根据APK文件获取应用相关信息
	 * @param APKFile
	 * @return
	 */
	public AppInfo getAppInfoByAPKFile(File APKFile) {
		AppInfo info = new AppInfo();
		if(!APKFile.exists()) {
			throw new IllegalArgumentException("APK文件不存在！");
		}
		PackageManager pm = context.getPackageManager();
	    PackageInfo packageInfo = pm.getPackageArchiveInfo(APKFile.getAbsolutePath(),
	    		PackageManager.GET_ACTIVITIES);
	    ApplicationInfo appInfo = null;
	    if (packageInfo != null) {
	    	appInfo = packageInfo.applicationInfo;
	    	info.setPakageName(appInfo.packageName);
	    	info.setVersion(packageInfo.versionName);
	    	info.setName(appInfo.name);
	    }
		return info;
	}
	
	
	
	/**
	 * 判断某个应用是否安装
	 * @param context 
	 * @param packageName 包名
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packageName) {  
		if(StringUtils.isEmpty(packageName)) {
			return false;
		}
        try {
            final PackageManager packageManager = context.getPackageManager(); 
			PackageInfo info =  packageManager.getPackageInfo(packageName, 0);
			if(null !=info) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        return false;
    }  
	
	


	
	
	

}
