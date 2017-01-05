package com.dong.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.utils.SharedPreferencesManager;

import butterknife.InjectView;

public class LostFindActivity extends BaseTitleActivity {

	@InjectView(R.id.tv_safenumber)
	TextView tv_safenumber;
	@InjectView(R.id.iv_protecting)
	ImageView iv_protecting;

	@Override
	protected int onGetContentView() {
		return R.layout.activity_lost_find;
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle(R.string.phone_protected);
		//得到我们设置的安全号码
		String safeNumber = SharedPreferencesManager.getInstance().getString(SpKey.key_safe_number,null);
		tv_safenumber.setText(safeNumber);
	}

	/**
	 * 重新进入手机防盗设置向导页面
	 * @param view
	 */
	public void reEnterSetup(View view){
		Intent intent = new Intent(this,Setup1Fragment.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}

}
