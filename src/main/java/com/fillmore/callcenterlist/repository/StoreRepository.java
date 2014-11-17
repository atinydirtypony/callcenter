package com.fillmore.callcenterlist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Store;

public interface StoreRepository extends CrudRepository<Store, Integer> {
	
	public Store findByIdNumber(int id);
	
	public List<Store> findByaGroup(int seat);
	
	public List<Store> findBybGroup(int seat);
	
	public List<Store> findBycGroup(int seat);
	
	public List<Store> findBysatGroup(int seat);
	

}
