package com.dong.mobilesafe.db.dao;

import com.dong.mobilesafe.domain.RubbishSms;

import android.content.Context;

public class RubbishSmsDao extends BaseDao {

	public RubbishSmsDao(Context context) {
		super(context);
	}
	
	
	/**
	 * 获取还没有阅读的垃圾短信总数
	 * @return
	 */
	public int getUnReadCount() {
		final String where = " isRead = 0 ";
		return db.findAllByWhere(RubbishSms.class, where).size();
	}

}
