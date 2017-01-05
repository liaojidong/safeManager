package com.dong.mobilesafe.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Xml;

import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ShareManager {
	public final static String CONFIG_FILE_NAME = "share_config.xml";
	public final static String SINA_WEIBO_NAME = "sinaweibo";
	public final static String WECHAT_NAME = "wechat";
	public final static String QZONE = "qzone";
	public final static String QQ = "qq";
	
	public final static String ACTION_SHARE_RESPOND = "com.loovee.common.action.share.respond";
	private static ShareManager instance;
	private Map<String, ShareCofig> shareCofigs;
	private OnShareRespondListener mShareRespondListener;
	private static Object lock = new Object();
	private Map<String, Share> shares = new HashMap<String, Share>();
	private ShareRespondBroadcast receiver;
	
	/**
	 * 分享平台的类型，
	 * sinaweibo：新浪微博分享
	 * wechat：微信分享
	 * qzone:qq空间分享
	 * qq:qq分享
	 * @author Jesse
	 *
	 */
	public enum SharePlatform {
		sinaweibo(1),wechat(2),qzone(3),qq(4);
		private int value;
		private SharePlatform(int value) {
			this.value = value;
		}
		public int value() {
			return value;
		}
	}
	


	private ShareManager(Context context) {
		parseCofig(readCofig(context));
		createShareObj();
		
	}

	
	/**
	 * 注册广播
	 * @param context
	 */
	private void registerBroadcast(Context context) {
		receiver = new ShareRespondBroadcast();
		IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPOND);
		context.registerReceiver(receiver, filter);
	}
	
	
	private void unRegisterBroadcast(Context context) {
		if(receiver != null) {
			try {
				context.unregisterReceiver(receiver);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}


	/**
	 * 读取分享配置信息
	 */
	private InputStream readCofig(Context context) {
		InputStream is = null;
		try {
			is = context.getAssets().open(CONFIG_FILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	

	/**
	 * 解析分享配置信息
	 */
	private void parseCofig(InputStream is) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			ShareCofig cofig = null;
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT) {
			    switch (eventType) {  
	            case XmlPullParser.START_DOCUMENT:  
	            	shareCofigs = new HashMap<String, ShareCofig>();
	                break; 
	                
	            case XmlPullParser.START_TAG:  
	            	if(parser.getName().equals("share")) {
	            		cofig = new ShareCofig();
	            		for(int i = 0; i < parser.getAttributeCount(); i++) {
	            			String attributeName = parser.getAttributeName(i);
	            			if(attributeName.equals("appid")) {
	            				cofig.setAppid(parser.getAttributeValue(i));
	            			}else if(attributeName.equals("appSecret")) {
	            				cofig.setAppSecret(parser.getAttributeValue(i));
	            			}else if(attributeName.equals("shareby")) {
	            				cofig.setShareBy(Integer.parseInt(parser.getAttributeValue(i)));
	            			}else if(attributeName.equals("platform")) {
	            				cofig.setPlatform(parser.getAttributeValue(i));
	            			}else if(attributeName.equals("redirectUrl")) {
	            				cofig.setRedirectUrl(parser.getAttributeValue(i));
	            			}else if(attributeName.equals("enable")) {
	            				cofig.setEnable(Boolean.parseBoolean(parser.getAttributeValue(i)));
	            			}
	            		}
	            	}
	            	break;
	                
	            case XmlPullParser.END_TAG:
	            	if(parser.getName().equals("share") && cofig != null) {
	            		shareCofigs.put(cofig.getPlatform(), cofig);
	            	}
	            	break;
			    }
			    // 进入下一个元素并触发相应事件
	            eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 创建分享对象
	 */
	private void createShareObj() {
		for(Entry<String, ShareCofig> entry:shareCofigs.entrySet()) {
			ShareCofig conCofig = shareCofigs.get(entry.getKey());
			Share share = null;
			if(conCofig.getPlatform().equals(SINA_WEIBO_NAME) && conCofig.isEnable()) {
				share = ShareFactor.createShare(SharePlatform.sinaweibo);
				shares.put(SINA_WEIBO_NAME, share);
			}else if(conCofig.getPlatform().equals(WECHAT_NAME) && conCofig.isEnable()) {
				share = ShareFactor.createShare(SharePlatform.wechat);
				shares.put(WECHAT_NAME, share);
			}else if(conCofig.getPlatform().equals(QZONE) && conCofig.isEnable()) {
				share = ShareFactor.createShare(SharePlatform.qzone);
				shares.put(QZONE, share);
			}else if(conCofig.getPlatform().equals(QQ) && conCofig.isEnable()) {
				share = ShareFactor.createShare(SharePlatform.qq);
				shares.put(QQ, share);
			}
		}
	}
	
	
	/**
	 * 初始化数据，进行分享前应该要先初始化
	 * @param context
	 */
	public void init(Context context) {
		for(Entry<String, Share> entry:shares.entrySet()) {
			Share share = entry.getValue();
			String key = entry.getKey();
			share.initParams(context,shareCofigs.get(key));
		}
		registerBroadcast(context);
	}
	
	/**
	 * 分享内容。支持新浪微博、微信、qq空间、短信等分享平台
	 * @param platform 分享平台
	 * @param ac
	 * @param shareParams 分享的参数
	 */
	public void share(SharePlatform platform,Activity ac,ShareParams shareParams) {
		switch (platform) {
		case sinaweibo:
			Share sinaShare = shares.get(SINA_WEIBO_NAME);
			sinaShare.sendRequest(ac, shareParams);
			break;

		case wechat:
			Share wechatShare = shares.get(WECHAT_NAME);
			wechatShare.sendRequest(ac, shareParams);
			break;
			
		case qzone:
			Share qzonShare = shares.get(QZONE);
			qzonShare.sendRequest(ac, shareParams);
			break;
			
		case qq:
			Share qqShare = shares.get(QQ);
			qqShare.sendRequest(ac, shareParams);
			break;
			
		default:
			throw new RuntimeException("暂时不支持该平台的分型功能！");
		}
	}
	
	
	
	/**
	 * 设定分享回调监听者
	 * @param onRespondListener
	 */
	public void registerShareRespondListener(OnShareRespondListener onRespondListener) {
		this.mShareRespondListener = onRespondListener;
	}
	
	

	public void handleIntent(Intent intent,Object object) {
		if(object instanceof IWXAPIEventHandler) {
			shares.get(WECHAT_NAME).handleRespond(intent, object);
		}
		if(object instanceof IWeiboHandler.Response) {
			shares.get(SINA_WEIBO_NAME).handleRespond(intent, object);
		}
	}
	
	
	/**
	 * 返回一个单例对象
	 * @param context
	 * @return
	 */
	public static ShareManager getInstance(Context context) {

		if (instance == null) {
			synchronized (lock) {
				if(instance == null) {
					instance = new ShareManager(context);
				}
			}
		}
		return instance;
	}
	
	
	public class ShareRespondBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null) {
				ShareRespond respond  = (ShareRespond) intent.getSerializableExtra("respond");
				if(mShareRespondListener != null) {
					mShareRespondListener.onRespond(respond);
				}
			}
		}
	}
	
	public void unRegisterShareRespondListener(Context context) {
		unRegisterBroadcast(context);
	}


	
	public ShareCofig getConfig(String type) {
		return shareCofigs.get(type);
	}




}
