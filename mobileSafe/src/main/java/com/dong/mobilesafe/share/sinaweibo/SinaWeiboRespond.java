package com.dong.mobilesafe.share.sinaweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;


import com.dong.mobilesafe.share.ShareManager;
import com.dong.mobilesafe.share.ShareRespond;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

public class SinaWeiboRespond extends Activity implements
		IWeiboHandler.Response {
	private ShareManager shareManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shareManager = ShareManager.getInstance(this);
		shareManager.handleIntent(getIntent(), this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		ShareRespond respond = new ShareRespond();
		respond.sharePlatform = ShareManager.SharePlatform.sinaweibo;
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			respond.code = ShareRespond.CODE_SUCCESS;
			respond.msg = baseResp.errMsg;
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			respond.code = ShareRespond.CODE_CANCEL;
			respond.msg = baseResp.errMsg;
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			respond.code = ShareRespond.CODE_FAIL;
			respond.msg = baseResp.errMsg;
			break;
		default:
			respond.code = ShareRespond.CODE_UNKNOW;
			respond.msg = baseResp.errMsg;
			break;
		}
		Intent intent = new Intent(ShareManager.ACTION_SHARE_RESPOND);
		intent.putExtra("respond", respond);
		sendBroadcast(intent);

		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		shareManager.handleIntent(intent, this);
	}

}
