package com.dong.mobilesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dong.mobilesafe.utils.SmsUtils;
import com.dong.mobilesafe.utils.SmsUtils.BackUpCallBack;

public class AtoolsActivity extends Activity {
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_advance_tools);
	}
	
	/**
	 * 点击事件，进入号码归属地查询的页面
	 * @param view
	 */
	public void numberQuery(View view){
		Intent intentv = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intentv);
	}
	
	/**
	 * 点击事件，短信的备份
	 * @param view
	 */
	public void smsBackup(View view){
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在备份短信");
		pd.show();
		new Thread(){
			public void run() {
				try {
					SmsUtils.backupSms(getApplicationContext(), new BackUpCallBack() {
						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
						}
						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);
						}
					});
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "备份成功", 0).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "备份失败", 0).show();
						}
					});
				}finally{
					pd.dismiss();
				}
			};
		}.start();
		
	}
	/**
	 * 点击事件，短信的还原
	 * @param view
	 */
	public void smsRestore(View view){
		
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this, "还原成功", 0).show();
	}
}
