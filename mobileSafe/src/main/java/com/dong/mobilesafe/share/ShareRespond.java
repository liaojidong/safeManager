package com.dong.mobilesafe.share;


import java.io.Serializable;

public class ShareRespond implements Serializable{
	/**
	 * 分享成功
	 */
	public final static int CODE_SUCCESS = 1;
	/**
	 * 取消分享
	 */
	public final static int CODE_CANCEL = 2;
	/**
	 * 分享失敗
	 */
	public final static int CODE_FAIL = 3;
	/**
	 * 分享被拒绝
	 */
	public final static int CODE_DENY = 4;
	/**
	 * 未知错误
	 */
	public final static int CODE_UNKNOW = 5;
	
	public ShareManager.SharePlatform sharePlatform;
	public int code;
	public String msg;
}
