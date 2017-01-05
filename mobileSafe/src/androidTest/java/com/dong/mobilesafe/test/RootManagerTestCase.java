package com.dong.mobilesafe.test;

import com.dong.mobilesafe.utils.RootManager;

import android.test.AndroidTestCase;
import android.widget.Toast;

public class RootManagerTestCase extends AndroidTestCase {
	
	public void upgradeRootPermissionTest() {
		if(RootManager.upgradeRootPermission(getContext().getPackageCodePath())) {
			Toast.makeText(getContext(), "获取Root成功", Toast.LENGTH_LONG).show();
		}else {
			Toast.makeText(getContext(), "获取Root失败", Toast.LENGTH_LONG).show();
		}
	}
}
