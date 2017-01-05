package com.dong.mobilesafe.adapter;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.domain.DialogItem;

public class CommonDialogListAdapter extends CommonBaseAdapter<DialogItem> {
	private LayoutInflater inflater;
	
	public CommonDialogListAdapter(Context context) {
		super(context);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		    ViewHolder viewHolder;
	       
	        if (convertView == null) {
	        	convertView = inflater.inflate(R.layout.simple_list_item, parent,false);
	            viewHolder = new ViewHolder();
	            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
	            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
	            convertView.setTag(viewHolder);
	        } else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }
	        DialogItem item = mList.get(position);
	        viewHolder.textView.setText(item.getName());
	        if(item.getIcon() != null) {
	        	viewHolder.imageView.setImageDrawable(item.getIcon());
	        }
	        
	        return convertView;
	}
	
	
    private static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

}
