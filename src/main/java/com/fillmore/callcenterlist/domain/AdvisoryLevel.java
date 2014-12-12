package com.fillmore.callcenterlist.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component 
public class AdvisoryLevel {
	private String level = "C";
	private int cutOff = 40;

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

	public int getCutOff() {
		return cutOff;
	}

	public void setCutOff(int cutOff) {
		this.cutOff = cutOff;
	}
	
	public void cutOffCheck(List<ReliefRequest> theList){
		
	}

}
