package com.dong.mobilesafe.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.adapter.RubbishSmsAdapter;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.db.dao.RubbishSmsDao;
import com.dong.mobilesafe.domain.RubbishSms;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

public class RubbishSmsFragment extends BaseFragment {
	private ListView rubbishListView;
	private RubbishSmsDao rubbishSmsDao;
	private RubbishSmsAdapter rubbishSmsAdapter;
	
	@Override
	protected void findViewById(View rootView) {
		rubbishListView = (ListView) rootView.findViewById(R.id.lv_rubbish_sms_listview);
	}
	
	@Override
	protected void onInitData(Bundle savedInstanceState) {
		super.onInitData(savedInstanceState);
		rubbishSmsDao = new RubbishSmsDao(context);
		rubbishSmsAdapter = new RubbishSmsAdapter(context);

	}
	
	
	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		rubbishListView.setAdapter(rubbishSmsAdapter);
		fillData();
	}
	
	
	
	
	

	private void fillData() {
		showLoading();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final List<RubbishSms> list = rubbishSmsDao.findAll(RubbishSms.class);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						hideLoading();
						if(list == null || list.isEmpty()) {
							showTip();
						}
						rubbishSmsAdapter.replaceList(list);
					}
				});
			}
		};
		GlobalThreadPool.getInstance().execute(runnable);
	}



	@Override
	protected int onGetContentView() {
		return R.layout.fragment_rubbish_sms;
	}

}
