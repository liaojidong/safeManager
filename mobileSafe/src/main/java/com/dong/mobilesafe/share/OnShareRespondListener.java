package com.dong.mobilesafe.share;

/**
 * 分享监听者
 * @author Jesse
 *
 */
public interface OnShareRespondListener {
	
	/**
	 * 返回分享结果
	 * @param respondData 返回的分享结果的数据
	 */
	public void onRespond(ShareRespond respondData);

}
