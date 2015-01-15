package com.fillmore.callcenterlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Store;

public interface StoreRepository extends CrudRepository<Store, Integer> {
	
	public Store findByIdNumber(int id);
	
	public List<Store> findByaGroup(String seat);
	
	public List<Store> findBybGroup(String seat);
	
	public List<Store> findBycGroup(String seat);
	
	public List<Store> findBysatGroup(String seat);
	
	@Query("from Store s order by s.lastChecked asc")
	public List<Store> getLastCheckedOrder();
	
	@Query("Select distinct s.aGroup from Store s order by s.aGroup asc")
	public List<Integer> findAGroups();
 	

}
