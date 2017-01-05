package com.dong.mobilesafe.base;

import android.animation.Animator;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.service.BaseService;
import com.dong.mobilesafe.ui.SlidFinishLayout;
import com.dong.mobilesafe.ui.dialog.IphoneDialog;
import com.dong.mobilesafe.utils.Utils;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 *应用中所有的Activit都继承于该类 
 *
 */
public abstract class BaseActivity extends FragmentActivity{
	protected LayoutInflater inflater;
	// ---------视图界面------------------------
	protected SlidFinishLayout contentLayout;
	protected RelativeLayout loadingLayout;
	protected RelativeLayout tipLayout;
	protected IphoneDialog loadingDialog;
	protected View contentView;
	protected ViewGroup rootView;
	private ViewStub tipVs,progressVs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStateBar();
		onInitData();
		setContentView(R.layout.activity_base);
		initView();
		ButterKnife.inject(this);
		onPrepareView();
		onRequestData();
	}

	/**
	 * 初始化所有的数据，子类可以重写该方法
	 */
	protected void onInitData() {
		initCustomActionBar();
		loadingDialog = new IphoneDialog(this);
		inflater = LayoutInflater.from(this);
	}

	/**
	 * 初始化actionBar
	 */
	private void initCustomActionBar() {
		ActionBar actionBar = getActionBar();
		if (actionBar == null) return;
		if (onActionBarContent() != -1) {
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(onActionBarContent());
		} else {
			actionBar.hide();
		}
	}

	/**
	 * ActionBar布局
	 *
	 * @return
	 */
	protected int onActionBarContent() {
		return -1;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		tipVs = (ViewStub) findViewById(R.id.vs_tip_layout);
		progressVs = (ViewStub) findViewById(R.id.vs_progress_layout);
		contentLayout = (SlidFinishLayout) findViewById(R.id.content_layout);
		contentLayout.setActivity(this);
		rootView = (ViewGroup) findViewById(R.id.rootView);

		// 添加内容布局
		if(getContentView() == null) {
			contentView = inflater.inflate(onGetContentView(), null);
		}else {
			contentView = getContentView();
		}

		contentLayout.addView(contentView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
	}

	/**
	 * 获取内容布局的资源id
	 *
	 * @return 返回需要显示的布局id
	 */
	protected abstract int onGetContentView();

	protected  View getContentView() {
		return null;
	}

	/**
	 * 初始化试图
	 */
	protected void onPrepareView() {
	}

	/**
	 * 请求数据
	 */
	protected void onRequestData() {

	}

	/**
	 * 初始化状态栏
	 */
	private void initStateBar() {
		if (Utils.hasKitkat()) {
			Window win = getWindow();
			win.addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// create our manager instance after the content view is set
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			// enable status bar tint
			tintManager.setStatusBarTintEnabled(onEnableStatusBarTint());
			tintManager.setNavigationBarTintEnabled(false);
			tintManager.setTintResource(R.color.status_bar);
		}

	}


	public boolean onEnableStatusBarTint() {
		return true;
	}

	/**
	 * 描述：Toast提示文本.
	 * 
	 * @param text
	 *            文本
	 */
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SuperToast.create(BaseActivity.this, text, SuperToast.Duration.LONG,
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
	 * 显示默认的提示
	 *
	 */
	public void showTip() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				hideLoading();
				if (tipLayout == null) {
					tipLayout = (RelativeLayout) tipVs.inflate();
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
	 * 隐藏提示
	 */
	public void hideTip() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(tipLayout != null) {
					tipLayout.setVisibility(View.GONE);
				}
			}
		});

	}

	/**
	 * 显示加载进度条
	 */
	protected void showLoading() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (loadingLayout == null) {
					loadingLayout = (RelativeLayout) progressVs.inflate();
					ProgressBar progressBar = (ProgressBar) loadingLayout.findViewById(R.id.progress);
					progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading_anim));
				}
				loadingLayout.setVisibility(View.VISIBLE);
				contentView.setVisibility(View.INVISIBLE);

			}
		});
	}

	/**
	 * 隐藏加载进度条
	 */
	protected void hideLoading() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (loadingLayout != null) {
					loadingLayout.setVisibility(View.GONE);
					contentView.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	/**
	 * 设置是否滑动关闭
	 *
	 * @param isEnableSlidFinish
	 */
	public void setEnableSlidFinish(boolean isEnableSlidFinish) {
		contentLayout.setEnableSlidFinish(isEnableSlidFinish);
	}
	
	/**
	 * 启动服务
	 * @param clazz
	 */
	protected void startService(Class<?> clazz) {
		Intent startIntent = new Intent(this, clazz);
		this.startService(startIntent);
	}
	
	/**
	 * 停止服务
	 * @param clzz
	 */
	protected void stopService(Class<?> clzz) {
		Intent stopIntent = new Intent(BaseService.ACTION_STOP_SERVICE);
		stopIntent.putExtra("serviceName", clzz.getName());
		this.sendBroadcast(stopIntent);
	}
	
	
	protected void startActivity(Class<?> clazz) {
		Intent startIntent = new Intent(this, clazz);
		this.startActivity(startIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
