package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.db.dao.BlackNumberDao;
import com.dong.mobilesafe.domain.BlackNumber;

public class BlackNumberAdapter extends CommonBaseAdapter<BlackNumber> {
	private static final int INTERCEPT_CALL = 1;
	private static final int INTERCEPT_SMS = 2;
	private static final int INTERCEPT_ALL = 3;
	private BlackNumberDao blackNumberDao;
	static final int ANIMATION_DURATION = 250;
	private OnDelteBlackNumberListener delteBlackNumberListener;


	public interface OnDelteBlackNumberListener {
		public void OnDeleteBlackNumber(int position);
	}


	public void setOndelteBlackNumberListener(OnDelteBlackNumberListener listener) {
		this.delteBlackNumberListener = listener;
	}

	public BlackNumberAdapter(Context context) {
		super(context);
		blackNumberDao = new BlackNumberDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;

		if (convertView == null) {
			view = View.inflate(context,R.layout.list_item_callsms, null);
			holder = new ViewHolder();
			holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
			holder.tv_mode = (TextView) view.findViewById(R.id.tv_block_mode);
			holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		final BlackNumber blackNumber = mList.get(position);
		holder.tv_number.setText(
				TextUtils.isEmpty(blackNumber.getName()) 
						? blackNumber.getNumber() : blackNumber.getName());
		holder.tv_location.setText(blackNumber.getLocation());
		int mode = blackNumber.getMode();
		switch (mode) {
		case INTERCEPT_CALL:
			holder.tv_mode.setText("电话拦截");
			break;
		case INTERCEPT_SMS:
			holder.tv_mode.setText("短信拦截");
			break;
		case INTERCEPT_ALL:
			holder.tv_mode.setText("全部拦截");
			break;
		}
		holder.iv_delete.setOnClickListener(new OnDeleListener(blackNumber,position,view));
		return view;
	}
	
	
	
	class OnDeleListener implements OnClickListener {
		BlackNumber blackNumber;
		int position;
		View view;
		
		public OnDeleListener(BlackNumber blackNumber,int position,View view) {
			this.blackNumber = blackNumber;
			this.position = position;
			this.view = view;
		}

		@Override
		public void onClick(View v) {
			
			AlphaAnimation anim = new AlphaAnimation(1.0f, 0f);
			anim.setDuration(300);
			anim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					blackNumberDao.deleteByNumber(blackNumber.getNumber());
					remove(blackNumber);
					if(delteBlackNumberListener != null) {
						delteBlackNumberListener.OnDeleteBlackNumber(position);
					}
				}
			});
			view.startAnimation(anim);
		}
		
		
	}
	

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
		TextView tv_location;
	}
	




}
