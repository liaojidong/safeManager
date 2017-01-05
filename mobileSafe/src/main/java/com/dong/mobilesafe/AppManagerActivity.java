package com.dong.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.dong.mobilesafe.adapter.AppManagerAdapter;
import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.db.dao.ApplockDao;
import com.dong.mobilesafe.domain.AppInfo;
import com.dong.mobilesafe.engine.AppInfoProvider;

public class AppManagerActivity extends BaseTitleActivity implements OnClickListener,OnItemClickListener,OnItemLongClickListener{
	private static final int MSG_LOAD_FINISH = 1;
	private StickyListHeadersListView lv_app_manager;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOAD_FINISH:
				mAdapter.replaceList(allAppInfos);
				hideLoading();
			break;
			}

		}
	};

	/**
	 * 所有的应用程序包信息
	 */
	private List<AppInfo> appInfos;

	/**
	 * 用户应用程序的集合
	 */
	private List<AppInfo> userAppInfos;
	
	private List<AppInfo> allAppInfos;

	/**
	 * 系统应用程序的集合
	 */
	private List<AppInfo> systemAppInfos;

	/**
	 * 被点击的条目。
	 */
	private AppInfo appInfo;

	private AppManagerAdapter mAdapter;
	
	private ApplockDao dao;
	

	
	@Override
	protected int onGetContentView() {
		return R.layout.activity_app_manager;
	}

	@Override
	protected void onInitData() {
		super.onInitData();
		dao = new ApplockDao(this);
		mAdapter = new AppManagerAdapter(this);
	}


	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle(R.string.app_manager);
		lv_app_manager = (StickyListHeadersListView) this.findViewById(R.id.lv_app_manager);

		lv_app_manager.setOnItemClickListener(this);
		lv_app_manager.setOnItemLongClickListener(this);
		lv_app_manager.setAdapter(mAdapter);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillData();
	}
	

	
	

	private void fillData() {
		showLoading();
		new Thread() {
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				allAppInfos = new ArrayList<AppInfo>();
				for (AppInfo info : appInfos) {
					info.setLock(dao.find(info.getPackname()));
					if (info.isUserApp()) {
						userAppInfos.add(info);
					} else {
						systemAppInfos.add(info);
					}
				}
				allAppInfos.addAll(userAppInfos);
				allAppInfos.addAll(systemAppInfos);
				mHandler.sendEmptyMessage(MSG_LOAD_FINISH);
			};
		}.start();
	}

	



	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		AppInfo appInfo = (AppInfo) mAdapter.getItem(position);
		if(dao.find(appInfo.getPackname())){
			dao.delete(appInfo.getPackname());
			appInfo.setLock(false);
		}else{
			dao.add(appInfo.getPackname());
			appInfo.setLock(true);
		}
		mAdapter.notifyDataSetChanged();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}


	@Override
	public void onClick(View v) {
		
	}



}
