package com.fillmore.callcenterlist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Bulletin;



public interface BulletinRepository extends CrudRepository<Bulletin, Integer> {

	public Bulletin findById(int id);
	
	public List<Bulletin> findByExpired(boolean expired);
	 
}
