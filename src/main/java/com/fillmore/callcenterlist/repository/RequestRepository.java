package com.fillmore.callcenterlist.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Account;
import com.fillmore.callcenterlist.domain.ReliefRequest;
import com.fillmore.callcenterlist.domain.Store;

public interface RequestRepository extends CrudRepository<ReliefRequest, Integer> {
	
	public ReliefRequest findById(int id_num);
	
	public List<ReliefRequest> findByStore(Store store);

	@Query("Select rr from ReliefRequest rr inner join rr.stamps s " +
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" + 
			" and s.type != 'ACTIONED' and s.type != 'FUFILLED' and rr.type != 'RPH'")
	public List<ReliefRequest> findUnactionedRequests();
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s " +
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" + 
			" and s.type = 'ACTIONED' and s.type != 'FUFILLED' and rr.type != 'RPH'")
	public List<ReliefRequest> findUnfufilledRequests();
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s " +
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" + 
			" and s.type != 'ACTIONED' and s.type != 'FUFILLED' and rr.type = 'RPH'")
	public List<ReliefRequest> findUnactionedRPHRequests();
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s inner join rr.stamps s3 " + 
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id) " +
			" and s.type != 'ACTIONED' and s.type != 'FUFILLED' and rr.type = 'C35' and s3.type = 'REQUEST' order by s3.time asc")
	public List<ReliefRequest> getOldestActiveBathroomBreak();
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s inner join rr.stamps s3 " + 
			"where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" +
			" and s.type != 'ACTIONED' and s.type != 'FUFILLED' and rr.type = 'DV2' and s3.type = 'REQUEST' order by s3.time asc")
	public List<ReliefRequest> getOldestActiveStoreRequest();
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s inner join rr.stamps s3 " + 
			"where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" +
			" and s.type != 'ACTIONED' and s.type != 'FUFILLED' and rr.type = 'RPH' and s3.type = 'REQUEST' order by s3.time asc")
	public List<ReliefRequest> getOldestActiveRPHRequest();
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s " +
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" + 
			" and s.type = 'ACTIONED' and s.id = ?1 and rr.type != 'RPH'")
	public List<ReliefRequest> findUsersActionedItems(String user);
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s " +
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" + 
			" and s.type = 'ACTIONED' and s.id = ?1 and rr.type = 'RPH'")
	public List<ReliefRequest> findRPHActionedItems(String user);
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s where s.stampId = ?1 and rr.id != ?2")
	public ReliefRequest getHelperRequest(Long stampId, Integer RRID);
	
	@Query("Select rr from ReliefRequest rr inner join rr.stamps s " +
			" where s.time = (select max(s2.time) from ReliefRequest rr2 inner join rr2.stamps s2 where rr2.id = rr.id)" + 
			" and s.type != 'FUFILLED' and rr.store = ?1")
	public List<ReliefRequest> findNonfufulledByStore(Store store);
}
