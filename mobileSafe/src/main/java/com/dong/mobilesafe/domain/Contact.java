package com.dong.mobilesafe.domain;

import android.graphics.Bitmap;

public class Contact {
	private String phoneNumber;
	private String contactName;
	private long contactid;
	private Bitmap contactPhoto;
	private boolean isChecked;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public long getContactid() {
		return contactid;
	}
	public void setContactid(long contactid) {
		this.contactid = contactid;
	}
	public Bitmap getContactPhoto() {
		return contactPhoto;
	}
	public void setContactPhoto(Bitmap contactPhoto) {
		this.contactPhoto = contactPhoto;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}


}
