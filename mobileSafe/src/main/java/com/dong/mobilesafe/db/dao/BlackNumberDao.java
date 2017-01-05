package com.dong.mobilesafe.db.dao;

import java.util.List;

import android.content.Context;

import com.dong.mobilesafe.domain.BlackNumber;

/**
 * 黑名单数据库的增删改查业务类
 * @author Administrator
 *
 */
public class BlackNumberDao extends BaseDao{

	public BlackNumberDao(Context context) {
		super(context);
	}

	
	/**
	 * 查询黑名单号码是是否存在
	 * @param number
	 * @return
	 */
	public boolean findExistByNumber(String number){
		final String where = "number = "+"'"+number+"'";
		List<BlackNumber> list = db.findAllByWhere(BlackNumber.class, where);
		if(list !=null && list.size()>0){
			return true;
		}
		return false;
	}
	/**
	 * 查询黑名单号码的拦截模式
	 * @param number
	 * @return 返回号码的拦截模式，不是黑名单号码返回-1
	 */
	public int findModeByNumber(String number){
		final String where = "number = "+"'"+number+"'";
		List<BlackNumber> list = db.findAllByWhere(BlackNumber.class, where);
		if(list != null && list.size() > 0) {
			return list.get(0).getMode();
		}
		return -1;
	}
	

	/**
	 * 删除黑名单号码
	 * @param number 要删除的黑名单号码
	 */
	public void deleteByNumber(String number){
		final String where = "number = "+"'"+number+"'";
		db.deleteByWhere(BlackNumber.class, where);
	}
}
