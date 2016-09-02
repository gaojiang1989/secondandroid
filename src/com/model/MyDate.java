package com.model;

public class MyDate {
	private int id;
	private String date;
	private String desc;
	public MyDate() {
		super();
	}
	public MyDate(int id, String date, String desc) {
		super();
		this.id = id;
		this.date = date;
		this.desc = desc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
