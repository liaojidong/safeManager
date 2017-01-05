package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.domain.CallLog;
import com.dong.mobilesafe.utils.DateUtil;
import com.dong.mobilesafe.utils.StringUtils;
import com.dong.mobilesafe.utils.date.DateUtils;

public class CallLogAdapter extends CommonBaseAdapter<CallLog> {
	public CallLogAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.list_item_call_log, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.callDate = (TextView) convertView.findViewById(R.id.tv_call_date);
			holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		final CallLog callLog = mList.get(position);
		holder.name.setText(StringUtils.isEmpty(callLog.getCachedName())?callLog.getNumber():callLog.getCachedName());
		holder.callDate.setText(DateUtils.formartCustomTime(context,callLog.getDate().getTime()));
		holder.tv_location.setText(callLog.getLocation());
		return convertView;
	}
	
	
	private static class Holder {
		TextView name,callDate;
		TextView tv_location;
	}

}
