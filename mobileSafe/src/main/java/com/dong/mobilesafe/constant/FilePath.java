package com.dong.mobilesafe.constant;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.dong.mobilesafe.utils.MyLogger;
import com.dong.mobilesafe.utils.Utils;

public class FilePath {
	public static String FILE_NAME_ADDRESS = "address.db";
	public static String FILE_NAME_ANTIVIRUS = "antivirus.db";
	
	
	/**
	 * 获取保存文件的目录
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public static File getDiskFileDir(Context context,String uniqueName) {
		 final String path =
	                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ?
	                				getExternalFileDir(context).getPath() : context.getFilesDir().getPath();
	        MyLogger.jLog().d("diskFileDir = "+path);				
	        return new File(path + File.separator + uniqueName);
	}
	
	
	
	/**
	 * 获取SD文件保存的目录
	 * @param context
	 * @return
	 */
    public static File getExternalFileDir(Context context) {
        if (Utils.hasFroyo()) {
            return context.getExternalFilesDir(null);
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String fileDir = "/Android/data/" + context.getPackageName() + "/file/";
        return new File(Environment.getExternalStorageDirectory().getPath() + fileDir);
    }
	

}
