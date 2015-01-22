package com.fillmore.callcenterlist.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;


@Entity
public class Bulletin {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private Date posted;
	private Date expires;
	private boolean expired=false;
	
	@Type(type="text")
	private String content;
	private boolean alert=false;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getPosted() {
		return posted;
	}
	public void setPosted(Date posted) {
		this.posted = posted;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isAlert() {
		return alert;
	}
	public void setAlert(boolean alert) {
		this.alert = alert;
	}
	
	
	
}
