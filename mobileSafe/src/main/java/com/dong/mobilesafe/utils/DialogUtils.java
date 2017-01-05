package com.dong.mobilesafe.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.adapter.CommonDialogListAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

public class DialogUtils {
	
	
    public static void showOnlyContentDialog(Context context,Holder holder, DialogPlus.Gravity gravity) {
		showOnlyContentDialog(context, holder, gravity, new CommonDialogListAdapter(context), null,null);
    }
    
    
    public static void showOnlyContentDialog(Context context,Holder holder, DialogPlus.Gravity gravity,OnClickListener clickListener) {
		showOnlyContentDialog(context, holder, gravity, new CommonDialogListAdapter(context), clickListener,null);
    }
	
	
    
    public static void showOnlyContentDialog(Context context,Holder holder, DialogPlus.Gravity gravity, BaseAdapter adapter,
            OnItemClickListener itemClickListener) {
    	showOnlyContentDialog(context, holder, gravity, adapter, null, itemClickListener);
    }
    
    
    public static void showOnlyContentDialog(Context context,Holder holder, DialogPlus.Gravity gravity, BaseAdapter adapter,
    		OnClickListener clickListener, OnItemClickListener itemClickListener) {
			final DialogPlus dialog = new DialogPlus.Builder(context)
			.setContentHolder(holder)
			.setCancelable(true)
			.setGravity(gravity)
			.setAdapter(adapter)
			.setOnClickListener(clickListener)
			.setOnItemClickListener(itemClickListener)
			.create();
			dialog.show();
    }
    
    
    
    public static void showNoFooterDialog(Context context,String title,Holder holder, DialogPlus.Gravity gravity, BaseAdapter adapter,
            OnClickListener clickListener, OnItemClickListener itemClickListener) {
    		View header = View.inflate(context, R.layout.dialog_header, null);
    		TextView titleTV = (TextView) header.findViewById(R.id.tv_dialog_title);
    		titleTV.setText(title);
    		final DialogPlus dialog = new DialogPlus.Builder(context)
			.setContentHolder(holder)
			.setHeader(header)
			.setCancelable(true)
			.setGravity(gravity)
			.setAdapter(adapter)
			.setOnClickListener(clickListener)
			.setOnItemClickListener(itemClickListener)
			.create();
			 dialog.show();
    }


	/**
	 * 提示框
	 *
	 * @param context
	 * @param tip
	 * @param gravity
	 */
	public static void showTipDialog(Context context, String tip, DialogPlus.Gravity gravity) {
		showTipDialog(context, tip, null, gravity);
	}

	/**
	 * 提示框
	 *
	 * @param context
	 * @param tip
	 * @param gravity
	 */
	public static void showTipDialog(Context context, String tip, String btnTxt, DialogPlus.Gravity gravity) {
		View contentView = View.inflate(context, R.layout.dialog_tip, null);
		ViewHolder holder = new ViewHolder(contentView);
		final DialogPlus dialog =
				new DialogPlus.Builder(context).setContentHolder(holder)
						.setBackgroundColorResourceId(R.drawable.transparent)
						.setCancelable(false)
						.setGravity(gravity)
						.create();

		TextView dialogTip = (TextView) contentView.findViewById(R.id.tv_dialog_tip);
		Button ok = (Button) contentView.findViewById(R.id.ok);
		if (!TextUtils.isEmpty(btnTxt)) {
			ok.setText(btnTxt);
		}
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialogTip.setText(tip);
		dialog.show();
	}


}
