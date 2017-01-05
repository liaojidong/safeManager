package com.dong.mobilesafe.business;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.dong.mobilesafe.db.dao.TrafficInfoDao;
import com.dong.mobilesafe.utils.DateUtil;

public class TrafficInfoBusiness {
	private TrafficInfoDao trafficInfoDao;
	
	
	public TrafficInfoBusiness(Context context) {
		trafficInfoDao = new TrafficInfoDao(context);
	}
	
	
	/**
	 * 获取当前月使用流量总和
	 * @param startDay 计费日
	 * @return 当前流量使用总和，单位 字节（b）
	 */
	public long getCurentMonth(int startDay) {
		
		Calendar temCalendar = Calendar.getInstance(Locale.CHINA);
		Calendar startCalendar = Calendar.getInstance(Locale.CHINA);
		Calendar endcCalendar = Calendar.getInstance(Locale.CHINA);
		
		temCalendar.set(Calendar.DAY_OF_MONTH, startDay);
		if(temCalendar.getTime().after(new Date())) {
			endcCalendar.setTime(temCalendar.getTime());
			temCalendar.add(Calendar.MONTH, -1);
			startCalendar.setTime(temCalendar.getTime());
			
		}else {
			startCalendar.setTime(temCalendar.getTime());
			temCalendar.add(Calendar.MONTH, 1);
			endcCalendar.setTime(temCalendar.getTime());
		}
		long usedTraffic = trafficInfoDao.getMobileTrafficCountByPeriod(
							DateUtil.toYYYYMMDD(startCalendar.getTime()),
							DateUtil.toYYYYMMDD(endcCalendar.getTime()));
		return usedTraffic;
	}

}
