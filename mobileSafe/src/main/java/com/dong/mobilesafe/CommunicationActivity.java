package com.dong.mobilesafe;

import java.util.LinkedHashMap;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.dong.mobilesafe.base.PageViewActivity;
import com.dong.mobilesafe.fragment.BlackNumberFragment;
import com.dong.mobilesafe.fragment.RubbishSmsFragment;
import com.dong.mobilesafe.fragment.WhiteNumberFragment;
import com.dong.mobilesafe.fragment.harassNumberFragment;

public class CommunicationActivity extends PageViewActivity implements OnClickListener{
	private harassNumberFragment harassNumberFragment;
	private RubbishSmsFragment rubbishSmsFragment;
	private BlackNumberFragment blackNumberFragment;
	private WhiteNumberFragment whiteNumberFragment;
	
	@Override
	protected void onInitData() {
		super.onInitData();
		harassNumberFragment = new harassNumberFragment();
		rubbishSmsFragment = new RubbishSmsFragment();
		blackNumberFragment = new BlackNumberFragment();
		whiteNumberFragment = new WhiteNumberFragment();
	}
	

	@Override
	protected LinkedHashMap<String, Fragment> setFragment() {
		LinkedHashMap<String, Fragment> map = new LinkedHashMap<String, Fragment>();
		map.put("骚扰电话", harassNumberFragment);
		map.put("垃圾短信", rubbishSmsFragment);
		map.put("黑名单", blackNumberFragment);
		map.put("白名单", whiteNumberFragment);
		return map;
	}



	@Override
	public void onClick(View v) {
		startActivity(CommunicationSettingActivity.class);
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle("通讯卫士");
	}
}
