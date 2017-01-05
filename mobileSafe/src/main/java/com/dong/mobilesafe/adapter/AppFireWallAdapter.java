package com.dong.mobilesafe.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.CommonBaseAdapter;
import com.dong.mobilesafe.utils.Api;
import com.dong.mobilesafe.utils.Api.DroidApp;
import com.dong.mobilesafe.utils.MyLogger;
import com.kyleduo.switchbutton.SwitchButton;

public class AppFireWallAdapter extends CommonBaseAdapter<DroidApp> implements OnCheckedChangeListener{

	public AppFireWallAdapter(Context context) {
		super(context);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
				convertView = View.inflate(context,R.layout.list_item_fire_wall,null);
				holder = new Holder();
				holder.box_wifi = (SwitchButton) convertView.findViewById(R.id.itemcheck_wifi);
				holder.box_3g = (SwitchButton) convertView.findViewById(R.id.itemcheck_3g);
				holder.text = (TextView) convertView.findViewById(R.id.tv_app_name);
				holder.icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
				convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final DroidApp app = mList.get(position);
		
		holder.box_wifi.setOnCheckedChangeListener(this);
		holder.box_3g.setOnCheckedChangeListener(this);
		
		holder.text.setText(app.name);
		holder.icon.setImageDrawable(app.icon);
		

		holder.box_wifi.setTag(app);
		holder.box_wifi.setChecked(app.selected_wifi);
		
		holder.box_3g.setTag(app);
		holder.box_3g.setChecked(app.selected_3g);
		
		return convertView;
	}


	
	private static class Holder {
		private SwitchButton box_wifi;
		private SwitchButton box_3g;
		private TextView text;
		private ImageView icon;
	}



	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final DroidApp app = (DroidApp) buttonView.getTag();
		MyLogger.jLog().d("isChecked = "+isChecked);
		if (app != null) {
			switch (buttonView.getId()) {
				case R.id.itemcheck_wifi:
					
					if(!app.selected_wifi && isChecked) {
						applyOrSaveRules(buttonView);
					}
					if(app.selected_wifi && !isChecked) {
						applyOrSaveRules(buttonView);
					}
					
					app.selected_wifi = isChecked;
					break;
					
				case R.id.itemcheck_3g: 
					
					if(!app.selected_3g && isChecked) {
						applyOrSaveRules(buttonView);
					}
					
					if(app.selected_3g && !isChecked) {
						applyOrSaveRules(buttonView);
					}
					
					app.selected_3g = isChecked;
					break;
			}
		}
	}
	
	
	
	/**
	 * Apply or save iptable rules, showing a visual indication
	 */
	private void applyOrSaveRules(final CompoundButton buttonView) {

		new MyAsyncTask(buttonView).execute("");

	}
	
	
	class MyAsyncTask extends AsyncTask<String, Void, Boolean> {
		CompoundButton buttonView;
		public MyAsyncTask(CompoundButton buttonView) {
			this.buttonView = buttonView;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;
			final boolean enabled = Api.isEnabled(context);
			if (enabled) {
				if (Api.hasRootAccess(context, true)) {
					if(Api.applyIptablesRules(context, false)) {
						result = true;
					}
				}
			} else {
				Api.saveRules(context);
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
				Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
			}else {
				buttonView.setChecked(false);
			}
		}
		
		
		
	}
	

}
