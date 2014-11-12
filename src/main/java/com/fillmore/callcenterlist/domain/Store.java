package com.fillmore.callcenterlist.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Store {
	@Id
	private int idNumber;
	

	private boolean onList;
	private boolean top15;
	private int aGroup;
	private int bGroup;
	private int cGroup;
	private int satGroup;
	private String state;
	
	public int getaGroup() {
		return aGroup;
	}
	public void setaGroup(int aGroup) {
		this.aGroup = aGroup;
	}
	public int getbGroup() {
		return bGroup;
	}
	public void setbGroup(int bGroup) {
		this.bGroup = bGroup;
	}
	public int getcGroup() {
		return cGroup;
	}
	public void setcGroup(int cGroup) {
		this.cGroup = cGroup;
	}
	public int getSatGroup() {
		return satGroup;
	}
	public void setSatGroup(int satGroup) {
		this.satGroup = satGroup;
	}
	public int getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
	public boolean isTop15() {
		return top15;
	}
	public void setTop15(boolean top15) {
		this.top15 = top15;
	}
	
	public boolean isOnList() {
		return onList;
	}
	public void setOnList(boolean onList) {
		this.onList = onList;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
