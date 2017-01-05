package com.dong.mobilesafe.share;

public class ShareCofig {
	private String appid; 
	private String appSecret;    
	private String redirectUrl; 
	private int shareBy;   //1:客户端分享 2：web分享 3：根据系统决定
	public int getShareBy() {
		return shareBy;
	}
	public void setShareBy(int shareBy) {
		this.shareBy = shareBy;
	}
	private boolean Enable;
	private String platform;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public boolean isEnable() {
		return Enable;
	}
	public void setEnable(boolean enable) {
		Enable = enable;
	}

}
