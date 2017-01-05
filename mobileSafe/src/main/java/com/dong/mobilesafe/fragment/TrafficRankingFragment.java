package com.dong.mobilesafe.fragment;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.dong.mobilesafe.OnChangeTitleListener;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.adapter.TrafficManagerAdapter;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.domain.AppInfo;
import com.dong.mobilesafe.engine.AppInfoProvider;
import com.dong.mobilesafe.utils.MyLogger;
import com.dong.mobilesafe.utils.log.LogUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import butterknife.InjectView;
import butterknife.OnClick;

public class TrafficRankingFragment extends BaseFragment implements OnCheckedChangeListener{
	private List<AppInfo> appList;
	private final int MSG_LOAD_FINISH = 1;
	private ListView trafficListView;
	private TrafficManagerAdapter mAdapter;
	private RadioGroup rankingType;
	private int currentCheck = R.id.rb_statistics_count;
	private TrafficRankingType currentRankingType = TrafficRankingType.mobile;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOAD_FINISH:
				mAdapter.replaceList(appList);
				hideLoading();
				break;

			default:
				break;
			}
		}
	};


	@Override
	protected int onGetContentView() {
		return R.layout.fragment_traffic_ranking;
	}


	@Override
	protected void onInitData(Bundle savedInstanceState) {
		mAdapter = new TrafficManagerAdapter(context,currentCheck);
	}
	
	
	@Override
	protected void findViewById(View rootView) {
		trafficListView = (ListView) rootView.findViewById(R.id.traffic_list);
		rankingType = (RadioGroup) rootView.findViewById(R.id.rg_ranking_type);
	}
	
	
	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		trafficListView.setAdapter(mAdapter);
		rankingType.check(currentCheck);
		rankingType.setOnCheckedChangeListener(this);
		showLoading();
		GlobalThreadPool.getInstance().execute(new BackgroundThread());
	}
	
	
	protected void doBackground() {
		appList = AppInfoProvider.getAppInfos(context,true,true);
		Collections.sort(appList, new TrafficComparator(currentRankingType));
		mHandler.sendEmptyMessage(MSG_LOAD_FINISH);
	}

	enum TrafficRankingType {
		mobile,wifi,name;
	}


	private class TrafficComparator implements Comparator<AppInfo> {
		TrafficRankingType type ;


		public TrafficComparator(TrafficRankingType type) {
			this.type = type;
		}

		@Override
		public int compare(AppInfo lhs, AppInfo rhs) {
			switch (type) {
				case mobile:
					return sortByMoible(lhs,rhs);
				case wifi:
					return sortByWifi(lhs,rhs);
				case name:
					return sortByAppName(lhs,rhs);
			}
			return 0;
		}

		private int sortByMoible(AppInfo lhs, AppInfo rhs) {
			if(lhs.getMobileTraffic() > rhs.getMobileTraffic()) {
				return -1;
			}

			if(lhs.getMobileTraffic() < rhs.getMobileTraffic()) {
				return 1;
			}
			return 0;
		}

		private int sortByWifi(AppInfo lhs, AppInfo rhs) {
			if(lhs.getWifiTraffic() > rhs.getWifiTraffic()) {
				return -1;
			}

			if(lhs.getWifiTraffic() < rhs.getWifiTraffic()) {
				return 1;
			}
			return 0;
		}

		private int sortByAppName(AppInfo lhs, AppInfo rhs) {
			return Collator.getInstance(Locale.CHINESE).compare(lhs.getName(), rhs.getName());
		}

		@Override
		public boolean equals(Object o) {
			return false;
		}
	}
	
	
	class BackgroundThread extends Thread {
		
		@Override
		public void run() {
				doBackground();
			}	
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		MyLogger.jLog().d("onCheckedChanged");
		switch (checkedId) {
		case R.id.rb_statistics_count:
			currentCheck = checkedId;
			rankingType.check(checkedId);
			mAdapter.setType(checkedId);
			showLoading();
			GlobalThreadPool.getInstance().execute(new BackgroundThread());
			break;

		case R.id.rb_statistics_today:
			currentCheck = checkedId;
			rankingType.check(checkedId);
			mAdapter.setType(checkedId);
			showLoading();
			GlobalThreadPool.getInstance().execute(new BackgroundThread());
			break;
		}
		
	}

	@OnClick(R.id.tv_mobile_rank)
	public void sortByMobile(View v) {
		LogUtils.jLog().e("sortByMobile");
		List<AppInfo> apps = mAdapter.getAllItems();
		currentRankingType = TrafficRankingType.mobile;
		Collections.sort(apps,new TrafficComparator(currentRankingType));
		mAdapter.replaceList(apps);
		trafficListView.setSelection(0);
	}

	@OnClick(R.id.tv_wifi_rank)
	public void sortByWifi(View v) {
		LogUtils.jLog().e("sortByWifi");
		List<AppInfo> apps = mAdapter.getAllItems();
		currentRankingType = TrafficRankingType.wifi;
		Collections.sort(apps,new TrafficComparator(currentRankingType));
		mAdapter.replaceList(apps);
		trafficListView.setSelection(0);
	}

	@OnClick(R.id.tv_name_rank)
	public void sortByAppName(View v) {
		LogUtils.jLog().e("sortByAppName");
		List<AppInfo> apps = mAdapter.getAllItems();
		currentRankingType = TrafficRankingType.name;
		Collections.sort(apps,new TrafficComparator(currentRankingType));
		mAdapter.replaceList(apps);
		trafficListView.setSelection(0);
	}

}
