package com.dong.mobilesafe.domain;

/**
 * 白名单号码的业务bean
 *
 */
public class WhiteNumber {
	private int id;
	private String number;
	private String name;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	
}
