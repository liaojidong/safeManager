package com.dong.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.dong.mobilesafe.base.BaseFragment;

import butterknife.InjectView;
import butterknife.OnClick;

public class Setup1Fragment extends BaseFragment {
	private OnNextPageListener mListener;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnNextPageListener) {
			mListener = (OnNextPageListener) activity;
		}else {
			throw new RuntimeException("must be implement OnNextPageListener");
		}
	}

	@Override
	protected int onGetContentView() {
		return R.layout.activity_setup1;
	}

	@OnClick(R.id.btn_start_use)
	public void startUse(View v) {
		mListener.onNextPage();
	}



}
