package com.dong.mobilesafe.domain;

import java.util.Date;

public class RubbishSms {
	private int id;
	private String msgBaby;
	private Date receiveTime;
	private String number;
	private String region;
	private boolean isRead; //是否已经查阅
	
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMsgBaby() {
		return msgBaby;
	}
	public void setMsgBaby(String msgBaby) {
		this.msgBaby = msgBaby;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
}
