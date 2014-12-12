package com.fillmore.callcenterlist.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;

@Component 
public class AdvisoryLevel {
	private String level = "C";
	private int cutOff = 40;
	private int timeTillComplete = 0;
	private List<Long> lapTimes = new ArrayList<Long>();
	
	public void addLapTime(Long time){
		lapTimes.add(time);
		if(lapTimes.size()>10){
			lapTimes.remove(0);
		}
		Long sum = (long) 0;
		for(Long i: lapTimes ){
			sum= sum+i;
		}
		timeTillComplete = Math.round((sum/ (lapTimes.size()*60 * 1000)) % 60);
	}

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
	
	public String getLevel(){
		return this.level;
	}
	
	public void reset(){
		if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY){
			this.level = "Sat";
		}else{
			this.level = "C";
		}
		cutOff = 40;
		timeTillComplete = 0;
		lapTimes.clear();
		
	}

	public int getCutOff() {
		return cutOff;
	}

	public void setCutOff(int cutOff) {
		this.cutOff = cutOff;
	}
	
	public void cutOffCheck(List<ReliefRequest> theList){
		if(theList.size() >= 15 ){
			cutOff += 5;
			if(cutOff >65 ){
				cutOff = 65;
			}
		}else if(theList.size() <=5){
			cutOff -= 5;
			if(cutOff <5 ){
				cutOff = 5;
			}
		}
	}

	public int getTimeTillComplete() {
		return timeTillComplete;
	}

	public void setTimeTillComplete(int timeTillComplete) {
		this.timeTillComplete = timeTillComplete;
	}

}
