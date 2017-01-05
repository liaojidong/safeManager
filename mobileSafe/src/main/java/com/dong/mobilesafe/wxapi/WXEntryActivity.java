package com.dong.mobilesafe.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.dong.mobilesafe.constant.IntentAction;
import com.dong.mobilesafe.share.ShareManager;
import com.dong.mobilesafe.share.ShareRespond;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String appId = ShareManager.getInstance(this).getConfig("wechat").getAppid();
        api = WXAPIFactory.createWXAPI(this, appId, false);
    	// 将该app注册到微信
	    api.registerApp(appId);
	    api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}



	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		ShareRespond respond = new ShareRespond();
		respond.sharePlatform = ShareManager.SharePlatform.wechat;
		
		if(resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) { //登录授权回调
			SendAuth.Resp sendResp = (SendAuth.Resp) resp;
			String code = sendResp.code;
			int errcode = sendResp.errCode;
			Intent intent = new Intent(IntentAction.ACTION_WEIXIN_AUTH_SUCCESS);
			intent.putExtra("code", code);
			intent.putExtra("errcode", errcode);
			sendBroadcast(intent);
		}else {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				respond.code = ShareRespond.CODE_SUCCESS;
				respond.msg = resp.errStr;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				respond.code = ShareRespond.CODE_CANCEL;
				respond.msg = resp.errStr;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				respond.code = ShareRespond.CODE_DENY;
				respond.msg = resp.errStr;
				break;
			default:
				respond.code = ShareRespond.CODE_UNKNOW;
				respond.msg = resp.errStr;
			}
			Intent intent = new Intent(ShareManager.ACTION_SHARE_RESPOND);
			intent.putExtra("respond", respond);
			sendBroadcast(intent);
		}
		finish();
	}

	@Override
	public void onReq(BaseReq arg0) {
		Toast.makeText(this, "cod = "+arg0, Toast.LENGTH_LONG).show();
	}

}