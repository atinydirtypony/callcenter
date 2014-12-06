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
	public void addRequest(@RequestParam("seat") int seatNum,
			@RequestParam("store") int storeNum,
			@RequestParam("C35") boolean c35) {
		boolean preRequested = false;
		List<ReliefRequest> currentList = this.getTheList();
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
				if (request.getSeatNum() == reqCheck.getSeatNum()) {
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
	public List<Store> storesBySeat(@RequestParam("seat") int seat_num) {
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
	public ReliefRequest getRequest() {
		List<ReliefRequest> currentList = this.getTheList();
		ReliefRequest request = currentList.get(0);
		request.setTimeActioned(new Date());
		requestRepository.save(request);
		return request;
	}

}
