package com.dong.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.engine.AppInfoProvider;

public class AppLockActivity extends LockActivity {
	 public final static String param_package_name = "package_name";
	  private String packname;
	  private Drawable appIcon;

	@Override
	protected void onInitData() {
		packname = getIntent().getStringExtra(param_package_name);
		appIcon = AppInfoProvider.getAppIcon(this, packname);
		if(appIcon == null) {
			appIcon = getResources().getDrawable(R.drawable.ic_launcher);
		}
		super.onInitData();
	}


	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		iconIV.setImageDrawable(appIcon);
	}

	@Override
	protected void onPasswordCorrect() {
		super.onPasswordCorrect();
		Intent intent = new Intent();
		intent.setAction("com.itheima.mobilesafe.tempstop");
		intent.putExtra("packname", packname);
		sendBroadcast(intent);
        finish();
	}

	
	@Override
	protected String getPaternStr() {
		SharedPreferences preferences = getSharedPreferences(SpKey.GESTURE_CONFIG,MODE_PRIVATE);
		return preferences.getString(SpKey.KEY_GESTURE_APP_LOCK_PASSWORD,null);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addCategory(Intent.CATEGORY_MONKEY);
		startActivity(intent);
	}
	
}
