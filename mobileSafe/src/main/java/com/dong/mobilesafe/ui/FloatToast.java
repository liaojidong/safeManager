package com.dong.mobilesafe.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public  class FloatToast implements OnTouchListener{
	protected WindowManager.LayoutParams  mParams;
	protected View contentView;
	protected WindowManager mWM;
	protected SharedPreferences sp;
	protected int screenWidth,screenHeight;
	
	public FloatToast(Context context,int  resId) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		this.mParams = 	 createParams();
		this.contentView = View.inflate(context, resId, null);
		this.mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		this.mWM.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		contentView.setOnTouchListener(this);
	}
	


	public  WindowManager.LayoutParams createParams() {
		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
		final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        params.x = sp.getInt("lastx", 0);
        params.y = sp.getInt("lasty", 0);
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        
        return mParams;
	}
	
	
	public void changeContent(String txt) {
		
	}
	
	
	
	public  void showToast() {
		mWM.addView(contentView, mParams);
	}
	
	public  void hideToast() {
		mWM.removeView(contentView);
	}



	private int startX;
	private int startY;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// 手指按下屏幕
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动
			int newX = (int) event.getRawX();
			int newY = (int) event.getRawY();
			int dx = newX - startX;
			int dy = newY - startY;
			mParams.x += dx;
			mParams.y += dy;
			// 考虑边界问题
			if (mParams.x < 0) {
				mParams.x = 0;
			}
			if (mParams.y < 0) {
				mParams.y = 0;
			}
			if (mParams.x > (screenWidth - contentView.getWidth())) {
				mParams.x = (screenWidth - contentView.getWidth());
			}
			if (mParams.y > (screenHeight - contentView.getHeight())) {
				mParams.y = (screenHeight - contentView.getHeight());
			}
			mWM.updateViewLayout(contentView, mParams);
			// 重新初始化手指的开始结束位置。
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_UP:// 手指离开屏幕一瞬间
			// 记录控件距离屏幕左上角的坐标
			Editor editor = sp.edit();
			editor.putInt("lastx", mParams.x);
			editor.putInt("lasty", mParams.y);
			editor.commit();
			break;
		}
		return true;
	}
	

}
