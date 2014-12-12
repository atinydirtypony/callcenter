package com.fillmore.callcenterlist.controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fillmore.callcenterlist.domain.AdvisoryLevel;
import com.fillmore.callcenterlist.domain.ReliefRequest;
import com.fillmore.callcenterlist.domain.Store;
import com.fillmore.callcenterlist.repository.RequestRepository;
import com.fillmore.callcenterlist.repository.StoreRepository;

@RestController
public class StoreMainController {

	private static final Log log = LogFactory.getLog(StoreMainController.class);

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private AdvisoryLevel advisoryLevel;

	@RequestMapping("/save")
	public void saveStore(@RequestBody Store store) {
		storeRepository.save(store);

	}

	@RequestMapping("/addRequest")
	public void addRequest(@RequestParam("seat") String seatNum,
			@RequestParam("store") int storeNum,
			@RequestParam("C35") boolean c35) {
		boolean preRequested = false;
		List<ReliefRequest> currentList = requestRepository.findBytimeFufilled(null);
		ReliefRequest request = new ReliefRequest();
		request.setSeatNum(seatNum);
		request.setTimeRequested(new Date());
		
		//log.info("C35:"+c35);
		
		if (c35) {
			request.setBathroomBreak(true);
		} else {
			request.setStore(storeRepository.findByIdNumber(storeNum));
		}

		for (ReliefRequest reqCheck : currentList) {
			log.info(reqCheck);
			if (c35) {
				if (request.getSeatNum() == reqCheck.getSeatNum() && reqCheck.isBathroomBreak()) {
					preRequested = true;
				}
			} else {
				if (request.getStore() == reqCheck.getStore()) {
					preRequested = true;
				}
			}
		}
		
		//log.info("Already on:"+preRequested);

		if (preRequested==false) {
			requestRepository.save(request);
		}

	}

	@RequestMapping("/storeById")
	public Store storeById(@RequestParam("id") int id) {
		return storeRepository.findByIdNumber(id);
	}

	@RequestMapping("/seatStores")
	public List<Store> storesBySeat(@RequestParam("seat") String seat_num) {
		if (advisoryLevel.adviseLevel() == "A") {
			return storeRepository.findByaGroup(seat_num);
		} else if (advisoryLevel.adviseLevel() == "B") {
			return storeRepository.findBybGroup(seat_num);
		} else if (advisoryLevel.adviseLevel() == "C") {
			return storeRepository.findBycGroup(seat_num);
		} else if (advisoryLevel.adviseLevel() == "Sat") {
			return storeRepository.findBysatGroup(seat_num);
		} else {
			return null;
		}
	}

	@RequestMapping("/getList")
	public List<ReliefRequest> getTheList() {
		return requestRepository.findBytimeActioned(null);
	}
	
	@RequestMapping("/getRequest")
	public ReliefRequest getRequest(@RequestParam("seat") String seat_num) {
		List<ReliefRequest> requests =requestRepository.getOldestActiveBathroomBreak();
		if(requests.isEmpty()){
			requests = requestRepository.getOldestActiveStoreRequest();
		}
		ReliefRequest request = requests.get(0);
		log.info(request);
		request.setActionSeat(seat_num);
		request.setTimeActioned(new Date());
		requestRepository.save(request);
		return request;
	}
	

	@RequestMapping("/getActiveRequests")
	public List<ReliefRequest> getActiveRequest(@RequestParam("seat") String seat_num) {
		return requestRepository.findBytimeFufilledAndActionSeat(null, seat_num);
	}
	
	@RequestMapping("/finishRequest")
	public void finishRequest(@RequestParam("id") int id_num, @RequestParam("seat") String seat_num){
		ReliefRequest request = requestRepository.findById(id_num);
		request.setFufillmentSeat(seat_num);
		request.setTimeFufilled(new Date());
		requestRepository.save(request);
	}
	
	
	@RequestMapping("/putBackRequest")
	public void otherRequest(@RequestParam("requestSeat") String reqSeat, @RequestParam("id") int idNum){
		ReliefRequest request = new ReliefRequest();
		ReliefRequest oldRequest = requestRepository.findById(idNum);
		request.setSeatNum(reqSeat);
		request.setBathroomBreak(oldRequest.isBathroomBreak());
		request.setStore(oldRequest.getStore());
		request.setTimeRequested(oldRequest.getTimeRequested());
		requestRepository.save(request);
		this.finishRequest(idNum, "N/F");
	}
	
	@RequestMapping("/calledRequest")
	public void calledRequest(@RequestParam("store") int storeNum, @RequestParam("seat") String seat){
		ReliefRequest request = new ReliefRequest();
		request.setActionSeat(seat);
		request.setSeatNum("CALL");
		Store store = storeRepository.findByIdNumber(storeNum);
		request.setStore(store);
		request.setTimeActioned(new Date());
		request.setTimeRequested(new Date());
		List<ReliefRequest> otherRequests = requestRepository.findByStore(store);
		if(otherRequests.size() != 0){
			for(ReliefRequest otherRequest : otherRequests){
				otherRequest.setActionSeat(seat);
				otherRequest.setTimeActioned(new Date());
				this.finishRequest(otherRequest.getId(), "CALL");
			}
		}
		requestRepository.save(request);
	}
	
	
	@RequestMapping("/getJavaDate")
	public Date sendJavaDate(){
		return new Date();
	}

}
