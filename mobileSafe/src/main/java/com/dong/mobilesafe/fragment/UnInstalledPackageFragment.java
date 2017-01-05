package com.dong.mobilesafe.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.dong.mobilesafe.R;
import com.dong.mobilesafe.base.BaseFragment;
import com.dong.mobilesafe.bean.AppInfo;
import com.dong.mobilesafe.engine.SearchFile;
import com.dong.mobilesafe.utils.AppManager;

public class UnInstalledPackageFragment extends BaseFragment {
	private List<AppInfo> unInstallList = new ArrayList<AppInfo>();
	
	@Override
	protected void onInitData(Bundle savedInstanceState) {
		super.onInitData(savedInstanceState);
		fillData();
	}

	private void fillData() {
		showLoading();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SearchFile searchFile = new SearchFile(new File(getSDPath()),".+\\.apk$");
				List<File> files = searchFile.allFiles();
				for(File f: files) {
					AppManager manager = AppManager.getInstance(context);
					AppInfo appInfo = manager.getAppInfoByAPKFile(f);
					if(appInfo !=null && !AppManager.isAppInstalled(context, appInfo.getPakageName())) {
						unInstallList.add(appInfo);
					}
				}
				hideLoading();
			}
		}).start();
		
	}

	@Override
	protected void findViewById(View rootView) {
		

	}

	@Override
	protected int onGetContentView() {
		
		return R.layout.fragment_uninstalled_package;
	}
	
	
	public String getSDPath(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
				
		}		
		return null;
	}


}
