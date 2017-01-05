package com.dong.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.service.FloatWindowService;
import com.dong.mobilesafe.ui.SettingItemView;
import com.dong.mobilesafe.ui.SettingItemView.onOnOffListener;
import com.dong.mobilesafe.ui.SettingNextItemView;
import com.dong.mobilesafe.utils.ServiceUtils;

public class TrafficSettingActivity extends BaseTitleActivity implements onOnOffListener,OnClickListener{
	private SharedPreferences sp;
	private SettingItemView floatWindowItem;
	private SettingNextItemView trafficSetupItem;

	@Override
	protected int onGetContentView() {
		return R.layout.activity_traffic_setting;
	}

	
	@Override
	protected void onInitData() {
		super.onInitData();
		sp = getSharedPreferences(SpKey.TRAFFIC_CONFIG, MODE_PRIVATE);
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle("流量设置");
		floatWindowItem = (SettingItemView) findViewById(R.id.siv_float_window);
		trafficSetupItem = (SettingNextItemView) findViewById(R.id.sniv_traffic_setup);

		floatWindowItem.setOnOnOffListener(this);
		trafficSetupItem.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean isFloatWindowServiceRunning = ServiceUtils.isServiceRunning(this,FloatWindowService.class.getName());
		floatWindowItem.setChecked(isFloatWindowServiceRunning);
	}


	@Override
	public void onChange(View view, boolean isCheced) {
		switch (view.getId()) {
		case R.id.siv_float_window:
			if (isCheced) {
				startService(FloatWindowService.class);
			} else {
				stopService(FloatWindowService.class);
			}
			break;

		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sniv_traffic_setup:
			startActivityForResult(
					new Intent(this, TrafficSetupActivity.class), RESULT_FIRST_USER);
			break;

		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			int trafficCount = data.getIntExtra(SpKey.KEY_TRAFFIC_COUNT, 0);
			int startDate = data.getIntExtra(SpKey.KEY_START_DATE, 1);
			trafficSetupItem.setHintTxt(trafficCount+"M/"+startDate+"日");
			Editor editor = sp.edit();
			editor.putInt(SpKey.KEY_TRAFFIC_COUNT, trafficCount);
			editor.putInt(SpKey.KEY_START_DATE, startDate);
			editor.putBoolean(SpKey.KEY_IS_SETTING, true);
			editor.commit();
		}
	}
	
}
