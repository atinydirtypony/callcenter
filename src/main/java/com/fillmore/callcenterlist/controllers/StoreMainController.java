package com.fillmore.callcenterlist.controllers;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fillmore.callcenterlist.domain.Store;
import com.fillmore.callcenterlist.repository.AccountRepository;
import com.fillmore.callcenterlist.repository.BulletinRepository;
import com.fillmore.callcenterlist.repository.RequestRepository;
import com.fillmore.callcenterlist.repository.SeatRepository;
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
		request.setRequestingUser(SecurityContextHolder.getContext().getAuthentication().getName());
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
	
	@RequestMapping("/helpRequest")
	public void getHelp(@RequestParam("id") int id){
		ReliefRequest oldRequest = requestRepository.findById(id);
		ReliefRequest request = new ReliefRequest();
		request.setTimeRequested(oldRequest.getTimeRequested());
		request.setStore(oldRequest.getStore());
		request.setHelpRequest(true);
		request.setOriginalId(oldRequest.getId());
		if(!oldRequest.isBeingHelped()){
			oldRequest.setBeingHelped(true);
			requestRepository.save(request);
		}
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
		//log.info(request);
		request.setActionSeat(seat_num);
		request.setActioningUser(SecurityContextHolder.getContext().getAuthentication().getName());
		request.setTimeActioned(new Date());
		requestRepository.save(request);
		return request;
	}
	

	@RequestMapping("/getActiveRequests")
	public List<ReliefRequest> getActiveRequest() {
		log.info(SecurityContextHolder.getContext().getAuthentication().getName());
		return requestRepository.findBytimeFufilledAndActioningUser(null, SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	@RequestMapping("/finishRequest")
	public void finishRequest(@RequestParam("id") int id_num, @RequestParam("seat") String seat_num){
		ReliefRequest request = requestRepository.findById(id_num);
		request.setFufillmentSeat(seat_num);
		request.setFufillingUser(SecurityContextHolder.getContext().getAuthentication().getName());
		request.setTimeFufilled(new Date());
		request.getStore().setLastChecked(new Date());
		if(!request.isBathroomBreak()){
			advisoryLevel.addLapTime(request.getTimeFufilled().getTime() - request.getTimeRequested().getTime());
		}
		requestRepository.save(request);
		if(!seat_num.equals("N/F")){
			ReliefRequest secondary = null;
			if(request.isHelpRequest()){
				secondary = requestRepository.findById(request.getOriginalId());
			}else if(request.isBeingHelped()){
				secondary = requestRepository.findByOriginalId(request.getId());
			}
			//log.info(request.isHelpRequest());
			//log.info(request.isBeingHelped());
			//log.info(secondary);
			if(secondary != null){
				//log.info("HI!!!");
				secondary.setTimeFufilled(new Date());
				secondary.setFufillingUser(secondary.getActioningUser());
				secondary.setFufillmentSeat(secondary.getActionSeat());
				if(secondary.getTimeActioned()==null){
					secondary.setTimeActioned(new Date());
				}
				requestRepository.save(secondary);
			}
		}
	}
	
	
	@RequestMapping("/putBackRequest")
	public void otherRequest(@RequestParam("requestSeat") String reqSeat, @RequestParam("id") int idNum){
		ReliefRequest request = new ReliefRequest();
		ReliefRequest oldRequest = requestRepository.findById(idNum);
		request.setSeatNum(reqSeat);
		request.setRequestingUser(SecurityContextHolder.getContext().getAuthentication().getName());
		request.setBathroomBreak(oldRequest.isBathroomBreak());
		request.setStore(oldRequest.getStore());
		request.setTimeRequested(oldRequest.getTimeRequested());
		request.setBeingHelped(oldRequest.isBeingHelped());
		request.setHelpRequest(oldRequest.isHelpRequest());
		requestRepository.save(request);
		this.finishRequest(idNum, "N/F");
	}
	
	@RequestMapping("/calledRequest")
	public void calledRequest(@RequestParam("store") int storeNum, @RequestParam("seat") String seat){
		ReliefRequest request = new ReliefRequest();
		request.setActionSeat(seat);
		request.setActioningUser(SecurityContextHolder.getContext().getAuthentication().getName());
		request.setSeatNum("CALL");
		request.setRequestingUser("CALL");
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
	
	@RequestMapping("/getAdvisory")
	public AdvisoryLevel getAdvisory(){
		return advisoryLevel;
	}
	
	@RequestMapping("/changeGroup")
	public void changeGroups(@RequestParam("group") String group){
		//log.info(group);
		if( group.equals("Sat")){
			advisoryLevel.changeToSat();
		}else if(group.equals("A")){
			advisoryLevel.changeToA();
		}else if(group.equals("B")){
			advisoryLevel.changeToB();
		}else{
			advisoryLevel.changeToC();
		}
		//log.info(advisoryLevel.getLevel());
	}
	
	@RequestMapping("/setCutOff")
	public void setCutOff(@RequestParam("number") int num){
		advisoryLevel.setCutOff(num);
	}
	
	@RequestMapping("/getGroups")
	public List<Integer> getCurrentGroups(){
		return storeRepository.findAGroups();
	}
	
	@RequestMapping("/getSeats")
	public List<Seat> getSeats(){
		return seatRepository.findAll();
	}
	
	@RequestMapping("/accountByUsername")
	public Account accountByUserName(@RequestParam("id") String name){
		return accountRepository.findByUsername(name);
	}
	
	@RequestMapping("/saveAccount")
	public void saveAccount(@RequestBody Account account){
		accountRepository.save(account);
	}
	
	@RequestMapping("/lastCheckedList")
	public List<Store> getLastCheckedOrder(){
		return storeRepository.getLastCheckedOrder();
	}
	
	@RequestMapping("/saveLastChecks")
	public void saveLastChecks(@RequestBody List<Store> stores) {
		for(Store store: stores){
			store.setLastChecked(new Date());
		}
		storeRepository.save(stores);

	}
	
	@RequestMapping("/postBulletin")
	public int newBulletin(@RequestBody String note){
		Bulletin bullet = new Bulletin();
		bullet.setContent(note);
		bullet.setPosted(new Date());
		bullet.setName(
				accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getFullName()
				);
		long theFuture = System.currentTimeMillis() + (86400 * 7 * 1000*2);
		Date twoWeeks = new Date(theFuture);
		bullet.setExpires(twoWeeks);
		bulletinRepository.save(bullet);
		return bullet.getId();
		
	}
	
	@RequestMapping("/updateBulletin")
	public void updateBulletin(@RequestParam("id")int id, @RequestParam("alert") boolean alert, @RequestParam("life") int life){
		Bulletin bullet = bulletinRepository.findById(id);
		bullet.setAlert(alert);
		long theFuture = System.currentTimeMillis() + (86400 * 7 * 1000 * (long) life);
		log.info(life);
		Date weeks = new Date(theFuture);
		bullet.setExpires(weeks);
		bulletinRepository.save(bullet);
	}
	
	@RequestMapping("/getBulletins")
	public List<Bulletin> getBulletins(){
		return bulletinRepository.findByExpired(false);
	}
	
}
