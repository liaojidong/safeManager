package com.dong.mobilesafe.fragment;

import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.dong.mobilesafe.OnChangeTitleListener;
import com.dong.mobilesafe.R;
import com.dong.mobilesafe.adapter.AppFireWallAdapter;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.utils.Api;
import com.dong.mobilesafe.utils.Api.DroidApp;


public class AppFireWallFragment extends BaseFragment{
	private ListView mListView;
	private View topView;


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Api.assertBinaries(context, true);
		Api.setEnabled(context, true);
	}
	
	
	@Override
	protected  void findViewById(View rootView) {
		mListView = (ListView) rootView.findViewById(R.id.lv_listview);
		topView = rootView.findViewById(R.id.rl_top_tip);
	}
	
	
	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		Api.applications = null;
		showOrLoadApplications();
	}

	
	/**
	 * If the applications are cached, just show them, otherwise load and show
	 */
	private void showOrLoadApplications() {
    	if (Api.applications == null) {
        	final Handler handler = new Handler() {
        		public void handleMessage(Message msg) {
        			showApplications();
        			hideLoading();
        			topView.setVisibility(View.VISIBLE);
        		}
        	};
        	showLoading();
        	new Thread() {
        		public void run() {
        			Api.getApps(context);
        			handler.sendEmptyMessage(0);
        		}
        	}.start();
    	} else {
    		// the applications are cached, just show the list
        	showApplications();
    	}
	}
	
	
	
    /**
     * Show the list of applications
     */
    private void showApplications() {
        final DroidApp[] apps = Api.getApps(context);
        // Sort applications - selected first, then alphabetically
        Arrays.sort(apps, new Comparator<DroidApp>() {
			@Override
			public int compare(DroidApp o1, DroidApp o2) {
				if ((o1.selected_wifi|o1.selected_3g) == (o2.selected_wifi|o2.selected_3g)) {
					return o1.names[0].compareTo(o2.names[0]);
				}
				if (o1.selected_wifi || o1.selected_3g) return -1;
				return 1;
			}
        });
      
		final AppFireWallAdapter adapter = new AppFireWallAdapter(context);
		adapter.replaceList(Arrays.asList(apps));
        mListView.setAdapter(adapter);
    }

	@Override
	protected int onGetContentView() {
		
		return R.layout.fragment_fire_wall;
	}
    



	

}
