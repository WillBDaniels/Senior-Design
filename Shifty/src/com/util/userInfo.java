package com.util;

import android.app.Application;

public class userInfo extends Application {
	private String employeeId;
	private String employeeName;
	private int houseNumber;
	public int getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public void onCreate(){
		super.onCreate();
		setEmployeeId("aa");
		setEmployeeName("aa");
		setHouseNumber(0);
	}
}
