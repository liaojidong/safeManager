package com.dong.mobilesafe;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dong.mobilesafe.adapter.FromSmsAdapter;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.domain.MessageInfo;
import com.dong.mobilesafe.utils.thread.GlobalThreadPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FromSMSActivity extends BaseTitleActivity {
	public static final String RESULT_MESSAGE = "messages";
	private ListView mListView;
	private FromSmsAdapter mAdapter;
	private List<MessageInfo> messageInfos = new ArrayList<MessageInfo>();

	@Override
	protected int onGetContentView() {
		return R.layout.activity_from_sms;
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle(R.string.sms_list);
		mAdapter = new FromSmsAdapter(this);
		mListView = (ListView) findViewById(R.id.lv_from_sms);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(itemClickListener);
	}

	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MessageInfo messageInfo = (MessageInfo) mAdapter.getItem(position);
			complete(messageInfo);
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
				final String SMS_URI_INBOX = "content://sms/inbox";// 收信箱  
			     ContentResolver cr = getContentResolver();  
			     String[] projection = new String[] { "_id", "address", "person","body", "date", "type" };  
			     Uri uri = Uri.parse(SMS_URI_INBOX);  
			     Cursor cursor = cr.query(uri, projection, null, null, "date desc");  
			     while (cursor.moveToNext()) {  
			         	MessageInfo messageInfo = new MessageInfo(); 
			            int nameColumn = cursor.getColumnIndex("person");// 联系人姓名列表序号  
			            int phoneNumberColumn = cursor.getColumnIndex("address");// 手机号  
			            int smsbodyColumn = cursor.getColumnIndex("body");// 短信内容  
			            int dateColumn = cursor.getColumnIndex("date");// 日期  
			            int type = cursor.getColumnIndex("type");	// 收发类型 1表示接受 2表示发送  
			            
			            String name = cursor.getString(nameColumn);  
			            String phoneNumber = cursor.getString(phoneNumberColumn);  
			            String smsbody = cursor.getString(smsbodyColumn);  
			            Date date = new Date(Long.parseLong(cursor.getString(dateColumn)));
			            
			            messageInfo.setPhoneNumber(phoneNumber);
			            messageInfo.setSmsbody(smsbody);
			            messageInfo.setDate(date);
			            messageInfo.setType(type);
			            messageInfo.setName(name);
			            messageInfos.add(messageInfo);
			     }
			     cursor.close();
			     runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						hideLoading();
						if(messageInfos == null || messageInfos.isEmpty()) {
							showTip();
							return;
						}
						mAdapter.replaceList(messageInfos);
					}
				});
				
			}
		});

	}

	private void complete(MessageInfo messageInfo) {
		Intent data = new Intent();
		ArrayList<String> results = new ArrayList<String>();
		results.add(messageInfo.getPhoneNumber());
		data.putStringArrayListExtra(RESULT_MESSAGE,results);
		setResult(RESULT_OK, data);
		finish();
	}
	

}
