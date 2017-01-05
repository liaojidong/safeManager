package com.dong.mobilesafe.domain;

import java.io.Serializable;
import java.util.Date;


public class CallLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String number;
	private String CachedName;
	private int type;
	private Date date;
	private boolean isChecked;
	private String location;


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public CallLog() {
		
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCachedName() {
		return CachedName;
	}
	public void setCachedName(String cachedName) {
		CachedName = cachedName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}


}
