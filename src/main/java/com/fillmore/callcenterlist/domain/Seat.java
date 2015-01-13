package com.fillmore.callcenterlist.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Seat {

	@Id
	private String label;
	
	private String taskType;
	private String becomesGroup;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getBecomesGroup() {
		return becomesGroup;
	}
	public void setBecomesGroup(String becomesGroup) {
		this.becomesGroup = becomesGroup;
	}
	
	
}
