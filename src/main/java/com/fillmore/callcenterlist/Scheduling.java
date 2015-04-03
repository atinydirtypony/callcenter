package com.fillmore.callcenterlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.fillmore.callcenterlist.domain.AdvisoryLevel;
import com.fillmore.callcenterlist.domain.Bulletin;
import com.fillmore.callcenterlist.domain.ReliefRequest;
import com.fillmore.callcenterlist.domain.Stamp;
import com.fillmore.callcenterlist.repository.BulletinRepository;
import com.fillmore.callcenterlist.repository.RequestRepository;
import com.fillmore.callcenterlist.repository.StampRepository;

@Configuration
@EnableScheduling 
@Transactional
public class Scheduling {
	@Autowired
	private AdvisoryLevel advisoryLevel;
	
	@Autowired
	private BulletinRepository bulletinRepository;
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private StampRepository stampRepository;
	
	@Scheduled(cron="0 */15 * * * *")
	public void listLengthCheck(){
		advisoryLevel.cutOffCheck(requestRepository.findUnactionedRequests());
		
		
	} 
	
	@Scheduled(cron ="0 0 9 * * *")
	public void adviseReset(){
		advisoryLevel.reset();
		List<Bulletin> bulletins = bulletinRepository.findByExpired(false);
		for(Bulletin bullet: bulletins){
			bullet.checkExpire();
			bulletinRepository.save(bullet);
		}
		
		List<ReliefRequest> UnactionedRequests =requestRepository.findUnactionedRequests();
		for(ReliefRequest request: UnactionedRequests ){
			Stamp actionStamp =new Stamp("NONE", "NONE", "ACTIONED");
			request.addStamp(actionStamp);
			stampRepository.save(actionStamp);
			requestRepository.save(request);
		}
		
		List<ReliefRequest> UnfufilledRequests =requestRepository.findUnfufilledRequests();
		for(ReliefRequest request: UnfufilledRequests ){
			Stamp fufillStamp =new Stamp("NONE", "NONE", "FUFILLED");
			request.addStamp(fufillStamp);
			stampRepository.save(fufillStamp);
			requestRepository.save(request);
		}
	}
	
}
