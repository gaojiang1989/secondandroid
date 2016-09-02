package com.model;

public class MyWord {
	private int id;
	private String jpword;
	private String jpch;
	private String chword;
	private String speech;
	private int type;
	private int dateid;
	private String desc;
	public MyWord() {
		super();
	}
	public MyWord(int id, String jpword, String jpch, String chword,
			String speech, int type, int dateid, String desc) {
		super();
		this.id = id;
		this.jpword = jpword;
		this.jpch = jpch;
		this.chword = chword;
		this.speech = speech;
		this.type = type;
		this.dateid = dateid;
		this.desc = desc;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJpword() {
		return jpword;
	}
	public void setJpword(String jpword) {
		this.jpword = jpword;
	}
	public String getJpch() {
		return jpch;
	}
	public void setJpch(String jpch) {
		this.jpch = jpch;
	}
	public String getChword() {
		return chword;
	}
	public void setChword(String chword) {
		this.chword = chword;
	}
	public String getSpeech() {
		return speech;
	}
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDateid() {
		return dateid;
	}
	public void setDateid(int dateid) {
		this.dateid = dateid;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
