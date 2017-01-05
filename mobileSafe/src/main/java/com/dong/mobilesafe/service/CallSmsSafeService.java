package com.dong.mobilesafe.service;

import java.lang.reflect.Method;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.dong.mobilesafe.db.dao.BlackNumberDao;
import com.dong.mobilesafe.db.dao.HarassCallDao;
import com.dong.mobilesafe.db.dao.RubbishSmsDao;
import com.dong.mobilesafe.domain.HarassCall;
import com.dong.mobilesafe.domain.RubbishSms;
import com.dong.mobilesafe.utils.MyLogger;
import com.dong.mobilesafe.utils.StringUtils;

public class CallSmsSafeService extends BaseService {
	private static final int INTERCEPT_CALL = 1;
	private static final int INTERCEPT_SMS = 2;
	private static final int INTERCEPT_ALL = 3;
	private InnerSmsReceiver receiver;
	private BlackNumberDao blackNumberDao;
	private TelephonyManager tm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	private class InnerSmsReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			MyLogger.jLog().d("内部广播接受者， 短信到来了");
			//检查发件人是否是黑名单号码，设置短信拦截全部拦截。
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object obj:objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				//得到短信发件人
				String sender = smsMessage.getDisplayOriginatingAddress();
				String body = smsMessage.getMessageBody();
				
				int result = blackNumberDao.findModeByNumber(StringUtils.formatPhoneNumber(sender));
				if(result == INTERCEPT_SMS || result == INTERCEPT_ALL){
					MyLogger.jLog().d("---拦截短信---");
					
					RubbishSms sms = new RubbishSms();
					sms.setMsgBaby(body);
					sms.setNumber(sender);
					sms.setReceiveTime(new Date());
					RubbishSmsDao rubbishSmsDao = new RubbishSmsDao(context);
					rubbishSmsDao.save(sms);
					
					abortBroadcast();
				}
			}
		}
	}
	
	
	
	@Override
	protected void initParams() {
		super.initParams();
		blackNumberDao = new BlackNumberDao(this);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		registerSmsReceiver();
	}
	
	
	
	private void registerSmsReceiver() {
		receiver = new InnerSmsReceiver();
		IntentFilter filter =  new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver,filter);
		
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null) {
			unregisterReceiver(receiver);	
			receiver = null;
		}
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);

	}
	
	private class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://零响状态。
				int result = blackNumberDao.findModeByNumber(incomingNumber);
				if(result == INTERCEPT_CALL || result == INTERCEPT_ALL ){
					MyLogger.jLog().d("挂断电话...");
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver( incomingNumber,new Handler()));
					endCall();
					
					HarassCall harassCall = new HarassCall();
					harassCall.setCallTime(new Date());
					harassCall.setCount(1);
					harassCall.setRead(false);
					harassCall.setLocation("不确定");
					harassCall.setNumber(incomingNumber);
					
					HarassCallDao harassCallDao = new HarassCallDao(CallSmsSafeService.this);
					harassCallDao.save(harassCall);
					
					
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	
	private class CallLogObserver extends ContentObserver{
		private String incomingNumber;

		public CallLogObserver(String incomingNumber,Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			MyLogger.jLog().d("数据库的内容变化了，产生了呼叫记录");
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);

		}
		
	}
	
	
	/**
	 * 结束通话
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void endCall() {

		try {
			//加载servicemanager的字节码
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 利用内容提供者删除呼叫记录
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		ContentResolver  resolver = getContentResolver();
		//呼叫记录uri的路径
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
}
