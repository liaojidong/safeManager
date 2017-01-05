package com.dong.mobilesafe.share.qq;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.dong.mobilesafe.share.ShareManager;
import com.dong.mobilesafe.share.ShareRespond;


/**
 * qq空间和qq的回调
 * @author Jesse
 *
 */
public class QzoneRespond extends Activity{
	private String KEY_ACTION = "action";
	private String KEY_RESULT = "result";
	private String ACTION_SHARE_TO_QZONE = "shareToQzone";
	private String ACTION_SHARE_TO_QQ = "shareToQQ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleActionUri();
		finish();
	}
	
	
	public void handleActionUri() {
		Intent intent = getIntent();
		if(intent == null) return;
		String uriStr = intent.getDataString();
		Uri uri = Uri.parse(uriStr.replace("#", ""));
		final String result = uri.getQueryParameter(KEY_RESULT);
		final String action = uri.getQueryParameter(KEY_ACTION);
		ShareRespond respond = new ShareRespond();
		
		if(action.equals(ACTION_SHARE_TO_QZONE)) {
			
			respond.sharePlatform = ShareManager.SharePlatform.qzone;
			if(result.equals("cancel")) {
				respond.code = ShareRespond.CODE_CANCEL;
			}
			if(result.equals("complete")) {
				respond.code = ShareRespond.CODE_SUCCESS;
			}
			if(result.equals("error")) {
				respond.code = ShareRespond.CODE_FAIL;
			}

		}
		
		if(action.equals(ACTION_SHARE_TO_QQ)) {
			respond.sharePlatform = ShareManager.SharePlatform.qq;
			if(result.equals("cancel")) {
				respond.code = ShareRespond.CODE_CANCEL;
			}
			if(result.equals("complete")) {
				respond.code = ShareRespond.CODE_SUCCESS;
			}
			if(result.equals("error")) {
				respond.code = ShareRespond.CODE_FAIL;
			}
		}
		
		Intent respondIntent = new Intent(ShareManager.ACTION_SHARE_RESPOND);
		respondIntent.putExtra("respond", respond);
		sendBroadcast(respondIntent);
		
	}
	

}
