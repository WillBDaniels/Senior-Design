package com.service;

public class Employee {
	String phone = null;
	String name = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	String id = null;
	//String location = null;
	boolean selected = false;
	public Employee(String phone,String name,boolean selected){
		super();
		this.phone = phone;
		this.name = name;
		this.selected = selected;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
