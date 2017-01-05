package com.dong.mobilesafe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtil {

	/**
	 * 把日期的时分秒去
	 * @param date
	 * @return
	 */
	public static Date transformYYYYMMDD(Date date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
			return format.parse(format.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String toYYYYMMDD(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		return format.format(date);
	}
	
	
	public static String toMMDD(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd",Locale.CHINA);
		return format.format(date);
	}
	
	

	
	
	/**
	 * 判断当前时间相对于输入时间是否为次日
	 * @param inDate 输入的时间
	 * @return 
	 */
	public static boolean isNextDay(Date inDate) {
		inDate = DateUtil.transformYYYYMMDD(inDate);
		Date currentTime = DateUtil.transformYYYYMMDD(new Date());
		if(currentTime.after(inDate)) {
			return true;
		}
		return false;
	}
	
	

}
