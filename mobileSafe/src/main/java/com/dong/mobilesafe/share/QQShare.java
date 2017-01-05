package com.dong.mobilesafe.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.tauth.Tencent;

public class QQShare extends Share{
	private Tencent mTencent;
	private int shareType = com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
	@Override
	public void initParams(Context context, ShareCofig param) {
		if(mTencent == null) {
			mTencent = Tencent.createInstance(param.getAppid(), context);
		}
	}

	@Override
	public void sendRequest(final Activity activity, ShareParams message) {
		final Bundle params = new Bundle();
		if(!TextUtils.isEmpty(message.getTitle())) {
			params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, message.getTitle());
		}
		if(!TextUtils.isEmpty(message.getSiteUrl())) {
			params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, message.getSiteUrl());
		}
       if(!TextUtils.isEmpty(message.getText())) {
		   params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, message.getText());
	   }
       if(!TextUtils.isEmpty(message.getImageUrl())) {
		   params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL,message.getImageUrl());
	   }

        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME, "loovee");
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mTencent.shareToQQ(activity, params, null); 
				
			}
		});
	}

	@Override
	public void handleRespond(Intent intent, Object handle) {
		
	}

}
