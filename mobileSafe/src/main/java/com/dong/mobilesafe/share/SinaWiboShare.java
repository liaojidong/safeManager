package com.dong.mobilesafe.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

public class SinaWiboShare extends Share {
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI  mWeiboShareAPI = null;

	@Override
	public void initParams(Context context,ShareCofig param) {
		if(!isInit) {
			mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, param.getAppid());
			mWeiboShareAPI.registerApp();	// 将应用注册到微博客户端
			isInit = true;
		}
	}

	@Override
	public void sendRequest(Activity activity,ShareParams message) {
	    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
	    
	  
	    if(message != null) {
	    	//文字
	      	TextObject textObject = new TextObject();
	    	textObject.text = message.getText();
	    	textObject.actionUrl = message.getUrl();
	    	textObject.title = message.getTitle();
	        weiboMessage.textObject = textObject;
	 
  
	        //图片
            ImageObject imageObject= new ImageObject();
            if(!TextUtils.isEmpty(message.getImagePath())) {
            	imageObject.imagePath = message.getImagePath();
            }
            if(!TextUtils.isEmpty(message.getImageUrl())) {
            	imageObject.imagePath = message.getImageUrl();
            }
            if(message.getImageData() != null) {
            	imageObject.imageData = message.getImageData();
            }
            weiboMessage.imageObject = imageObject;
     
        }

        
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(activity, request); //发送请求消息到微博，唤起微博分享界面
	}
	
	


	@Override
	public void handleRespond(Intent intent, Object handle) {
		IWeiboHandler.Response response = (IWeiboHandler.Response)handle;
		mWeiboShareAPI.handleWeiboResponse(intent, response);
	}
	
}
