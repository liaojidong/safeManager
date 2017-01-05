package com.dong.mobilesafe.utils;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

public class CacheManager {
	private Context context;
	private PackageManager pm;
	private long allCacheSize = -1;
	private OnScanListener scanListener;
	private OnCleanListener cleanListener;
	private final static  int STATE_SCAN_START = 1;
	private final static int STATE_SCANNING = 2;
	private final static int STATE_SCAN_FINISH = 3;
	private final static int STATE_SCAN_RESULT = 4;
	private final static int STATE_ALL_CLEAN= 5;
	private  Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STATE_SCAN_START:
				int scanSize = msg.arg1;
				if(scanListener != null) {
					scanListener.onScanStart(scanSize);
				}
				break;

			case STATE_SCANNING:
				int progress = msg.arg1;
				PackageInfo packInfo  = (PackageInfo) msg.obj;
				if(scanListener !=null) {
					scanListener.onScanning(progress,packInfo);
				}
				break;

			case STATE_SCAN_FINISH:
				if(scanListener !=null) {
					scanListener.onScanFinish();
				}
				break;
			case STATE_SCAN_RESULT:
				PackageStats pStats = (PackageStats) msg.obj;
				if(scanListener != null) {
					scanListener.onScanCacheSize(pStats);
				}
				break;
				
			case STATE_ALL_CLEAN:
             	if(cleanListener != null) {
            		cleanListener.onCleanFinish();
            	}
				break;
			}
		}
	};
	
	public interface OnScanListener {
		public void onScanStart(int scanCount);
		public void onScanning(int progress,PackageInfo packInfo);
		public void onScanCacheSize(PackageStats pStats);
		public void onScanFinish();
	}
	
	public interface OnCleanListener {
		public void onCleanFinish();
	}
	
	
	public CacheManager(Context context) {
		this(context, null);
	}
	
	
	public CacheManager(Context context,OnScanListener scanListener) {
		this(context, scanListener, null);
	}
	
	public CacheManager(Context context,OnScanListener scanListener,OnCleanListener cleanListener) {
		
		
		this.context = context;
		this.scanListener = scanListener;
		this.cleanListener = cleanListener;
		pm = context.getPackageManager();
		
	}
	
	
	public void scanCache() {
		GlobalThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					allCacheSize = 0;
					Method getPackageSizeInfoMethod  = PackageManager.class.getMethod("getPackageSizeInfo",
							String.class,IPackageStatsObserver.class);
					List<PackageInfo> packInfos = pm.getInstalledPackages(0);
					int progress = 0;
					mHandler.sendMessage(mHandler.obtainMessage(STATE_SCAN_START, packInfos.size(), 0));
					for (PackageInfo packInfo : packInfos) {
						try {
							getPackageSizeInfoMethod.invoke(pm,packInfo.packageName, new MyDataObserver());
							Thread.sleep(20);
						} catch (Exception e) {
							e.printStackTrace();
						}
						progress++;
						mHandler.sendMessage(mHandler.obtainMessage(STATE_SCANNING, progress, 0, packInfo));
					}
					mHandler.sendEmptyMessage(STATE_SCAN_FINISH);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private class MyDataObserver extends IPackageStatsObserver.Stub {
		
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long appCacheSize = pStats.cacheSize+pStats.externalCacheSize;
			allCacheSize += appCacheSize;
			mHandler.sendMessage(mHandler.obtainMessage(STATE_SCAN_RESULT, pStats));
		}
	}
	
	
	/**
	 * 清除所有缓存
	 */
    public  void clearAllCache() {
		GlobalThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					PackageManager packageManager = context.getPackageManager();
					Method localMethod = packageManager.getClass().getMethod("freeStorageAndNotify", Long.TYPE,
							IPackageDataObserver.class);

					localMethod.invoke(packageManager, (Long.MAX_VALUE-1), new IPackageDataObserver.Stub() {

						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
							mHandler.sendEmptyMessage(STATE_ALL_CLEAN);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

    } 



	public void setOnScanListener(OnScanListener scanListener) {
		if(scanListener != null) {
			this.scanListener = scanListener;
		}
		
	}
	
	public void setOnCleanListener(OnCleanListener cleanListener) {
		if(cleanListener != null) {
			this.cleanListener = cleanListener;
		}
	}


	public long getAllCacheSize() {
		return allCacheSize;
	}
	
}
