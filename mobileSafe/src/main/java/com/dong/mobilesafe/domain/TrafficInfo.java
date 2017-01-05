package com.dong.mobilesafe.domain;




public class TrafficInfo {
	private int id;
	private String statisticsDate;
	private long mobileRx;
	private long mobileTx;
	private long rxCount;
	private long txCount;
	private long bootMobileRx;
	private long bootMobileTx;
	private long bootRxCount;
	private long bootTxCount;
	

	public long getMobileRx() {
		return mobileRx;
	}
	public void setMobileRx(long mobileRx) {
		this.mobileRx = mobileRx;
	}
	public long getMobileTx() {
		return mobileTx;
	}
	public void setMobileTx(long mobileTx) {
		this.mobileTx = mobileTx;
	}
	public long getRxCount() {
		return rxCount;
	}
	public void setRxCount(long rxCount) {
		this.rxCount = rxCount;
	}
	public long getTxCount() {
		return txCount;
	}
	public void setTxCount(long txCount) {
		this.txCount = txCount;
	}
	public long getBootMobileRx() {
		return bootMobileRx;
	}
	public void setBootMobileRx(long bootMobileRx) {
		this.bootMobileRx = bootMobileRx;
	}
	public long getBootMobileTx() {
		return bootMobileTx;
	}
	public void setBootMobileTx(long bootMobileTx) {
		this.bootMobileTx = bootMobileTx;
	}
	public long getBootRxCount() {
		return bootRxCount;
	}
	public void setBootRxCount(long bootRxCount) {
		this.bootRxCount = bootRxCount;
	}
	public long getBootTxCount() {
		return bootTxCount;
	}
	public void setBootTxCount(long bootTxCount) {
		this.bootTxCount = bootTxCount;
	}
	public String getStatisticsDate() {
		return statisticsDate;
	}
	public void setStatisticsDate(String statisticsDate) {
		this.statisticsDate = statisticsDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
