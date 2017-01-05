package com.dong.mobilesafe.ui;

import android.content.Context;
import android.widget.TextView;

import com.dong.mobilesafe.R;

public class trafficFloatToast extends FloatToast {
	private TextView trafficSpeed;

	public trafficFloatToast(Context context,int resId) {
		super(context,resId);
	}
	
	@Override
	public void changeContent(String txt) {
		super.changeContent(txt);
		if(trafficSpeed == null) {
			trafficSpeed = (TextView) contentView.findViewById(R.id.tv_traffic_speed);
		}
		trafficSpeed.setText(txt);

	}



}
