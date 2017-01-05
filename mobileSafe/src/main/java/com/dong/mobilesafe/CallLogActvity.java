package com.dong.mobilesafe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dong.mobilesafe.adapter.CallLogAdapter;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogActvity extends BaseTitleActivity{
	public static final String RESULT_CALLLOGS = "callLogs";
	private ListView mListView;
	private CallLogAdapter mAdapter;
	private List<com.dong.mobilesafe.domain.CallLog> mList =new ArrayList<com.dong.mobilesafe.domain.CallLog>();

	@Override
	protected int onGetContentView() {
		return R.layout.activity_call_log;
	}

	
	@Override
	protected void onInitData() {
		super.onInitData();
		mAdapter = new CallLogAdapter(CallLogActvity.this);
	}


	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle(R.string.call_records);
		mListView = (ListView) findViewById(R.id.lv_call_log);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(itemClickListener);
	}


	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			com.dong.mobilesafe.domain.CallLog callLog = (com.dong.mobilesafe.domain.CallLog) mAdapter.getItem(position);
			complete(callLog);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillData();
	}

	private void fillData() {
		showLoading();
		GlobalThreadPool.getInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				Cursor cursor = null;
				try {
					cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[] {
							CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
							CallLog.Calls.TYPE, CallLog.Calls.DATE }, null, null,
							CallLog.Calls.DEFAULT_SORT_ORDER);
					while(cursor.moveToNext()) {
						com.dong.mobilesafe.domain.CallLog callLog = new com.dong.mobilesafe.domain.CallLog();
						callLog.setCachedName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
						callLog.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
						callLog.setType(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
						callLog.setDate(new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))));
						callLog.setLocation(NumberAddressQueryUtils.queryNumber(CallLogActvity.this,callLog.getNumber()));
						mList.add(callLog);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							hideLoading();
							if(mList == null || mList.isEmpty()) {
								showTip();
								return;
							}
							mAdapter.replaceList(mList);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if(cursor != null) {
						cursor.close();
					}
				}
				
			}
		});
		
	}





	public void complete(com.dong.mobilesafe.domain.CallLog callLog) {
		Intent data = new Intent();
		ArrayList<String> results = new ArrayList<>();
		results.add(callLog.getNumber());
		data.putStringArrayListExtra(RESULT_CALLLOGS, results);
		setResult(RESULT_OK	, data);
		finish();
		
	}

}
