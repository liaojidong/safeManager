package com.dong.mobilesafe.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.OnChangeTitleListener;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.business.TrafficInfoBusiness;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.domain.TaskInfo;
import com.dong.mobilesafe.engine.TaskInfoProvider;
import com.dong.mobilesafe.ui.DynamicWave;
import com.dong.mobilesafe.ui.StorageUtil;
import com.dong.mobilesafe.utils.DensityUtil;
import com.dong.mobilesafe.utils.Process.ProcessKiller;
import com.dong.mobilesafe.utils.SystemInfoUtils;
import com.dong.mobilesafe.utils.WaveNotifyManager;
import com.dong.mobilesafe.utils.log.LogUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

	private ArcProgress arcProgress;
	private FragmentManager fragmentManager;
	private HomeListFragment homeListFragment;
	private OnChangeTitleListener changeTitleListener;
	private Fragment currentFragment;
	private TrafficInfoBusiness trafficInfoBusiness;
	private SensorManager sensorManager = null;
	@InjectView(R.id.iv_sliding_menu)
	ImageView iv_sliding_menu;
	@InjectView(R.id.dynamicWave)
	DynamicWave dynamicWave;
	private OnSlidMenuClickListener slidMenuClickListener;

	public interface OnSlidMenuClickListener {
		public void onMenuClick(View view);
	}
	
	private long availMem;  //可用内存
	private long totalMem;  //总共内存
	private long availStorage; //可用的存储
	private int remainTraffic; //剩余可用流量
	
	private boolean isRefreshData = false; //是否刷新后台数据
	
	private static final int WHAT_CHANGE_PROGRESS = 1;
	private TextView availMemTV,availStorageTV,availTrafficTV;
	
	private SharedPreferences sp;
	
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			if(!isRefreshData) {
				return;
			}
			switch (msg.what) {
			case WHAT_CHANGE_PROGRESS:
				final int progress = msg.arg1;
				
				arcProgress.setProgress(progress);
				dynamicWave.setHeightPercent(progress/100f);
				availMemTV.setText(Formatter.formatShortFileSize(context, availMem));
				availStorageTV.setText(Formatter.formatShortFileSize(context, availStorage));
				if(sp.getBoolean(SpKey.KEY_IS_SETTING, false)) {
					availTrafficTV.setText(Formatter.formatShortFileSize(context, remainTraffic));
				}else {
					availTrafficTV.setText(getString(R.string.unknown));
				}
				break;

			default:
				break;
			}
		}
	};


	@Override
	protected int onGetContentView() {
		return R.layout.fragment_home;
	}

	private boolean isMenuClose = true;
	@OnClick(R.id.iv_sliding_menu)
	public void menuClick(View v) {
		slidMenuClickListener.onMenuClick(v);
	}


	public void setMenuIcon(int resId) {
		if(iv_sliding_menu != null) {
			iv_sliding_menu.setImageResource(resId);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			changeTitleListener = (OnChangeTitleListener) getActivity();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(activity instanceof OnSlidMenuClickListener) {
			slidMenuClickListener = (OnSlidMenuClickListener) getActivity();
		}else {
			throw new RuntimeException("must be implements OnSlidMenuClickListener");
		}
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		changeTitleListener.onChangeTitle(getString(R.string.app_name), null, null);
		updateData(true);
		isRefreshData = true;
		new UpdateDataThread().start();
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}


	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			int sensorType = event.sensor.getType();
			//values[0]:X轴，values[1]：Y轴，values[2]：Z轴
			float[] values = event.values;
			if (sensorType == Sensor.TYPE_ACCELEROMETER)
			{
				if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math
						.abs(values[2]) > 17)) {
					List<Float> list = Arrays.asList(Math.abs(values[0]),Math.abs(values[1]),Math.abs(values[2]));
					float maxValue =  Collections.min(list);
					LogUtils.jLog().e("sensor x = " + values[0]);
					LogUtils.jLog().e("sensor y = " + values[1]);
					LogUtils.jLog().e("sensor z = " + values[2]);

					if(dynamicWave.isQuite()) {
						//摇动手机后，再伴随震动提示~~
						WaveNotifyManager.getInstance(getActivity()).notifyByAll();
						dynamicWave.setWaveAmplitude(DensityUtil.dip2px(getActivity(),15));
						//清理进程
						killAllProcess();
					}
				}

			}
		}

		private boolean isKillFinish = true;
		private void killAllProcess() {
			if(!isKillFinish) return;
			GlobalThreadPool.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					isKillFinish = false;
					List<TaskInfo> allTaskInfos = TaskInfoProvider.getTaskInfos(getActivity());
					long savedMem = 0;
					for (TaskInfo info : allTaskInfos) {
						if (info.isUserTask() && !getActivity().getPackageName().equals(info.getPackname())) {// 杀死所有的用户进程
							ProcessKiller.killProcess(getActivity(),info.getPackname());
							savedMem += info.getMemsize();
						}
					}
					showToast(getString(R.string.toast_home_kill_process_tip,Formatter.formatFileSize(getActivity(), savedMem)));
					isKillFinish = true;
				}
			});
		}


		@Override
		public void onAccuracyChanged(Sensor sensor, int i) {

		}
	};


	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(sensorEventListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		isRefreshData = false;
	}

	/**
	 * 更新线程，负责不断刷新数据
	 *
	 */
	class UpdateDataThread extends Thread {
		@Override
		public void run() {
			while(isRefreshData) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(availMem != SystemInfoUtils.getAvailMem(context)) {
					updateData(false);
				}
			}
		}


	}
	
	
	/**
	 * 更新数据
	 * @param isAnim 
	 */
	protected void updateData(final boolean isAnim) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				availMem = SystemInfoUtils.getAvailMem(context);
				totalMem = SystemInfoUtils.getTotalMem(context);
				long usedMem = totalMem - availMem;
				
				getAvailStorageSize();
				
				getRemainTraffic();
				
				final int progress = Math.round((((float)usedMem/totalMem)*100));
	
				if(isAnim) {
					int startProgress = progress - 3;
					if(startProgress < 0) {
						startProgress = 0;
					}
					while(startProgress <= progress) {
						
						mHandler.sendMessage(mHandler.obtainMessage(WHAT_CHANGE_PROGRESS, startProgress, 0));
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						startProgress++;
					}
		
				}else {
					mHandler.sendMessage(mHandler.obtainMessage(WHAT_CHANGE_PROGRESS, progress, 0));
				}
			
			}

		};
		GlobalThreadPool.getInstance().execute(r);
	}
	
	
	/**
	 * 获取可用存储大小,单位字节（B）
	 */
	private void getAvailStorageSize() {
		long sdSize = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? 
						StorageUtil.getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath()):0;
		long romSize = StorageUtil.getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
		availStorage = sdSize + romSize;
	}

	/**
	 * 剩余多少流量
	 * @return 返回剩余流量，单位字节（B）
	 */
	private void getRemainTraffic() {
 
		final long trafficCount = sp.getInt(SpKey.KEY_TRAFFIC_COUNT, 0)*1024*1024;
		int startDay = sp.getInt(SpKey.KEY_START_DATE, 1);
		long usedTraffic = trafficInfoBusiness.getCurentMonth(startDay);
		remainTraffic =  (int) (trafficCount - usedTraffic);
	}

	@Override
	protected void onInitData(Bundle savedInstanceState) {
		super.onInitData(savedInstanceState);
		fragmentManager = getActivity().getSupportFragmentManager();
		sp = getActivity().getSharedPreferences(SpKey.TRAFFIC_CONFIG, Activity.MODE_PRIVATE);
		trafficInfoBusiness = new TrafficInfoBusiness(context);
		sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		homeListFragment = new HomeListFragment();
		setCurrentFragment(homeListFragment);
		
	}

	@Override
	protected void findViewById(View rootView) {
		arcProgress = (ArcProgress) rootView.findViewById(R.id.circle_score_view);
		availMemTV = (TextView) rootView.findViewById(R.id.tv_memory_size);
		availStorageTV = (TextView) rootView.findViewById(R.id.tv_storage_size);
		availTrafficTV = (TextView) rootView.findViewById(R.id.tv_traffic_size);
	}


	/**
	 * 切换Fragment
	 * @param fragment
	 */
	private void setCurrentFragment(Fragment fragment) {
		    currentFragment = fragment;
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fl_home_list_container, currentFragment);
			transaction.commit();

	}




}
