package com.dong.mobilesafe.db.dao;

import java.util.List;

import android.content.Context;

import com.dong.mobilesafe.domain.AppTrafficInfo;

public class AppTrafficInfoDao extends BaseDao {

	public AppTrafficInfoDao(Context context) {
		super(context);
	}
	
	
	public AppTrafficInfo findByPackageAndDate(String packageName,String statisticsDate) {
		final String where = "packageName = " + "'" + packageName + "'" +" and " +" statisticsDate ="+"'"+statisticsDate+"'";
		List<AppTrafficInfo> appTrafficInfos = db.findAllByWhere(AppTrafficInfo.class, where);
		if(appTrafficInfos != null && appTrafficInfos.size() >0) {
			return appTrafficInfos.get(0);
		}
		return null;
	}
	
	
	public List<AppTrafficInfo> findByPackage(String packageName) {
		final String where = "packageName = " + "'" + packageName + "'";
		List<AppTrafficInfo> appTrafficInfos = db.findAllByWhere(AppTrafficInfo.class, where);
		if(appTrafficInfos != null && appTrafficInfos.size() >0) {
			return appTrafficInfos;
		}
		return null;
	}
	
	
	
	
	public AppTrafficInfo findByPackage(String packageName,String date) {
		final String where = "packageName = " + "'" + packageName + "'"+"statisticsDate = "+"'"+date+"'";
		List<AppTrafficInfo> appTrafficInfos = db.findAllByWhere(AppTrafficInfo.class, where);
		if(appTrafficInfos != null && appTrafficInfos.size() >0) {
			return appTrafficInfos.get(0);
		}
		return null;
	}

}
