package com.fillmore.callcenterlist.controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fillmore.callcenterlist.domain.Account;
import com.fillmore.callcenterlist.domain.AdvisoryLevel;
import com.fillmore.callcenterlist.domain.Bulletin;
import com.fillmore.callcenterlist.domain.ReliefRequest;
import com.fillmore.callcenterlist.domain.Seat;
import com.fillmore.callcenterlist.domain.Stamp;
import com.fillmore.callcenterlist.domain.Store;
import com.fillmore.callcenterlist.repository.AccountRepository;
import com.fillmore.callcenterlist.repository.BulletinRepository;
import com.fillmore.callcenterlist.repository.RequestRepository;
import com.fillmore.callcenterlist.repository.SeatRepository;
import com.fillmore.callcenterlist.repository.StampRepository;
import com.fillmore.callcenterlist.repository.StoreRepository;

@RestController
public class StoreMainController {
	private static final Log log = LogFactory.getLog(StoreMainController.class);

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private BulletinRepository bulletinRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private AdvisoryLevel advisoryLevel;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private StampRepository stampRepository;

	@RequestMapping("/calledRequest")
	public void calledRequest(@RequestParam("store") int storeNum,
			@RequestParam("seat") String seatNum) {
		Stamp actionStamp = new Stamp(seatNum, "ACTIONED");
		ReliefRequest request = new ReliefRequest(actionStamp);
		Store store = storeRepository.findByIdNumber(storeNum);
		request.setStore(store);
		request.setType("CALL");
		stampRepository.save(actionStamp);
		List<ReliefRequest> otherRequests = requestRepository.findNonfufulledByStore(store);
		if (otherRequests.size() != 0) {
			request.setHelpRequest(true);
			for (ReliefRequest otherRequest : otherRequests) {
				otherRequest.addStamp(actionStamp);
				otherRequest.setBeingHelped(true);
				requestRepository.save(otherRequest);
			}
		}
		requestRepository.save(request);
	}

	@RequestMapping("/finishRequest")
	public void finishRequest(@RequestParam("id") int id_num,
			@RequestParam("seat") String seatNum) {
		ReliefRequest request = requestRepository.findById(id_num);
		Stamp stamp1 = new Stamp(seatNum, "FUFILLED");
		request.addStamp(stamp1);
		if (request.getType().equals("DV2")) {
			request.getStore().setLastChecked(new Date());
		}
		if (!request.getStamps().get(0).getType().equals("C35") && !request.getStamps().get(0).getType().equals("CALL")) {
			advisoryLevel.addLapTime(-(request.getStamps().get(0).getTime()
					.getTime()
					- request.getStamps().get(request.getStamps().size() - 1)
							.getTime().getTime()));
		}
		stampRepository.save(stamp1);
		requestRepository.save(request);
		ReliefRequest secondary = null;
		if (request.isHelpRequest() || request.isBeingHelped()) {
			secondary = requestRepository.getHelperRequest(request.getStamps()
					.get(0).getStampId(), request.getId());
		}
		// log.info(request.isHelpRequest());
		// log.info(request.isBeingHelped());
		// log.info(secondary);
		if (secondary != null) {
			// log.info("HI!!!");
			if (!secondary.getStamps().get(secondary.getStamps().size() - 1)
					.getType().equals("ACTIONED")) {
				secondary.addStamp(request.getStamps().get(
						request.getStamps().size() - 2));
			}
			Stamp stamp2 = new Stamp(secondary.getStamps()
					.get(secondary.getStamps().size() - 1).getSeat(),
					"FUFILLED");
			secondary.addStamp(stamp2);
			stampRepository.save(stamp2);
			requestRepository.save(secondary);
		}

	}

	@RequestMapping("/putBackRequest")
	public void putBackRequest(@RequestParam("requestSeat") String seatNum,

	@RequestParam("id") int idNum) {
		ReliefRequest oldRequest = requestRepository.findById(idNum);
		Stamp stamp = new Stamp(seatNum, "PUTBACK");
		oldRequest.addStamp(stamp);
		stampRepository.save(stamp);
		requestRepository.save(oldRequest);
	}

	@RequestMapping("/helpRequest")
	public void getHelp(@RequestParam("id") int id) {
		ReliefRequest oldRequest = requestRepository.findById(id);
		ReliefRequest request = new ReliefRequest(oldRequest.getStamps().get(0));
		request.setStore(oldRequest.getStore());
		request.setHelpRequest(true);
		request.setType("DV2");
		if (!oldRequest.isBeingHelped() && !oldRequest.isHelpRequest()) {
			oldRequest.setBeingHelped(true);
			requestRepository.save(request);
			requestRepository.save(oldRequest);
		}
	}

