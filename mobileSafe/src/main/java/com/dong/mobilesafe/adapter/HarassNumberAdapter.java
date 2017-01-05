package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.mobilesafe.NumberAddressQueryActivity;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.dong.mobilesafe.domain.HarassCall;
import com.dong.mobilesafe.utils.DateUtil;


public class HarassNumberAdapter extends CommonBaseAdapter<HarassCall> {

	public HarassNumberAdapter(Context context) {
		super(context);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holer = null;
		if(convertView == null) {
			holer = new ViewHolder();
			convertView = View.inflate(context, R.layout.list_item_harass_number, null);
			holer.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
			holer.tv_Receive_time = (TextView) convertView.findViewById(R.id.tv_receiver_time);
			holer.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
			convertView.setTag(holer);
		}else {
			holer = (ViewHolder) convertView.getTag();
		}
		HarassCall call = mList.get(position);
		holer.tv_number.setText(call.getNumber());
		holer.tv_Receive_time.setText(DateUtil.toYYYYMMDD(call.getCallTime()));
		holer.tv_location.setText(NumberAddressQueryUtils.queryNumber(context,call.getNumber()));
		return convertView;
	}

	
	static class ViewHolder {
		TextView tv_number;
		TextView tv_Receive_time;
		TextView tv_location;
	}


}
