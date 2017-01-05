package com.dong.mobilesafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.utils.SharedPreferencesManager;

import butterknife.InjectView;
import butterknife.OnClick;

public class Setup4Fragment extends BaseFragment {

	@Override
	protected int onGetContentView() {
		return R.layout.activity_setup4;
	}


	@Override
	public void onResume() {
		super.onResume();

	}

	@OnClick(R.id.btn_complete_setup)
	public void completeSetup(View v) {
		SharedPreferencesManager.getInstance().putBoolean(SpKey.KEY_COMPLETE_SETUP,true);
		getActivity().finish();
	}

}
