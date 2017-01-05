package com.dong.mobilesafe.base;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.utils.MyLogger;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
	protected Context context;
	private LayoutInflater inflater;
	protected View rootView;
	protected View contentView;

	protected ViewGroup contentLayout;
	protected RelativeLayout loadingLayout;
	protected ViewGroup tipLayout;
	private ViewStub tipVs, progressVs;

	protected Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		inflater = LayoutInflater.from(context);
		onInitData(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView  = inflater.inflate(R.layout.fragment_base, null);
		initView();
		ButterKnife.inject(this, rootView);
		findViewById(rootView);
		onPrepareView();
		return rootView;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		tipVs = (ViewStub) rootView.findViewById(R.id.vs_tip_layout);
		progressVs = (ViewStub) rootView.findViewById(R.id.vs_progress_layout);
		contentLayout = (ViewGroup) rootView.findViewById(R.id.content_layout);

		// 添加内容布局
		contentView = inflater.inflate(onGetContentView(), null);
		contentLayout.addView(contentView, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
	}


	/**
	 * 显示默认的提示
	 *
	 */
	public void showTip() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
					hideLoading();
					if (tipLayout == null) {
						tipLayout = (ViewGroup) tipVs.inflate();
					}
					if(tipLayout.getVisibility() != View.VISIBLE) {
						tipLayout.animate().alpha(1.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
							@Override
							public void onAnimationStart(Animator animator) {
							}

							@Override
							public void onAnimationEnd(Animator animator) {
								tipLayout.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationCancel(Animator animator) {
								tipLayout.setVisibility(View.VISIBLE);
							}

							@Override
							public void onAnimationRepeat(Animator animator) {
							}
						}).start();
					}
			}
		});

	}


	/**
	 * 显示带有文本和图片的提示
	 *
	 * @param text  提示文本
	 * @param resid 显示的图片
	 */
	public void showTip(final String text, final int resid) {
		hideLoading();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (tipLayout == null ) {
					tipLayout = (ViewGroup) tipVs.inflate();
				}
				TextView tipTV = (TextView) tipLayout.findViewById(R.id.tv_tip);
				ImageView tipIV = (ImageView) tipLayout.findViewById(R.id.iv_tip);
				tipLayout.animate().alpha(1.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
					}
					@Override
					public void onAnimationEnd(Animator animator) {
						tipLayout.setVisibility(View.VISIBLE);
					}
					@Override
					public void onAnimationCancel(Animator animator) {
						tipLayout.setVisibility(View.VISIBLE);
					}
					@Override
					public void onAnimationRepeat(Animator animator) {
					}
				}).start();
				if (!TextUtils.isEmpty(text)) {
					tipTV.setText(text);
				}
				if (resid != 0) {
					tipIV.setImageResource(resid);
				}
			}
		});

	}


	/**
	 * 隐藏提示
	 */
	public void hideTip() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (tipLayout != null && tipLayout.getVisibility() == View.VISIBLE) {
					tipLayout.animate().alpha(0f).setDuration(250).setListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animator) {
						}
						@Override
						public void onAnimationEnd(Animator animator) {
							tipLayout.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationCancel(Animator animator) {
							tipLayout.setVisibility(View.GONE);
						}
						@Override
						public void onAnimationRepeat(Animator animator) {
						}
					}).start();
					contentLayout.animate().alpha(1f).setDuration(250).start();
				}

			}
		});

	}


	/**
	 * 显示加载进度条
	 */
	protected void showLoading() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (loadingLayout == null) {
					loadingLayout = (RelativeLayout) progressVs.inflate();
				}
				loadingLayout.setVisibility(View.VISIBLE);
				loadingLayout.animate().alpha(1.0f).setDuration(250).start();
				contentView.setVisibility(View.INVISIBLE);


			}
		});
	}

	/**
	 * 隐藏加载进度条
	 */
	protected void hideLoading() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (loadingLayout != null && loadingLayout.getVisibility() == View.VISIBLE) {
					loadingLayout.setVisibility(View.GONE);
					contentView.setVisibility(View.VISIBLE);
					contentView.animate().alpha(1.0f).setDuration(250).start();
				}
			}
		});

	}

	/**
	 * 设置控件的属性
	 */
	protected void onPrepareView() {
		
	}

	
	protected  void findViewById(View rootView) {

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
