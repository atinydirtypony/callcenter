package com.fillmore.callcenterlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fillmore.callcenterlist.domain.AdvisoryLevel;
import com.fillmore.callcenterlist.repository.RequestRepository;

@Configuration
@EnableScheduling 
public class Scheduling {
	@Autowired
	private AdvisoryLevel advisoryLevel;
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Scheduled(cron="0 */15 * * * *")
	public void listLengthCheck(){
		advisoryLevel.cutOffCheck(requestRepository.findBytimeActioned(null));
	}
	
	@Scheduled(cron ="0 0 9 * * *")
	public void adviseReset(){
		advisoryLevel.reset();
	}
}
