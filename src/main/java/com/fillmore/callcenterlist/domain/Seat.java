package com.fillmore.callcenterlist.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Seat {

	@Id
	private String label;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}	
	
}
