package com.dong.mobilesafe.db.dao;

import com.dong.mobilesafe.constant.FilePath;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 病毒数据库查询业务类
 *
 */
public class AntivirsuDao {
	
	/**
	 * 查询一个md5是否在病毒数据库里面存在
	 * @param md5 
	 * @return
	 */
	public static boolean isVirus(Context context,String md5){
		
		boolean result = false;
		//打开病毒数据库文件
		SQLiteDatabase db = SQLiteDatabase.openDatabase(FilePath.getDiskFileDir(context, FilePath.FILE_NAME_ANTIVIRUS).getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from datable where md5=?", new String[]{md5});
		if(cursor.moveToNext()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
}
