package com.onion.o2o.entity;

import java.util.Date;

public class WechatAuth {
	private Long wwchatAuthId;
	private String openId;
	private Date createTime;
	private PersonInfo personInfo;
	public Long getWwchatAuthId() {
		return wwchatAuthId;
	}
	public void setWwchatAuthId(Long wwchatAuthId) {
		this.wwchatAuthId = wwchatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	
}
