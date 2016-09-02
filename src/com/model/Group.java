package com.model;

public class Group {
	private int id;
	private String groupName;
	private int isVisible;         //0����ʾ��1������ʾ
	private String groupDesc;
	public Group() {
		super();
	}
	public Group(int id, String groupName, int isVisible, String groupDesc) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.isVisible = isVisible;
		this.groupDesc = groupDesc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getIsVisible() {
		return isVisible;
	}
	public void setIsVisible(int isVisible) {
		this.isVisible = isVisible;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public String toString(){
		return this.groupName;
	}
}
