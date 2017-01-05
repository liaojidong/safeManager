package com.dong.mobilesafe.db.dao;

import java.util.List;

import android.content.Context;

import com.dong.mobilesafe.domain.TrafficInfo;

public class TrafficInfoDao extends BaseDao {

	public TrafficInfoDao(Context context) {
		super(context);
	}
	
	
	/**
	 * 获取某一日流量使用情况。
	 * @param date
	 * @return
	 */
	public TrafficInfo findByDate(String date) {
		final String where = "statisticsDate = " + "'" + date + "'";
		List<TrafficInfo> trafficInfos = db.findAllByWhere(TrafficInfo.class, where);
		if(trafficInfos != null && trafficInfos.size() > 0) {
			return trafficInfos.get(0);
		}
		return null;
	}
	
	
	/**
	 * 获取一段时间内流量使用总和
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public long getMobileTrafficCountByPeriod(String startDate,String endDate) {
		long trafficCount = 0;
		final String where = "statisticsDate between " + "'"+startDate+"'" +" and " + "'"+endDate+"'";
		List<TrafficInfo> trafficInfos = db.findAllByWhere(TrafficInfo.class, where);
		for(int i=0;trafficInfos!=null && i<trafficInfos.size();i++) {
			TrafficInfo info = trafficInfos.get(i);
			trafficCount += (info.getMobileRx() + info.getMobileTx());
		}
		return trafficCount;
	}

}
