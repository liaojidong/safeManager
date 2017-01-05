package com.dong.mobilesafe.bean;

import java.io.Serializable;

public class AppInfo implements Serializable{
	
	private String name;
	private String version;
	private String pakageName;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPakageName() {
		return pakageName;
	}
	public void setPakageName(String pakageName) {
		this.pakageName = pakageName;
	}


}
