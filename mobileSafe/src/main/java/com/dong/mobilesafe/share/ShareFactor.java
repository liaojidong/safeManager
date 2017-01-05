package com.dong.mobilesafe.share;



public class ShareFactor {
	

	public static Share createShare(ShareManager.SharePlatform type) {
		Share share = null;
		switch (type) {
		case sinaweibo:
			share = new SinaWiboShare();
			break;

		case wechat:
			share = new WechatShare();
			break;
		case qzone:
			share = new QzoneShare();
			break;
		case qq:
			share = new QQShare();
			break;
		default:
			break;
		}
		return share;
	}
	
}
