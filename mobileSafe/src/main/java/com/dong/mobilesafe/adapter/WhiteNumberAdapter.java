package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.db.dao.WhiteNumberDao;
import com.dong.mobilesafe.domain.WhiteNumber;

public class WhiteNumberAdapter extends CommonBaseAdapter<WhiteNumber> {
	private WhiteNumberDao whiteNumberDao;

	public WhiteNumberAdapter(Context context) {
		super(context);
		whiteNumberDao = new WhiteNumberDao(context);
	}


	private OnDeleteWhiteNumberListener delteWhiteNumberListener;

	public interface OnDeleteWhiteNumberListener {
		public void OnDeleteWhiteNumber(int position);
	}


	public void setOnDeleteWhiteNumberListener(OnDeleteWhiteNumberListener listener) {
		delteWhiteNumberListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;

		if (convertView == null) {
			view = View.inflate(context,R.layout.list_item_white_number, null);
			holder = new ViewHolder();
			holder.tv_number = (TextView) view.findViewById(R.id.tv_white_number);
			holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
			holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		final WhiteNumber whiteNuber = mList.get(position);
		holder.tv_number.setText(
				TextUtils.isEmpty(whiteNuber.getName()) 
						? whiteNuber.getNumber() : whiteNuber.getName());
		holder.tv_location.setText(whiteNuber.getLocation());
		holder.iv_delete.setOnClickListener(new OnDeleListener(whiteNuber,position,view));
		return view;
	}


	class OnDeleListener implements View.OnClickListener {
		WhiteNumber WhiteNumber;
		int position;
		View view;

		public OnDeleListener(WhiteNumber blackNumber,int position,View view) {
			this.WhiteNumber = blackNumber;
			this.position = position;
			this.view = view;
		}

		@Override
		public void onClick(View v) {

			AlphaAnimation anim = new AlphaAnimation(1.0f, 0f);
			anim.setDuration(300);
			anim.setAnimationListener(new Animation.AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					whiteNumberDao.deleteByNumber(WhiteNumber.getNumber());
					remove(WhiteNumber);
					if(delteWhiteNumberListener != null) {
						delteWhiteNumberListener.OnDeleteWhiteNumber(position);
					}
				}
			});
			view.startAnimation(anim);
		}

	}
	
	

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		TextView tv_location;
		ImageView iv_delete;
	}
	



}
