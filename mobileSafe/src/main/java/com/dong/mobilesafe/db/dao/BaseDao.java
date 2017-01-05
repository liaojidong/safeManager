package com.dong.mobilesafe.db.dao;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.database.sqlite.SQLiteException;

public class BaseDao {
	protected FinalDb db;
	
	public BaseDao (Context context) {
		db = FinalDb.create(context);
	}
	
	
	/**
	 * 保存
	 * @param entity
	 */
	public void save(Object entity) {
		try {
			db.save(entity);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 更新
	 * @param entity
	 */
	public void update(Object entity) {
		try {
			db.update(entity);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * 删除
	 * @param entity
	 */
	public void delete(Object entity) {
		
		try {
			db.delete(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据Id查找
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T> T findById(Object id ,Class<T> clazz) {
		return db.findById(id, clazz);
	}
	
	
	/**
	 * 查找�?��条目
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findAll(Class<T> clazz) {
		return db.findAll(clazz);
	}

}
