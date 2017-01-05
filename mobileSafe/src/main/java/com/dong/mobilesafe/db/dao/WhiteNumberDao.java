package com.dong.mobilesafe.db.dao;

import android.content.Context;

import com.dong.mobilesafe.domain.BlackNumber;
import com.dong.mobilesafe.domain.WhiteNumber;

public class WhiteNumberDao extends BaseDao {

	public WhiteNumberDao(Context context) {
		super(context);
	}

	/**
	 * 删除黑名单号码
	 * @param number 要删除的黑名单号码
	 */
	public void deleteByNumber(String number){
		final String where = "number = "+"'"+number+"'";
		db.deleteByWhere(WhiteNumber.class, where);
	}

}
