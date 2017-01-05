package com.dong.mobilesafe.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.MyLogger;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import butterknife.ButterKnife;

public abstract class BaseFragment2 extends Fragment {
	protected Context context;
	protected View contentView;
	protected ProgressBar mProgressBar;
	protected RelativeLayout progressLayout;
	protected Handler mHandler = new Handler(Looper.getMainLooper());
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		onInitData(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLogger.jLog().d( getClass().getName()+"  onCreateView");
		contentView  = inflater.inflate(onGetContentView(), null);
		ButterKnife.inject(this, contentView);
		addLoadingView();
		findViewById(contentView);
		onPrepareView();
		return contentView;
	}
	


	private void addLoadingView() {
		if(!(contentView instanceof RelativeLayout)) {
			RelativeLayout rl = new RelativeLayout(context);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			rl.addView(contentView, params);
			contentView = rl;
			addProgress();
		}
		addProgress();

	}


	protected void addProgress() {
		mProgressBar = new ProgressBar(context);
		mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading_anim));
		mProgressBar.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);

		progressLayout = new RelativeLayout(context);
		progressLayout.setVisibility(View.GONE);
		progressLayout.setBackgroundColor(Color.WHITE);
		progressLayout.addView(mProgressBar, params);

		((RelativeLayout) contentView).addView(progressLayout, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		progressLayout.bringToFront();
	}

	
	/**
	 * 显示进度条
	 */
	protected void showLoading() {
		if(progressLayout != null) {
			progressLayout.post(new Runnable() {
				@Override
				public void run() {
					progressLayout.setVisibility(View.VISIBLE);
					
				}
			});
			
		}
	}
	
	/**
	 * 隐藏进度条
	 */
	protected void hideLoading() {
		if(progressLayout != null) {
			progressLayout.post(new Runnable() {
				@Override
				public void run() {
					AlphaAnimation apAlphaAnimation = new AlphaAnimation(1.0f, 0f);
					apAlphaAnimation.setDuration(250);
					apAlphaAnimation.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {}
						
						@Override
						public void onAnimationRepeat(Animation animation) {}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							progressLayout.setVisibility(View.GONE);
							
						}
					});
					progressLayout.startAnimation(apAlphaAnimation);
					
				}
			});
			
		}
	}


	/**
	 * 设置控件的属性
	 */
	protected void onPrepareView() {
		
	}

	
	protected  void findViewById(View rootView) {

	}


	public View getRootView(){
		return contentView;
	}

	/**
	 * 描述：Toast提示文本.
	 *
	 * @param text
	 *            文本
	 */
	public void showToast(final String text) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				SuperToast.create(getActivity(), text, SuperToast.Duration.LONG,
						Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
			}
		});

	}

	/**
	 * 描述：Toast提示文本.
	 *
	 * @param mesageId
	 *            文本id
	 */
	public void showToast(int mesageId) {
		showToast(getString(mesageId));
	}

	/**
	 * 初始化界面
	 * @param
	 * @return
	 */
	protected abstract int onGetContentView();


	
	/**
	 * 初始化数据
	 * @param savedInstanceState
	 */
	protected  void onInitData(Bundle savedInstanceState) {
		
	}
	
	
	protected void startActivity(Class<? extends Activity> clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
		
	}

}
