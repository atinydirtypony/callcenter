package com.fillmore.callcenterlist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Store;

public interface StoreRepository extends CrudRepository<Store, Integer> {
	
	public List<Store> findByaGroup(int seat);
	
	public Store findByPhoneNumber(String phone);

}
