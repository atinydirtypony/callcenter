package com.fillmore.callcenterlist.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fillmore.callcenterlist.domain.Store;
import com.fillmore.callcenterlist.repository.StoreRepository;


@RestController
public class StoreMainController {
	
	private static final Log log = LogFactory.getLog(StoreMainController.class);
	
	@Autowired
	private StoreRepository storeRepository;
	
	@RequestMapping("/save")
	public void saveStore() {
		Store currentStore = new Store();
		currentStore.setIdNumber(23423);
		currentStore.setaGroup(1);
		currentStore.setbGroup(1);
		currentStore.setcGroup(1);
		currentStore.setSatGroup(1);
		currentStore.setOnList(false);
		currentStore.setPhoneNumber("111-111-LOVE");
		currentStore.setTop15(true);
		storeRepository.save(currentStore);
		
	}
	
	@RequestMapping("/storeInfo")
	public Store getStore(@RequestParam("number") int id){
		Store currentStore = new Store();
		currentStore.setIdNumber(id);
		currentStore.setaGroup(1);
		currentStore.setbGroup(1);
		currentStore.setcGroup(1);
		currentStore.setSatGroup(1);
		currentStore.setOnList(false);
		currentStore.setPhoneNumber("111-111-LOVE");
		currentStore.setTop15(true);
		log.info("Store info request: " + currentStore.getIdNumber());
		return currentStore;
	}
	
	@RequestMapping("/storeByPhone")
	public Store storeByPhone(@RequestParam("phone") String phone){
		return storeRepository.findByPhoneNumber(phone);
	}
	
}
