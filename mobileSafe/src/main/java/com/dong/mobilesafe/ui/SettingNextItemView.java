package com.dong.mobilesafe.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.mobilesafe.R;

public class SettingNextItemView extends RelativeLayout {
	private ImageView iconIV;
	private TextView titleTV,hintTV;
	private View divider;
	private boolean isEnable = true;
	
	

	public SettingNextItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.SettingNextView);
		String title = arr.getString(R.styleable.SettingNextView_next_title);
		int resId = arr.getResourceId(R.styleable.SettingNextView_next_icon, -1);
		String hint = arr.getString(R.styleable.SettingNextView_next_hint);
		arr.recycle();

		if(resId == -1) {
			iconIV.setVisibility(View.GONE);
		}else {
			iconIV.setImageResource(resId);
		}

		titleTV.setText(title);
		hintTV.setText(hint);
		
	}

	private void initView() {
		View.inflate(getContext(), R.layout.setting_next_item_view, SettingNextItemView.this);
		iconIV = (ImageView) this.findViewById(R.id.iv_icon);
		titleTV = (TextView) this.findViewById(R.id.tv_title);
		hintTV = (TextView) this.findViewById(R.id.tv_hint);
		divider = this.findViewById(R.id.bottom_divider);
		this.setBackgroundResource(R.drawable.next_item_selector);
	}

	
	
	public void setViewEnabled(boolean isEnable) {
		this.isEnable = isEnable;
		if(isEnable) {
			titleTV.setTextColor(getResources().getColor(R.color.black));
			titleTV.setClickable(false);
			this.setEnabled(true);
		}else {
			titleTV.setTextColor(getResources().getColor(R.color.color_txt));
			titleTV.setClickable(true);
			this.setEnabled(false);
		}
	}
	
	
	
	/**
	 * 设置提示字体的颜色
	 * @param color
	 */
	public void setHintTextColor(int color) {
		hintTV.setTextColor(color);
	}
	
	
	/**
	 * 是否显示分割线
	 * @param isShow
	 */
	public void isShowDivider(boolean isShow) {
		if(isShow) {
			divider.setVisibility(View.VISIBLE);
		}else {
			divider.setVisibility(View.GONE);
		}
	
	}
	
	
	public void setHintTxt(String txt) {
		if(!TextUtils.isEmpty(txt)) {
			hintTV.setText(txt);
		}
	}
	
}