	@RequestMapping("/addRequest")
	public void addRequest(@RequestParam("seat") String seatNum,
			@RequestParam("store") int storeNum,
			@RequestParam("C35") boolean c35) {
		boolean preRequested = false;
		List<ReliefRequest> currentList = requestRepository
				.findUnactionedRequests();
		Stamp stamp = new Stamp(seatNum, "REQUEST");
		ReliefRequest request = new ReliefRequest(stamp);

		if (c35) {
			request.setType("C35");
		} else {
			request.setType("DV2");
			request.setStore(storeRepository.findByIdNumber(storeNum));
		}

		for (ReliefRequest reqCheck : currentList) {
			log.info("rec seat" + reqCheck.getStamps().get(0).getSeat());
			log.info("seatnum" + seatNum);

			if (c35) {
				if (seatNum.equals(reqCheck.getStamps().get(0).getSeat())
						&& reqCheck.getType().equals("C35")) {
					preRequested = true;
				}
			} else {
				if (request.getStore() == reqCheck.getStore()) {
					preRequested = true;
				}
			}
		}

		// log.info("Already on:"+preRequested); */

		if (preRequested == false) {
			stampRepository.save(stamp);
			requestRepository.save(request);
		}

	}

	@RequestMapping("/getActiveRequests")
	public List<ReliefRequest> getActiveRequest() {
		// log.info(SecurityContextHolder.getContext().getAuthentication().getName());
		return requestRepository.findUsersActionedItems(SecurityContextHolder
				.getContext().getAuthentication().getName());
	}

	@RequestMapping("/getList")
	public List<ReliefRequest> getTheList() {
		return requestRepository.findUnactionedRequests();
	}

	@RequestMapping("/getRequest")
	public ReliefRequest getRequest(@RequestParam("seat") String seatNum) {
		List<ReliefRequest> requests = requestRepository
				.getOldestActiveBathroomBreak();
		if (requests.isEmpty()) {
			requests = requestRepository.getOldestActiveStoreRequest();
		}
		ReliefRequest request = requests.get(0);
		// log.info(request);
		Stamp stamp = new Stamp(seatNum, "ACTIONED");
		request.addStamp(stamp);
		stampRepository.save(stamp);
		requestRepository.save(request);
		return request;
	}

	@RequestMapping("/storeById")
	public Store storeById(@RequestParam("id") int id) {
		return storeRepository.findByIdNumber(id);
	}

	@RequestMapping("/seatStores")
	public List<Store> storesBySeat(@RequestParam("seat") String seat_num) {
		if (advisoryLevel.getLevel() == "A") {
			return storeRepository.findByaGroup(seat_num);
		} else if (advisoryLevel.getLevel() == "B") {
			return storeRepository.findBybGroup(seat_num);
		} else if (advisoryLevel.getLevel() == "C") {
			return storeRepository.findBycGroup(seat_num);
		} else if (advisoryLevel.getLevel() == "Sat") {
			return storeRepository.findBysatGroup(seat_num);
		} else {
			return null;
		}
	}

	@RequestMapping("/setCutOff")
	public void setCutOff(@RequestParam("number") int num) {
		advisoryLevel.setCutOff(num);
	}

	@RequestMapping("/getGroups")
	public List<Integer> getCurrentGroups() {
		return storeRepository.findAGroups();
	}

	@RequestMapping("/getSeats")
	public List<Seat> getSeats() {
		return seatRepository.findAll();
	}

	@RequestMapping("/accountByUsername")
	public Account accountByUserName(@RequestParam("id") String name) {
		return accountRepository.findByUsername(name);
	}

	@RequestMapping("/saveAccount")
	public void saveAccount(@RequestBody Account account) {
		log.info("yep");
		accountRepository.save(account);
	}
	
	@RequestMapping("/newAccount")
	public void newAccount(@RequestBody Account account) {
		Account newAccount = new Account();
		newAccount.setUsername(account.getUsername());
		newAccount.setPassword(new ShaPasswordEncoder(256).encodePassword(account.getUsername(), null));
		newAccount.setRole(account.getRole());
		newAccount.setFirstName(account.getFirstName());
		newAccount.setLastName(account.getLastName());
		newAccount.setEnabled(true);
		accountRepository.save(newAccount);
	}
	
	@RequestMapping("/resetPassword")
	public void resetPassword(@RequestParam("id") String name){
		Account account = accountRepository.findByUsername(name);
		account.setPassword(new ShaPasswordEncoder(256).encodePassword(account.getUsername(), null));
		accountRepository.save(account);
	}

