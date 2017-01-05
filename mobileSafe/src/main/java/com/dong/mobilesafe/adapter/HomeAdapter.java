package com.dong.mobilesafe.adapter;

import org.xclcharts.common.DensityUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.constant.SpKey;
import com.dong.mobilesafe.ui.BadgeView;
import com.dong.mobilesafe.utils.log.LogUtils;

public class HomeAdapter extends BaseAdapter {
	private Context context;
	private int padding;
	private int unReadInfoCount = 0; //还没有查阅的拦截信息条数

	private static String [] names;
	private SharedPreferences sp;
	private static final int[] ids = {
		R.drawable.home_list_lock,R.drawable.home_list_communication,
		R.drawable.home_list_progress,R.drawable.home_list_traffic,
		R.drawable.home_list_virus,R.drawable.home_list_cache };
	private OnHomeListItemClickListener itemClickListener;

	public interface OnHomeListItemClickListener {
		public void onItemClick(int position);
	}

	public void setOnHomeListItemClickListener(OnHomeListItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public HomeAdapter(Context context) {
		this.context = context;
		names = context.getResources().getStringArray(R.array.home_names);
		sp = context.getSharedPreferences(SpKey.TRAFFIC_CONFIG, Context.MODE_PRIVATE);
		padding = DensityUtil.dip2px(context, 1);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = View.inflate(context, R.layout.list_item_home, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_item);
			holder.name = (TextView) convertView.findViewById(R.id.tv_item);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_home_item);
			holder.rippleView = (RippleView) convertView.findViewById(R.id.rippleView);
            holder.badgeViewRL = (RelativeLayout) convertView.findViewById(R.id.rl_badge);
            
            holder.bv =  new BadgeView(context, holder.badgeViewRL);
            holder.bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            holder.bv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
            
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ll.setPadding(0, 0, 0, 0);
		holder.name.setText(names[position]);
		holder.icon.setImageResource(ids[position]);
		holder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
			@Override
			public void onComplete(RippleView rippleView) {
				LogUtils.jLog().d("rippleView click");
				if(itemClickListener == null) return;
				itemClickListener.onItemClick(position);
			}
		});
		
		switch (position) {
		case 0://手机防盗
			holder.ll.setPadding(0, 0, 0, 0);
			holder.bv.hide();
			break;
			
		case 1:// 通讯卫士
			holder.ll.setPadding(padding, 0, 0, 0);
			if(unReadInfoCount<=0) {
				holder.bv.hide(true);
			}else {
				holder.bv.setText(unReadInfoCount+"");
				holder.bv.show();
			}
			break;
			
		case 2://进程管理
			holder.ll.setPadding(0, padding, 0, 0);
			holder.bv.hide();
			break;
				
		case 3://流量统计
			holder.ll.setPadding(padding, padding, 0, 0);
			if(sp.getBoolean(SpKey.KEY_IS_SETTING, false)) {
				holder.bv.hide();
			}else {
				holder.bv.setText(context.getString(R.string.not_setting));
				holder.bv.show();
			}
			
			break;
		case 4://手机杀毒
			holder.ll.setPadding(0, padding, 0, padding);
			holder.bv.hide();
			break;
			
		case 5://缓存清理
			holder.ll.setPadding(padding, padding, 0, padding);
			holder.bv.hide();
			break;
		}
		
		return convertView;
	}
	
	
	private static class ViewHolder {
		ImageView icon;
		TextView name;
		LinearLayout ll;
		RelativeLayout badgeViewRL;
		BadgeView bv;
		RippleView rippleView;
	}


	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return names[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setUnReadInfoCount(int unReadInfoCount) {
		this.unReadInfoCount = unReadInfoCount;
		notifyDataSetChanged();
	}

}
