package com.dong.mobilesafe.receiver;

import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.service.GPSService;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.SharedPreferencesManager;
import com.dong.mobilesafe.utils.log.LogUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// 写接收短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		
		for(Object b:objs){
			//具体的某一条短信
			SmsMessage sms =SmsMessage.createFromPdu((byte[]) b);
			//发送者
			String sender = sms.getOriginatingAddress();
			final String safeNumber = SharedPreferencesManager.getInstance().getString(SpKey.key_safe_number,"");
			LogUtils.jLog().e("====sender=="+sender);
			String body = sms.getMessageBody();
			if(sender.contains(safeNumber)){
				if("#*location*#".equals(body)){
					//得到手机的GPS
					Log.i(TAG, "得到手机的GPS");
					//启动服务
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
					String lastlocation = sp.getString("lastlocation", null);
					if(TextUtils.isEmpty(lastlocation)){
						//位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction.....", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					//把这个广播终止掉
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					//播放报警影音
					Log.i(TAG, "播放报警影音");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(false);//
					player.setVolume(1.0f, 1.0f);
					player.start();
					abortBroadcast();
				}
				else if("#*wipedata*#".equals(body)){
					//远程清除数据
					Log.i(TAG, "远程清除数据");
					abortBroadcast();
				}
				else if("#*lockscreen*#".equals(body)){
					//远程锁屏
					Log.i(TAG, "远程锁屏");
					abortBroadcast();
				}
			}
			
			
			
		}
 

	}

}
