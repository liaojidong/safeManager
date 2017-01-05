package com.dong.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.dong.mobilesafe.domain.TaskInfo;
import com.dong.mobilesafe.engine.TaskInfoProvider;
import com.dong.mobilesafe.utils.Process.ProcessKiller;
import com.dong.mobilesafe.utils.log.LogUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import java.util.List;

public class AutoCleanService extends Service {
	private ScreenOffReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new ScreenOffReceiver();
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}
	
	private class ScreenOffReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.jLog().e("屏幕锁屏了。。。");
			killProcess(context);
		}
	}

	private void killProcess(final Context context) {
		GlobalThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				List<TaskInfo> infos = TaskInfoProvider.getTaskInfos(getApplicationContext());
				for(TaskInfo info:infos){
					final String packageName = info.getPackname();
					if(info.isUserTask() && !getPackageName().equals(packageName)) {
						ProcessKiller.killProcess(context,packageName);
					}
				}
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
}
