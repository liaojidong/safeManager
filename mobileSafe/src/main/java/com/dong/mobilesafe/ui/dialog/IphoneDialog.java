package com.dong.mobilesafe.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dong.mobilesafe.R;


public class IphoneDialog {
	private TextView tv;
	private View view;
	private ImageView loading;
	private Dialog dialog;

	public IphoneDialog(Context context) {
		view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		tv = (TextView) view.findViewById(R.id.textinfo);
		loading = (ImageView) view.findViewById(R.id.progressBar1);
		loading.setBackgroundResource(R.drawable.loading_process);
		AnimationDrawable anim = (AnimationDrawable) loading.getBackground();
		anim.start();

		dialog = new Dialog(context, R.style.loading_dialog_style);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);

	}

	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		} else {
			throw new NullPointerException("自定义 dialog 为null，原因可能是没有初始化！");
		}
	}

	public void show() {
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}

	public void setMessage(String message) {
		if (tv != null) {
			tv.setText(message);
		} else {
			throw new NullPointerException("自定义 dialog_loading 中textview 为null，原因可能是没有初始化！");
		}

	}

	public boolean isShowing() {
		return dialog.isShowing();
	}

	public void setOnDismissListener(OnDismissListener dismissListener) {
		dialog.setOnDismissListener(dismissListener);

	}

	public void cancel() {
		dialog.cancel();
	}
}
