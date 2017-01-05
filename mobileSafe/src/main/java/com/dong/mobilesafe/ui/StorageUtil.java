package com.dong.mobilesafe.ui;

import android.os.StatFs;

public class StorageUtil {

	
	/**
	 * 获取某个目录的可用空间
	 * 
	 * @param path
	 * @return
	 */
	public static long getAvailSpace(String path) {
		StatFs statf = new StatFs(path);
		statf.getBlockCount();// 获取分区的个数
		long size = statf.getBlockSize();// 获取分区的大小
		long count = statf.getAvailableBlocks();// 获取可用的区块的个数
		return size * count;
	}
	
}
