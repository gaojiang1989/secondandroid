package com.model;

public class MyType {
	private int id;
	private String typename;
	private String desc;
	public MyType() {
		super();
	}
	public MyType(int id, String typename, String desc) {
		super();
		this.id = id;
		this.typename = typename;
		this.desc = desc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
