package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.CleanCacheActivity.CacheApp;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.BaseRecyleAdapter;
import com.dong.mobilesafe.base.CommonBaseAdapter;

public class ScanCacheAdapter extends BaseRecyleAdapter<ScanCacheAdapter.MyViewHoder,CacheApp> {

	public ScanCacheAdapter(Context context) {
		super(context);
	}

	@Override
	public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context)
				.inflate(R.layout.list_item_cacheinfo, null);
		MyViewHoder hoder = new MyViewHoder(v);
		return hoder;
	}

	@Override
	public void onBindViewHolder(MyViewHoder holder, int position) {
		CacheApp app = mList.get(position);
		holder.appIcon.setImageDrawable(app.icon);
		holder.appName.setText(app.appName);
		holder.cacheSize.setText(Formatter.formatFileSize(context, app.cacheSize));
	}


	static class MyViewHoder extends RecyclerView.ViewHolder {
		ImageView appIcon;
		TextView appName, cacheSize;

		public MyViewHoder(View itemView) {
			super(itemView);
			appIcon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
			appName = (TextView) itemView.findViewById(R.id.tv_app_name);
			cacheSize = (TextView) itemView.findViewById(R.id.tv_cache_size);
		}

	}

}
