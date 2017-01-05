package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.domain.RubbishSms;
import com.dong.mobilesafe.utils.DateUtil;


public class RubbishSmsAdapter extends CommonBaseAdapter<RubbishSms> {

	public RubbishSmsAdapter(Context context) {
		super(context);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holer = null;
		if(convertView == null) {
			holer = new ViewHolder();
			convertView = View.inflate(context, R.layout.list_item_rubbish_sms, null);
			holer.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
			holer.tv_Receive_time = (TextView) convertView.findViewById(R.id.tv_receiver_time);
			holer.tv_content = (TextView) convertView.findViewById(R.id.tv_sms_content);
			convertView.setTag(holer);
		}else {
			holer = (ViewHolder) convertView.getTag();
		}
		RubbishSms sms = mList.get(position);
		holer.tv_number.setText(sms.getNumber());
		holer.tv_Receive_time.setText(DateUtil.toYYYYMMDD(sms.getReceiveTime()));
		holer.tv_content.setText(sms.getMsgBaby());
		return convertView;
	}

	
	static class ViewHolder {
		TextView tv_number;
		TextView tv_Receive_time;
		TextView tv_content;
	}


}
