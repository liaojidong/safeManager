package com.dong.mobilesafe.utils.date;

import android.annotation.SuppressLint;
import android.content.Context;


import com.dong.mobilesafe.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 格式化时间工具类
 *
 * @author chends
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	// 1毫秒
	public static int MILLI_SECOND = 1;
	// 1秒
	public static int SECOND = MILLI_SECOND * 1000;
	// 1分钟
	public static int MINUTE = 60 * SECOND;
	// 1小时
	public static int HOUR = 60 * MINUTE;



	/**
	 * 系统显示时间格式
	 *
	 * @param context
	 * @param millsecond
	 * @return 今天 10:00 昨天 昨天 10:00 昨天之前 4-15 10:00
	 */
	public static String formartCustomTime(Context context,
										   long millsecond) {
		SimpleDateFormat format = null;
		long bakMillSecond = millsecond;
		long cur = System.currentTimeMillis();
		long days = daysOfNum(new Date(bakMillSecond), new Date(cur));
		// 昨天之前
		if (days > 1) {
			format = new SimpleDateFormat("MM-dd HH:mm");
			return format.format(new Date(bakMillSecond));
		}
		// 昨天
		if (days == 1) {
			format = new SimpleDateFormat("HH:mm");
			return context.getString(R.string.TxtYesterday) + " "
					+ format.format(new Date(bakMillSecond));
		}
		format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(bakMillSecond));
	}

	public static int daysOfNum(Date fDate, Date oDate) {

		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(fDate);

		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		aCalendar.setTime(oDate);

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day2 - day1;

	}


}
