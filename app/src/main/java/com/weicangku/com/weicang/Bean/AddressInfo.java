package com.weicangku.com.weicang.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class AddressInfo extends BmobObject implements Serializable{
	
	private String id;
	private String name;
	private String phone;
	private String provinces;
	private String street;
	private boolean status = false;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
