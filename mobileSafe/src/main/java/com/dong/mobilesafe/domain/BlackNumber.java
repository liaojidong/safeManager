package com.dong.mobilesafe.domain;

/**
 * 黑名单号码的业务bean
 *
 */
public class BlackNumber {
	private int id;
	private String number;
	private String name;
	private int mode; //1.电话拦截 2.短信拦截 3.全部拦截
	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "blacknumber [number=" + number + ", mode=" + mode + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