	@RequestMapping("/lastCheckedList")
	public List<Store> getLastCheckedOrder() {
		return storeRepository.getLastCheckedOrder();
	}

	@RequestMapping("/saveLastChecks")
	public void saveLastChecks(@RequestBody List<Store> stores) {
		for (Store store : stores) {
			store.setLastChecked(new Date());
		}
		storeRepository.save(stores);

	}

	@RequestMapping("/postBulletin")
	public int newBulletin(@RequestBody String note) {
		Bulletin bullet = new Bulletin();
		bullet.setContent(note);
		bullet.setPosted(new Date());
		bullet.setName(accountRepository.findByUsername(
				SecurityContextHolder.getContext().getAuthentication()
						.getName()).getFullName());
		long theFuture = System.currentTimeMillis() + (86400 * 1000)+ 1;
		Date oneDay = new Date(theFuture);
		bullet.setExpires(oneDay);
		bulletinRepository.save(bullet);
		return bullet.getId();

	}

	@RequestMapping("/updateBulletin")
	public void updateBulletin(@RequestParam("id") int id,
			@RequestParam("alert") boolean alert, @RequestParam("life") int life) {
		Bulletin bullet = bulletinRepository.findById(id);
		bullet.setAlert(alert);
		long theFuture = System.currentTimeMillis()
				+ (86400 * 1000 * (long) life);
		log.info(life);
		Date weeks = new Date(theFuture);
		bullet.setExpires(weeks);
		bulletinRepository.save(bullet);
	}

	@RequestMapping("/getBulletins")
	public List<Bulletin> getBulletins() {
		return bulletinRepository.findByExpired(false);
	}

	@RequestMapping("/save")
	public void saveStore(@RequestBody Store store) {
		storeRepository.save(store);

	}

	@RequestMapping("/getJavaDate")
	public Date sendJavaDate() {
		return new Date();
	}

	@RequestMapping("/getAdvisory")
	public AdvisoryLevel getAdvisory() {
		return advisoryLevel;
	}

	@RequestMapping("/changeGroup")
	public void changeGroups(@RequestParam("group") String group) {
		// log.info(group);
		if (group.equals("Sat")) {
			advisoryLevel.changeToSat();
		} else if (group.equals("A")) {
			advisoryLevel.changeToA();
		} else if (group.equals("B")) {
			advisoryLevel.changeToB();
		} else {
			advisoryLevel.changeToC();
		}
		// log.info(advisoryLevel.getLevel());
	}

	@RequestMapping("/getAllStores")
	public List<Store> getAllStores(){
		return storeRepository.findAll();
	}
	
	/*@RequestMapping("/addRPHRequest")
	public void addRPHrequest(@RequestParam("seat") String seatNum) {
		boolean preRequested = false;
		List<ReliefRequest> currentList = requestRepository.findUnactionedRPHRequests();
		Stamp stamp = new Stamp(seatNum, "REQUEST");
		ReliefRequest request = new ReliefRequest(stamp);
		request.setType("RPH");

		for (ReliefRequest reqCheck : currentList) {
				if (seatNum.equals(reqCheck.getStamps().get(0).getSeat())) {
					preRequested = true;
				}
			}
		if(!preRequested){
			stampRepository.save(stamp);
			requestRepository.save(request);
		}
		}
	
	@RequestMapping("/getRPHrequests")
	public List<ReliefRequest> getRPHrequests(){
		return requestRepository.findUnactionedRPHRequests();
	}
	
	@RequestMapping("/getActiveRPHRequests")
	public List<ReliefRequest> getActiveRPHRequest() {
		// log.info(SecurityContextHolder.getContext().getAuthentication().getName());
		return requestRepository.findRPHActionedItems(SecurityContextHolder
				.getContext().getAuthentication().getName());
	}

	@RequestMapping("/takeRPHRequest")
	public ReliefRequest getRPHRequest() {
		List<ReliefRequest> requests = requestRepository.getOldestActiveRPHRequest();
		ReliefRequest request = requests.get(0);
		// log.info(request);
		Stamp stamp = new Stamp("RPH", "ACTIONED");
		request.addStamp(stamp);
		stampRepository.save(stamp);
		requestRepository.save(request);
		return request;
	}
	
	@RequestMapping("/finishRPHRequest")
	public void finishRPHRequest(@RequestParam("id") int id){
		ReliefRequest request = requestRepository.findById(id);
		Stamp stamp = new Stamp("RPH", "FUFILLED");
		request.addStamp(stamp);
		stampRepository.save(stamp);
		requestRepository.save(request);
	}*/
	
}
