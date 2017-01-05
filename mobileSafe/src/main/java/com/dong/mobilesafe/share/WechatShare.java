package com.dong.mobilesafe.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

public class WechatShare extends Share{
	private IWXAPI api;
	private static final int THUMB_SIZE = 150;

	@Override
	public void initParams(Context context, ShareCofig param) {
		if(!isInit) {
			api = WXAPIFactory.createWXAPI(context,param.getAppid());
			api.registerApp(param.getAppid());
			isInit = true;
		}
		
	}


	@Override
	public void sendRequest(Activity activity, ShareParams message) {
		
		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		if(message !=null) {
			// 初始化一个WXTextObject对象
			WXTextObject textObj = new WXTextObject();
			textObj.text = message.getText();

			msg.mediaObject = textObj;
			msg.description = message.getText();
			req.transaction = buildTransaction("text"); 

			byte[]imageData = message.getImageData();
			if(imageData !=null) {
				WXImageObject imgObj = new WXImageObject(imageData);
				msg.mediaObject = imgObj;
				Bitmap thumbBmp = Bitmap.createScaledBitmap(bytesToBimap(imageData), THUMB_SIZE, THUMB_SIZE, true);
				msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图
				req.transaction = buildTransaction("img"); 
			}
	
		}
		
		req.message = msg;
		req.scene = message.getFlag();
		if(api != null) {
			// 调用api接口发送数据到微信
			api.sendReq(req);	
		}
	
	}


	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}


	@Override
	public void handleRespond(Intent intent, Object handle) {
		IWXAPIEventHandler handler = (IWXAPIEventHandler)handle;
		if(api != null) {
			api.handleIntent(intent, handler);
		}
		
	}
	
	
	private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }



}
