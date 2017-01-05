package com.dong.mobilesafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * 我们自定义的组合控件，它里面有两个TextView ，还有一个CheckBox,还有一个View
 *
 */
public class SettingItemView extends RelativeLayout implements OnCheckedChangeListener{
	private SwitchButton cb_status;
	private TextView tv_title;
	private onOnOffListener mListener;
	private boolean isEnable = true;
	
	public interface onOnOffListener {
		public void onChange(View view ,boolean isCheced);
	}
	
	
	/**
	 * 初始化布局文件
	 * @param context
	 */
	private void iniView(Context context) {
		
		//把一个布局文件---》View 并且加载在SettingItemView
		View.inflate(context, R.layout.setting_item_view, SettingItemView.this);

		cb_status = (SwitchButton) this.findViewById(R.id.cb_status);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		cb_status.setOnCheckedChangeListener(this);
		this.setBackgroundColor(Color.WHITE);
	}
	
	

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
		TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.SettingView);
		String title = arr.getString(R.styleable.SettingView_item_title);
		arr.recycle();
		tv_title.setText(title);
	}
	
	
	
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.SettingView);
		String title = arr.getString(R.styleable.SettingView_item_title);
		arr.recycle();
		tv_title.setText(title);
	}

	

	public SettingItemView(Context context) {
		super(context);
		iniView(context);
	}
	
	/**
	 * 校验组合控件是否选中
	 */
	
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	
	
	/**
	 * 设置组合控件的状态
	 */
	public void setChecked(boolean checked){
		if(checked != cb_status.isChecked()) {
			cb_status.setChecked(checked);
		}
	}
	


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(mListener != null ) {
			mListener.onChange(this,isChecked);
		}
		
	}

	
	public void setOnOnOffListener(onOnOffListener mListener) {
		this.mListener = mListener;
	}



	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
		if(isEnable) {
			tv_title.setTextColor(getResources().getColor(R.color.black));
			cb_status.setEnabled(true);
		}else {
			tv_title.setTextColor(getResources().getColor(R.color.color_txt));
			cb_status.setEnabled(false);
		}
	}
	


}
