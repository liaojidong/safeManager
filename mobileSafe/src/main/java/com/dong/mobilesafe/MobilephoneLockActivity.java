package com.dong.mobilesafe;

import android.content.SharedPreferences;

import com.dong.mobilesafe.constant.SpKey;

public class MobilephoneLockActivity extends LockActivity {

	@Override
	protected String getPaternStr() {
		
		SharedPreferences preferences = getSharedPreferences(SpKey.GESTURE_CONFIG,MODE_PRIVATE);
		return preferences.getString(SpKey.KEY_GESTURE_PROTECT_PHONE_PASSWORD,null);
	}
	
	
	@Override
	protected void onPasswordCorrect() {
		super.onPasswordCorrect();
		
	}

}
