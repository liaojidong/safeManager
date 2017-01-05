package com.dong.mobilesafe.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.StickyCommonBaseAdapter;
import com.dong.mobilesafe.domain.TaskInfo;
import com.dong.mobilesafe.utils.MyLogger;

public class TaskManagerAdapter extends StickyCommonBaseAdapter<TaskInfo,String>{
	
	public TaskManagerAdapter(Context context) {
		super(context);
		setSections(Arrays.asList(context.getResources().getStringArray(R.array.app_section)));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context,R.layout.list_item_taskinfo, null);
			holder = new ViewHolder();
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_task_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_task_name);
			holder.tv_memsize = (TextView) convertView.findViewById(R.id.tv_task_memsize);
			holder.cb_status = (CheckBox) convertView.findViewById(R.id.cb_status);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		final TaskInfo taskInfo = mList.get(position);
		holder.iv_icon.setImageDrawable(taskInfo.getIcon());
		holder.tv_name.setText(taskInfo.getName());
		holder.tv_memsize.setText("内存占用："+ Formatter.formatFileSize(context,taskInfo.getMemsize()));
		holder.cb_status.setChecked(taskInfo.isChecked());
		if (context.getPackageName().equals(taskInfo.getPackname())) {
			holder.cb_status.setVisibility(View.INVISIBLE);
		} else {
			holder.cb_status.setVisibility(View.VISIBLE);
		}
		return convertView;
	}



	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		MyLogger.jLog().d("getHeaderView = "+position);
	
		TaskInfo info = mList.get(position);
		
		if(info.isUserTask()) {
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

	
	
	private static class HeaderViewHolder {
        TextView text;
    }
	
	
	private static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memsize;
		CheckBox cb_status;
	}
	
	
	@Override
	public void replaceList(List<TaskInfo> list) {
		updateSectionIndices(list);
		super.replaceList(list);
	}

	
	@Override
	protected void updateSectionIndices(List<TaskInfo> list) {
		sectionIndices.clear();
		sectionIndices.add(0);
		for(int i=0;i<list.size();i++){		
			if(!list.get(i).isUserTask()) {
				sectionIndices.add(i);
				break;
			}
		}
	}
	
	
	@Override
	public void removeAll(List<TaskInfo> list) {
		mList.removeAll(list);
		updateSectionIndices(mList);
		this.notifyDataSetChanged();
	}
	
	
	
}
