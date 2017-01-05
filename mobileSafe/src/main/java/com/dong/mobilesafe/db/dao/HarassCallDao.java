package com.dong.mobilesafe.db.dao;

import android.content.Context;

import com.dong.mobilesafe.domain.HarassCall;


public class HarassCallDao extends BaseDao {

	public HarassCallDao(Context context) {
		super(context);
	}
	
	
	/**
	 * 获取还没查阅的垃圾短信总条数
	 * @return
	 */
	public int getUnReadCount() {
		final String where = " isRead = 0 ";
		return db.findAllByWhere(HarassCall.class, where).size();
	}

}
