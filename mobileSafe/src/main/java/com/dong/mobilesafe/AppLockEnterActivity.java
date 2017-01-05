package com.dong.mobilesafe;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.engine.AppInfoProvider;

public class AppLockEnterActivity extends LockActivity {

	  private String packname;
	  private Drawable appIcon;
	
	@Override
	protected void onInitData() {
		packname = getIntent().getStringExtra("packname");
		appIcon = AppInfoProvider.getAppIcon(this, packname);
		if(appIcon == null) {
			appIcon = getResources().getDrawable(R.drawable.ic_launcher);
		}
		super.onInitData();
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setEnableSlidFinish(false);
		iconIV.setImageDrawable(appIcon);
	}

	@Override
	protected void onPasswordCorrect() {
		super.onPasswordCorrect();
        startActivity(AppManagerActivity.class);
        finish();
	}

	
	@Override
	protected String getPaternStr() {
		SharedPreferences preferences = getSharedPreferences(SpKey.GESTURE_CONFIG,MODE_PRIVATE);
		return preferences.getString(SpKey.KEY_GESTURE_APP_LOCK_PASSWORD,null);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !TextUtils.isEmpty(packname)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		Intent intent = new Intent();
//		intent.setAction("android.intent.action.MAIN");
//		intent.addCategory("android.intent.category.HOME");
//		intent.addCategory("android.intent.category.DEFAULT");
//		intent.addCategory("android.intent.category.MONKEY");
//		startActivity(intent);
//	}
	
}
