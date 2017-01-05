package com.dong.mobilesafe.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.adapter.HarassNumberAdapter;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.db.dao.HarassCallDao;
import com.dong.mobilesafe.domain.HarassCall;

public class harassNumberFragment extends BaseFragment {
	private ListView harassListview;
	private HarassNumberAdapter harassNumberAdapter;
	private HarassCallDao harassCallDao;

	@Override
	protected void findViewById(View rootView) {
		harassListview = (ListView) rootView.findViewById(R.id.lv_harass_listview);
	}

	@Override
	protected void onInitData(Bundle savedInstanceState) {
		super.onInitData(savedInstanceState);
		harassNumberAdapter = new HarassNumberAdapter(context);
		harassCallDao = new HarassCallDao(context);
		
	}
	

	
	private void fillData() {
		showLoading();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final List<HarassCall> calls = harassCallDao.findAll(HarassCall.class);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						hideLoading();
						if(calls == null || calls.isEmpty()) {
							showTip();
						}
						harassNumberAdapter.replaceList(calls);
					}
				});
			}
		}).start();
		
	}


	
	
	@Override
	protected void onPrepareView() {
		harassListview.setAdapter(harassNumberAdapter);
		fillData();
	}

	@Override
	protected int onGetContentView() {
		return R.layout.fragment_harass_number;
	}

}
