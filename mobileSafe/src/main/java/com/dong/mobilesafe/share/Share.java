package com.dong.mobilesafe.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public abstract class Share {
	protected boolean isInit = false;
	
	/**
	 * 初始化分享的配置参数
	 * @param context
	 * @param param
	 */
	public abstract void initParams(Context context,ShareCofig param);
	
		
	
	
	public abstract void sendRequest(Activity activity,ShareParams message);
	
	
	public abstract void handleRespond(Intent intent,Object handle);
	

}
