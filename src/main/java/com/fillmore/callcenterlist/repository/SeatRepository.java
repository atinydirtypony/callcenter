package com.fillmore.callcenterlist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Seat;


public interface SeatRepository extends CrudRepository<Seat, Integer> {

	public List<Seat> findAll();
}
