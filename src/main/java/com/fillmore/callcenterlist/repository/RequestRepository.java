package com.fillmore.callcenterlist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.ReliefRequest;

public interface RequestRepository extends CrudRepository<ReliefRequest, Integer> {

}
