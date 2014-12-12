package com.fillmore.callcenterlist.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.ReliefRequest;
import com.fillmore.callcenterlist.domain.Store;

public interface RequestRepository extends CrudRepository<ReliefRequest, Integer> {
	
	public List<ReliefRequest> findBytimeActioned(Date time);
	
	public List<ReliefRequest> findBytimeFufilled(Date time);
	
	public List<ReliefRequest> findBytimeFufilledAndActionSeat(Date timeActioned, String actionSeat);
	
	public ReliefRequest findById(int id_num);
	
	public List<ReliefRequest> findByStore(Store store);

	@Query("from ReliefRequest r where r.timeActioned is null and r.bathroomBreak = true order by r.timeRequested asc")
	public List<ReliefRequest> getOldestActiveBathroomBreak();
	
	@Query("from ReliefRequest r where r.timeActioned is null and r.bathroomBreak = false order by r.timeRequested asc")
	public List<ReliefRequest> getOldestActiveStoreRequest();
	
}
