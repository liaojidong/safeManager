package com.dong.mobilesafe.service;

import com.dong.mobilesafe.utils.MyLogger;
import com.dong.mobilesafe.utils.StringUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BaseService extends Service {
	private boolean isinitiativeStop;
	private ServiceControlReceiver mReceiver;
	public static final String ACTION_STOP_SERVICE = "com.dong.mobilesafe.stop.service";
	
	private class ServiceControlReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			String serviceName = intent.getStringExtra("serviceName");
			if(StringUtils.isEmpty(action)) {
				return;
			}
			
			if(action.equals(ACTION_STOP_SERVICE) && 
							getServiceName().equals(serviceName)) {
				isinitiativeStop = true;
				BaseService.this.stopSelf();
				MyLogger.jLog().d("关闭了 "+getServiceName()+"服务！");
			}
			
		}
		
	}
	
	
	public String getServiceName() {
		return getClass().getName();
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		initParams();
		
	}


	protected void initParams() {
		isinitiativeStop = false;
		registerControlReceiver();
	}


	protected void registerControlReceiver() {
		mReceiver = new ServiceControlReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_STOP_SERVICE);
		this.registerReceiver(mReceiver, filter);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!isinitiativeStop) {
			reStartService();
		}
		if(mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}
	
	
	protected void reStartService() {
		startService(new Intent(this, getClass()));
	}

}
