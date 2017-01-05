package com.dong.mobilesafe.base;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dong.mobilesafe.R;

public abstract class BaseTitleActivity extends BaseActivity {
	protected TextView titleTV;
	protected ImageView backIcon;
	protected LinearLayout rightIconLayout;
	protected ActionBar actionBar;

	@Override
	protected int onActionBarContent() {
		return R.layout.custom_action_bar;
	}

	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		actionBar = getActionBar();
		if (actionBar == null) return;
		titleTV = (TextView) actionBar.getCustomView().findViewById(R.id.bar_title);
		backIcon = (ImageView) actionBar.getCustomView().findViewById(R.id.bar_icon_back);
		rightIconLayout = (LinearLayout) actionBar.getCustomView().findViewById(R.id.title_bar_right_layout);
		backIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseTitleActivity.this.finish();
			}
		});

	}

	protected void setBarBg(int color) {
		View v = actionBar.getCustomView().findViewById(R.id.rl_bar);
		v.setBackgroundColor(color);
	}


	protected void setBarTitle(String title) {
		if(titleTV == null) return;
		titleTV.setText(title);
	}

	protected void setBarTitle(int resid) {
		if(titleTV != null) {
			titleTV.setText(resid);
		}
	}

	protected void setBackIcon(Drawable icon) {
		if(backIcon == null) return;
		backIcon.setImageDrawable(icon);
		if(icon == null) {
			backIcon.setOnClickListener(null);
		}else {
			backIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseTitleActivity.this.finish();
				}
			});
		}
	}

	protected void setBackIcon(Drawable icon, OnClickListener listener) {
		backIcon.setImageDrawable(icon);
		backIcon.setOnClickListener(listener);
	}

	protected void setBackIconRes(int resId) {
		backIcon.setImageResource(resId);
		backIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseTitleActivity.this.finish();
			}
		});
	}


	/**
	 * 隐藏返回按钮
	 */
	protected void hideBackIcon() {
		if (backIcon != null) {
			backIcon.setVisibility(View.GONE);
		}
	}


	protected void setRightIcon(Drawable icon, OnClickListener listener) {
		rightIconLayout.removeAllViews();
		ImageView rightIcon = new ImageView(this);
		rightIcon.setScaleType(ScaleType.CENTER_INSIDE);
		rightIcon.setImageDrawable(icon);
		rightIcon.setOnClickListener(listener);
		rightIconLayout.addView(rightIcon, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	protected void setRightText(String text) {
		rightIconLayout.removeAllViews();
		TextView rightText = new TextView(this);
		rightText.setGravity(Gravity.CENTER);
		rightText.setText(text);
		rightIconLayout.addView(rightText, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	protected void setRightText(String text, OnClickListener listener) {
		rightIconLayout.removeAllViews();
		TextView rightText = new TextView(this);
		rightText.setPadding(
				getResources().getDimensionPixelSize(R.dimen.title_bar_padding), 0,
				getResources().getDimensionPixelSize(R.dimen.title_bar_padding), 0);
		rightText.setTextColor(Color.WHITE);
		rightText.setOnClickListener(listener);
		rightText.setGravity(Gravity.CENTER);
		rightText.setText(text);
		rightIconLayout.addView(rightText, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	/**
	 * 传入自定义view
	 * @param view
	 */
	protected void setRightView(View view) {
		rightIconLayout.removeAllViews();
		rightIconLayout.addView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	protected void setRightIconRes(int resId, OnClickListener listener) {
		setRightIcon(getResources().getDrawable(resId), listener);
	}

}
