package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.domain.MessageInfo;

public class FromSmsAdapter extends CommonBaseAdapter<MessageInfo> {

	public FromSmsAdapter(Context context) {
		super(context);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.list_item_from_sms_, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.content = (TextView) convertView.findViewById(R.id.tv_sms_content);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		final MessageInfo info = mList.get(position);
		
		holder.name.setText(TextUtils.isEmpty(info.getName())? info.getPhoneNumber() : info.getName());
		holder.content.setText(info.getSmsbody());
		
		return convertView;
	}
	
	
	private static class Holder {
		TextView name,content;
	}

}
