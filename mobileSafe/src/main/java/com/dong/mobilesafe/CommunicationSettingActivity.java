package com.dong.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;

import com.dong.mobilesafe.adapter.CommonDialogListAdapter;
import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.domain.DialogItem;
import com.dong.mobilesafe.service.CallSmsSafeService;
import com.dong.mobilesafe.ui.SettingItemView;
import com.dong.mobilesafe.ui.SettingItemView.onOnOffListener;
import com.dong.mobilesafe.ui.SettingNextItemView;
import com.dong.mobilesafe.utils.DialogUtils;
import com.dong.mobilesafe.utils.ServiceUtils;
import com.orhanobut.dialogplus.DialogPlus.Gravity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

public class CommunicationSettingActivity extends BaseTitleActivity implements onOnOffListener,OnClickListener{
	public static final int INTERCEPT_AUTO = 1;
	public static final int INTERCEPT_BLACK_NUMBER = 2;
	public static final int EXCEPT_WHITE_NUMBER = 3;
	public static final int INTERCEPT_STRANG_NUMBER = 4;
	
	private SettingItemView harassItem,harassTipItem;
	private SettingNextItemView harassPattern;
	private SharedPreferences sp;
	private List<DialogItem> items = new ArrayList<DialogItem>();
	
	@Override
	protected int onGetContentView() {
		
		return R.layout.activity_communication_setting;
	}
	
	@Override
	protected void onInitData() {
		super.onInitData();
		String[]arrs = getResources().getStringArray(R.array.intercept_pattern);
		for(String s:arrs) {
			DialogItem item = new DialogItem();
			item.setName(s);
			items.add(item);
		}
		sp = getSharedPreferences(SpKey.COMMUNICATION_CONFIG, MODE_PRIVATE);
	}
	


	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle("通讯设置");
		harassItem = (SettingItemView) findViewById(R.id.siv_harass);
		harassTipItem = (SettingItemView) findViewById(R.id.siv_harass_tip);
		harassPattern = (SettingNextItemView) findViewById(R.id.sniv_harass_pattern);

		harassItem.setOnOnOffListener(this);
		harassTipItem.setOnOnOffListener(this);
		harassPattern.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		refleshSetingParams();
		
	}

	protected void refleshSetingParams() {
		boolean iscallSmsServiceRunning = ServiceUtils.isServiceRunning(this,CallSmsSafeService.class.getName());
		boolean isShowTip = sp.getBoolean(SpKey.KEY_SHOW_TIP, false);
		harassTipItem.setChecked(isShowTip);
		harassItem.setChecked(iscallSmsServiceRunning);
		if(iscallSmsServiceRunning) {
			harassTipItem.setEnable(true);
			harassPattern.setViewEnabled(true);
			
		}else {
			harassTipItem.setEnable(false);
			harassPattern.setViewEnabled(false);
		}
		int pattern = sp.getInt(SpKey.KEY_INTERCEPT_PATTERN, 1);
		final String[]arrs = getResources().getStringArray(R.array.intercept_pattern);
		harassPattern.setHintTxt(arrs[pattern-1]);
	}
	

	@Override
	public void onChange(View view, boolean isCheced) {
		switch (view.getId()) {
		case R.id.siv_harass:
			if(isCheced) {
				startService(CallSmsSafeService.class);
				harassTipItem.setEnable(true);
				harassPattern.setViewEnabled(true);
			}else {
				stopService(CallSmsSafeService.class);
				harassTipItem.setEnable(false);
				harassPattern.setViewEnabled(false);
			}
			break;

		case R.id.siv_harass_tip:
			sp.edit().putBoolean(SpKey.KEY_SHOW_TIP, isCheced).commit();
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sniv_harass_pattern:
			Holder holder = new ListHolder();
			CommonDialogListAdapter adapter = new CommonDialogListAdapter(this);
			OnItemClickListener itemClickListener = new OnItemClickListener() {
				
				@Override
				public void onItemClick(DialogPlus dialog, Object item, View view,
						int position) {
					final String[]arrs = getResources().getStringArray(R.array.intercept_pattern);
					switch (position) {
					case INTERCEPT_AUTO:
						sp.edit().putInt(SpKey.KEY_INTERCEPT_PATTERN, INTERCEPT_AUTO).commit();
					
						break;

					case INTERCEPT_BLACK_NUMBER:
						sp.edit().putInt(SpKey.KEY_INTERCEPT_PATTERN, INTERCEPT_BLACK_NUMBER).commit();
						break;
						
					case EXCEPT_WHITE_NUMBER:
						sp.edit().putInt(SpKey.KEY_INTERCEPT_PATTERN, EXCEPT_WHITE_NUMBER).commit();
						break;
						
					case INTERCEPT_STRANG_NUMBER:
						sp.edit().putInt(SpKey.KEY_INTERCEPT_PATTERN, INTERCEPT_STRANG_NUMBER).commit();
						break;
					}
					harassPattern.setHintTxt(arrs[position-1]);
					dialog.dismiss();
					
				}
			};
			adapter.replaceList(items);
			DialogUtils.showNoFooterDialog(this, getString(R.string.harass_pattern), holder, Gravity.BOTTOM, adapter, null, itemClickListener);
			break;

		default:
			break;
		}
		
	}
		

}
