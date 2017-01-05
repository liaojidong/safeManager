package com.dong.mobilesafe;

import java.util.LinkedHashMap;

import android.support.v4.app.Fragment;
import android.view.View;

import com.dong.mobilesafe.base.PageViewActivity;
import com.dong.mobilesafe.fragment.InstalledPackageFragment;
import com.dong.mobilesafe.fragment.UnInstalledPackageFragment;

/**
 *包管理Activity 
 *
 */
public class PackageManagerActivity extends PageViewActivity {
	UnInstalledPackageFragment unInstalledPackageFragment;
	InstalledPackageFragment installedPackageFragment;
	
	@Override
	protected void onInitData() {
		super.onInitData();
		unInstalledPackageFragment = new UnInstalledPackageFragment();
		installedPackageFragment = new InstalledPackageFragment();
	}

	@Override
	protected LinkedHashMap<String, Fragment> setFragment() {
		LinkedHashMap<String, Fragment> tabs = new LinkedHashMap<String, Fragment>();
		tabs.put("未安装", unInstalledPackageFragment);
		tabs.put("已安装", installedPackageFragment);
		return tabs;
	}
	


	@Override
	protected void onPrepareView() {
		super.onPrepareView();
		setBarTitle("安装包管理");
	}
}
