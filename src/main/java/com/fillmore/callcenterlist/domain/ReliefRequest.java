package com.fillmore.callcenterlist.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ReliefRequest {
	
	
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToMany
	private List<Stamp> stamps = new ArrayList<Stamp>();
	
	
	private boolean helpRequest = false;
	private boolean beingHelped = false;
	private String type;
	
	@OneToOne
	private Store store;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public List<Stamp> getStamps() {
		return stamps;
	}
	public void setStamps(List<Stamp> stamps) {
		this.stamps = stamps;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	public boolean isHelpRequest() {
		return helpRequest;
	}
	
	public void setHelpRequest(boolean helpRequest) {
		this.helpRequest = helpRequest;
	}
	
	public boolean isBeingHelped() {
		return beingHelped;
	}
	public void setBeingHelped(boolean beingHelped) {
		this.beingHelped = beingHelped;
	}
	
	public void addStamp(Stamp stamp){
		this.stamps.add(stamp);
	}
	
	
	public ReliefRequest(Stamp stamp){
		this.stamps.add(stamp);
	}
	
	public ReliefRequest(){
		
	}
	
}
