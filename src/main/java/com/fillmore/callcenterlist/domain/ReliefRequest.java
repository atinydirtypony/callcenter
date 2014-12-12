package com.fillmore.callcenterlist.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ReliefRequest {
	//bathroomBreak is a poop/pee time request
	private boolean bathroomBreak =false;
	
	@OneToOne
	private Store store;
	private String seatNum;
	private Date timeRequested;
	private String actionSeat;
	private Date timeActioned;
	private String fufillmentSeat;
	public String getActionSeat() {
		return actionSeat;
	}
	public void setActionSeat(String actionSeat) {
		this.actionSeat = actionSeat;
	}
	public String getFufillmentSeat() {
		return fufillmentSeat;
	}
	public void setFufillmentSeat(String fufillmentSeat) {
		this.fufillmentSeat = fufillmentSeat;
	}
	private Date timeFufilled;
	@Id
	@GeneratedValue
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isBathroomBreak() {
		return bathroomBreak;
	}
	public void setBathroomBreak(boolean bathroomBreak) {
		this.bathroomBreak = bathroomBreak;
	}
	
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public String getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	public Date getTimeRequested() {
		return timeRequested;
	}
	public void setTimeRequested(Date timeRequested) {
		this.timeRequested = timeRequested;
	}
	public Date getTimeActioned() {
		return timeActioned;
	}
	public void setTimeActioned(Date timeActioned) {
		this.timeActioned = timeActioned;
	}
	public Date getTimeFufilled() {
		return timeFufilled;
	}
	public void setTimeFufilled(Date timeFufilled) {
		this.timeFufilled = timeFufilled;
	}
	
	
}
