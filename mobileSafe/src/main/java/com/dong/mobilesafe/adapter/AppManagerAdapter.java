package com.dong.mobilesafe.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.StickyCommonBaseAdapter;
import com.dong.mobilesafe.domain.AppInfo;

public class AppManagerAdapter extends StickyCommonBaseAdapter<AppInfo,String> {	

	public AppManagerAdapter(Context context) {
		super(context);
		setSections(Arrays.asList(context.getResources().getStringArray(R.array.app_type)));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if (convertView != null ) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(context,R.layout.list_item_appinfo, null);
			holder = new ViewHolder();
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
			holder.tv_location = (TextView) convertView.findViewById(R.id.tv_app_location);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_app_name);
			holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
			convertView.setTag(holder);
		}
		
		final AppInfo appInfo = mList.get(position);
		holder.iv_icon.setImageDrawable(appInfo.getIcon());
		holder.tv_name.setText(appInfo.getName());
		if (appInfo.isInRom()) {
			holder.tv_location.setText("手机内存");
		} else {
			holder.tv_location.setText("外部存储");
		}
		if(appInfo.isLock()){
			holder.iv_status.setImageResource(R.drawable.lock);
		}else{
			holder.iv_status.setImageResource(R.drawable.unlock);
		}
		return convertView;
	}
	
	
	public static class ViewHolder {
		public TextView tv_name;
		public TextView tv_location;
		public ImageView iv_icon;
		public ImageView iv_status;
	}



	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		AppInfo info = mList.get(position);
		
		if(info.isUserApp()) {
			position = 0;
		}else {
			position = 1;
			   
		}
      		
		HeaderViewHolder holder;
	    if (convertView == null) {
	        holder = new HeaderViewHolder();
	        convertView = mInflater.inflate(R.layout.header, parent, false);
	        holder.text = (TextView) convertView.findViewById(R.id.text1);
	        convertView.setTag(holder);
	     } else {
	         holder = (HeaderViewHolder) convertView.getTag();
	     }
	 
	     CharSequence headerChar = sections.get(position);
	     holder.text.setText(headerChar);
	     return convertView;
	}

	@Override
	protected void updateSectionIndices(List<AppInfo> list) {
		sectionIndices.clear();
		sectionIndices.add(0);
		for(int i=0;i<list.size();i++){		
			if(!list.get(i).isUserApp()) {
				sectionIndices.add(i);
				break;
			}
		}
	}
	
	private static class HeaderViewHolder {
        TextView text;
    }
	
	
	@Override
	public void replaceList(List<AppInfo> list) {
		updateSectionIndices(list);
		super.replaceList(list);
	}
	
}
