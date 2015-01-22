package com.fillmore.callcenterlist.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.security.core.context.SecurityContextHolder;

@Entity
public class Stamp {
	private String seat;
	private String id;
	private Date time;
	private String type;
	
	@Id
	@GeneratedValue
	private long stampId;
	
	
	public long getStampId() {
		return stampId;
	}

	public void setStampId(long stampId) {
		this.stampId = stampId;
	}

	public Stamp(String seat, String type){
		this.seat =seat;
		this.id = SecurityContextHolder.getContext().getAuthentication().getName();
		this.time = new Date();
		this.type = type;
	}
	
	public Stamp(String seat, String id, String type){
		this.seat =seat;
		this.time = new Date();
		this.id=id;
		this.type = type;
	}
	
	public Stamp(String seat, String id, Date time, String type){
		this.seat =seat;
		this.time = time;
		this.id=id;
		this.type = type;
	}
	
	public Stamp(){
		
	}
	
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
