package com.dong.mobilesafe;

import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;
import com.dong.mobilesafe.db.dao.NumberAddressQueryUtils;

public class NumberAddressQueryActivity extends BaseTitleActivity {
	
	private EditText ed_phone;
	private TextView result;
	private Vibrator vibrator;
	
	

	@Override
	protected int onGetContentView() {
		return R.layout.activity_number_addres_query;
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		result = (TextView) findViewById(R.id.result);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		setBarTitle(R.string.mobile_no_track);
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ed_phone.addTextChangedListener(new TextWatcher() {
			
			/**
			 * 当文本发生变化的时候回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!= null&&s.length()>=3){
					//查询数据库，并且显示结果
					String address = NumberAddressQueryUtils.queryNumber(NumberAddressQueryActivity.this,s.toString());
					result.setText(address);
				}
				
			}
			
			/**
			 * 当文本发生变化之前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * 当文本发生变化之后回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	/**
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone = ed_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", Toast.LENGTH_LONG).show();
			 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 ed_phone.startAnimation(shake);
			 
			 //当电话号码为空的时候，就去振动手机提醒用户
//			 vibrator.vibrate(2000);
			 long[] pattern = {200,200,300,300,1000,2000};
			 //-1不重复 0循环振动 1；
			 vibrator.vibrate(pattern, -1);
			 
			return;
		}else{
			String address = NumberAddressQueryUtils.queryNumber(NumberAddressQueryActivity.this,phone);
			result.setText(address);
			//去数据库查询号码归属地
			//1.网络查询 ；2.本地的数据库--数据库
			//写一个工具类，去查询数据库
			Log.i("NumberAddressQueryActivity", "您要查询的电话号码=="+phone);
		}
		
	}


}
