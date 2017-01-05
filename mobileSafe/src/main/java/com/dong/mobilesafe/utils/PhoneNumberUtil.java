package com.dong.mobilesafe.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class PhoneNumberUtil {
	
	
	/**
	 * 根据电话号码查找用户名称
	 * @param context
	 * @param number
	 * @return
	 */
	public static String getNameByNumber(Context context,String number) {
		String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='"+number+"'";
	     Cursor cursor =
	             context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                 new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
	                 selection,null,null);
	     while(cursor.moveToNext()) {
	    	return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	     }
	     cursor.close();
		return null;
	}

}
