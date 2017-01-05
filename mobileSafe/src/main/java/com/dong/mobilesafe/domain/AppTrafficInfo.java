package com.dong.mobilesafe.domain;


public class AppTrafficInfo {
	private int id;
	private String packageName;
	private long bootTxCount;
	private long bootRxCount;
	private String statisticsDate;
	private long txCount;
	private long rxCount;
	private long txMobileCount;
	private long rxMobileCount;
	private long txWifiCount;
	private long rxWifiCount;
	
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public long getTxCount() {
		return txCount;
	}
	public void setTxCount(long txCount) {
		this.txCount = txCount;
	}
	public long getRxCount() {
		return rxCount;
	}
	public void setRxCount(long rxCount) {
		this.rxCount = rxCount;
	}

	public long getBootTxCount() {
		return bootTxCount;
	}
	public void setBootTxCount(long bootTxCount) {
		this.bootTxCount = bootTxCount;
	}
	public long getBootRxCount() {
		return bootRxCount;
	}
	public void setBootRxCount(long bootRxCount) {
		this.bootRxCount = bootRxCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatisticsDate() {
		return statisticsDate;
	}
	public void setStatisticsDate(String statisticsDate) {
		this.statisticsDate = statisticsDate;
	}
	public long getTxMobileCount() {
		return txMobileCount;
	}
	public void setTxMobileCount(long txMobileCount) {
		this.txMobileCount = txMobileCount;
	}
	public long getRxMobileCount() {
		return rxMobileCount;
	}
	public void setRxMobileCount(long rxMobileCount) {
		this.rxMobileCount = rxMobileCount;
	}
	public long getTxWifiCount() {
		return txWifiCount;
	}
	public void setTxWifiCount(long txWifiCount) {
		this.txWifiCount = txWifiCount;
	}
	public long getRxWifiCount() {
		return rxWifiCount;
	}
	public void setRxWifiCount(long rxWifiCount) {
		this.rxWifiCount = rxWifiCount;
	}

}
