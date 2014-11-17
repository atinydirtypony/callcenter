package com.fillmore.callcenterlist.domain;

import java.util.Calendar;

import org.springframework.stereotype.Component;

@Component 
public class AdvisoryLevel {
	private String level = "C";

	public void changeToA() {
		this.level = "A";
	}

	public void changeToB() {
		this.level = "B";
	}
	
	public void changeToC(){
		this.level = "C";
	}
	
	public void changeToSat(){
		this.level = "Sat";
	}
	
	public String adviseLevel(){
		return this.level;
	}
	
	public void reset(){
		if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
			this.level = "Sat";
		}else{
			this.level = "C";
		}
	}

}
