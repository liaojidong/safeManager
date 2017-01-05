package com.dong.mobilesafe;

import android.content.Intent;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.adapter.TaskManagerAdapter;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.domain.TaskInfo;
import com.dong.mobilesafe.engine.TaskInfoProvider;
import com.dong.mobilesafe.utils.Process.ProcessKiller;
import com.dong.mobilesafe.utils.SystemInfoUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class TaskManagerActivity extends BaseTitleActivity implements OnClickListener,OnItemClickListener{
	private StickyListHeadersListView taskListView;


	private Button killALl;
	
	private TaskManagerAdapter mAdapter;
	private int processCount;
	private long availMem;
	private long totalMem;


	@Override
	protected void onInitData() {
		super.onInitData();
		mAdapter = new TaskManagerAdapter(TaskManagerActivity.this);
	}
	
	
	@Override
	protected int onGetContentView() {
		return R.layout.activity_task_manager;
	}
	

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle(R.string.task_manager);

		taskListView = (StickyListHeadersListView) findViewById(R.id.lv_task_manager);
		killALl = (Button) findViewById(R.id.btn_kill_all);

		killALl.setOnClickListener(this);
		taskListView.setOnItemClickListener(this);
		taskListView.setAdapter(mAdapter);
	}

	private void setTitle() {
		processCount = SystemInfoUtils.getRunningProcessCount(this);
		availMem = SystemInfoUtils.getAvailMem(this);
		totalMem = SystemInfoUtils.getTotalMem(this);
	}


	@Override
	protected void onStart() {
		super.onStart();
		fillData();
	}

	/**
	 * 填充数据
	 */
	private void fillData() {
		showLoading();
		Runnable run = new Runnable() {
			public void run() {
				final List<TaskInfo> allTaskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
				List<TaskInfo> userTask = new ArrayList<TaskInfo>();
				List<TaskInfo> sysTask = new ArrayList<TaskInfo>();
				for(TaskInfo t:allTaskInfos) {
					if(t.isUserTask()) {
						userTask.add(t);
					}else {
						sysTask.add(t);
					}
				}
				allTaskInfos.clear();
				allTaskInfos.addAll(userTask);
				allTaskInfos.addAll(sysTask);
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideLoading();
						mAdapter.replaceList(allTaskInfos);
						setTitle();
						selectAllUserTask();
					}
				});
			};
		};
		GlobalThreadPool.getInstance().execute(run);
	}

	

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memsize;
		CheckBox cb_status;
	}

	
	/**
	 * 选中全部用户进程
	 * 
	 */
	public void selectAllUserTask() {
		for (TaskInfo info : mAdapter.getAllItems()) {
			if (getPackageName().equals(info.getPackname())) { //排除自己
				continue;
			}
			if(info.isUserTask()) {
				info.setChecked(true);
			}else {
				info.setChecked(false);
			}
		
		}
		mAdapter.notifyDataSetChanged();
	}


	/**
	 * 一键清理
	 *
	 */
	public void killAll() {
		int count = 0;
		long savedMem = 0;
		// 记录那些被杀死的条目
		List<TaskInfo> killedTaskinfos = new ArrayList<TaskInfo>();
		for (TaskInfo info : mAdapter.getAllItems()) {
			if (info.isChecked()) {// 被勾选的，杀死这个进程。
				ProcessKiller.killProcess(this,info.getPackname());
				killedTaskinfos.add(info);
				count++;
				savedMem += info.getMemsize();
			}
		}
		mAdapter.removeAll(killedTaskinfos);
		showToast(getString(R.string.toast_kill_process_tip,count,Formatter.formatFileSize(this, savedMem)));;
		processCount -= count;
		availMem += savedMem;
	}


	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_kill_all:
			killAll();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TaskInfo info = (TaskInfo) mAdapter.getItem(position);
		if(!getPackageName().equals(info.getPackname())){
			info.setChecked(!info.isChecked());
			mAdapter.notifyDataSetChanged();
		}
	}
}
