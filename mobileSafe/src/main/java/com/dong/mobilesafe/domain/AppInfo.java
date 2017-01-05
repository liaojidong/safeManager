package com.dong.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息的业务bean
 */
public class AppInfo {
	protected int uid;
	protected Drawable icon;
	protected String name;
	protected String packname;
	protected boolean inRom;
	protected boolean userApp;
	protected long mobileTraffic;
	protected long wifiTraffic;
	protected long todayMobileTraffic;
	protected long todayWifiTraffic;
	protected int tag; //0真实数据、1用户标签、2系统标签
	protected boolean isLock;

	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public boolean isInRom() {
		return inRom;
	}
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", packname=" + packname + ", inRom="
				+ inRom + ", userApp=" + userApp + "]";
	}
	

	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public boolean isLock() {
		return isLock;
	}
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	public long getMobileTraffic() {
		return mobileTraffic;
	}
	public void setMobileTraffic(long mobileTraffic) {
		this.mobileTraffic = mobileTraffic;
	}
	public long getWifiTraffic() {
		return wifiTraffic;
	}
	public void setWifiTraffic(long wifiTraffic) {
		this.wifiTraffic = wifiTraffic;
	}
	public long getTodayMobileTraffic() {
		return todayMobileTraffic;
	}
	public void setTodayMobileTraffic(long todayMobileTraffic) {
		this.todayMobileTraffic = todayMobileTraffic;
	}
	public long getTodayWifiTraffic() {
		return todayWifiTraffic;
	}
	public void setTodayWifiTraffic(long todayWifiTraffic) {
		this.todayWifiTraffic = todayWifiTraffic;
	}

}

	
	
