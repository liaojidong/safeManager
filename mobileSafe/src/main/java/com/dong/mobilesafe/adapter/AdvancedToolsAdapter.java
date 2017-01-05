package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.domain.AdvancedToolsItem;
import com.dong.mobilesafe.ui.BadgeView;

public class AdvancedToolsAdapter extends CommonBaseAdapter<AdvancedToolsItem> {

	public AdvancedToolsAdapter(Context context) {
		super(context);
		String[]titles = context.getResources().getStringArray(R.array.advanced_tools);
		for(String s:titles) {
			AdvancedToolsItem item = new AdvancedToolsItem();
			item.setName(s);
			mList.add(item);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = View.inflate(context, R.layout.list_item_advanced_tools, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		AdvancedToolsItem item = mList.get(position);
		holder.name.setText(item.getName());
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView icon;
		TextView name;
		BadgeView badgeView;
	}

}
