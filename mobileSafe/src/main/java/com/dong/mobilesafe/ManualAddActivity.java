package com.dong.mobilesafe;

import java.util.ArrayList;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dong.mobilesafe.base.BaseActivity;
import com.dong.mobilesafe.base.BaseTitleActivity;

import butterknife.InjectView;

public class ManualAddActivity extends BaseTitleActivity implements OnClickListener{
	private static final int INTERCEPT_CALL = 1;
	private static final int INTERCEPT_SMS = 2;
	private static final int INTERCEPT_ALL = 3;
	public static final String PARAM_FROM = "from";
	public static final int FROM_BLACK_NUMBER = 1;
	public static final int FROM_WHITE_NUMBER = 2;
	@InjectView(R.id.ll_check)
	LinearLayout ll_check;
	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private int from = FROM_BLACK_NUMBER;//1表示来自黑名单、2表示来自白名单


	@Override
	protected void onInitData() {
		super.onInitData();

	}

	@Override
	protected int onGetContentView() {
		return R.layout.dialog_add_blacknumber;
	}


	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle(R.string.manual_add);
		from = getIntent().getIntExtra(PARAM_FROM,FROM_BLACK_NUMBER);
		if(from == FROM_BLACK_NUMBER) {
			ll_check.setVisibility(View.VISIBLE);
		}else {
			ll_check.setVisibility(View.GONE);
		}
		et_blacknumber = (EditText) contentView.findViewById(R.id.et_blacknumber);
		cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_ok = (Button) contentView.findViewById(R.id.ok);
		bt_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			String number = et_blacknumber.getText().toString().trim();
			if (TextUtils.isEmpty(number)) {
				showToast("黑名单号码不能为空");
				return;
			}
			int mode;
			if(from == FROM_BLACK_NUMBER) {
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					// 全部拦截
					mode = INTERCEPT_ALL;
				} else if (cb_phone.isChecked()) {
					// 电话拦截
					mode = INTERCEPT_CALL;
				} else if (cb_sms.isChecked()) {
					// 短信拦截
					mode = INTERCEPT_SMS;
				} else {
					showToast("请选择拦截模式");
					return;
				}
			}
			ArrayList<String> checkedList = new ArrayList<String>();
			checkedList.add(number);
			Intent data = new Intent();
			data.putStringArrayListExtra("manual", checkedList);
			setResult(RESULT_OK	, data);
			finish();
			break;
		}
	}
	
	

}
