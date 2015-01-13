package com.fillmore.callcenterlist.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Store {
	@Id
	private int idNumber;
	

	private boolean top15;
	private String aGroup;
	private String bGroup;
	private String cGroup;
	private String satGroup;
	private String state;
	
	public String getaGroup() {
		return aGroup;
	}
	public void setaGroup(String aGroup) {
		this.aGroup = aGroup;
	}
	public String getbGroup() {
		return bGroup;
	}
	public void setbGroup(String bGroup) {
		this.bGroup = bGroup;
	}
	public String getcGroup() {
		return cGroup;
	}
	public void setcGroup(String cGroup) {
		this.cGroup = cGroup;
	}
	public String getSatGroup() {
		return satGroup;
	}
	public void setSatGroup(String satGroup) {
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
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
