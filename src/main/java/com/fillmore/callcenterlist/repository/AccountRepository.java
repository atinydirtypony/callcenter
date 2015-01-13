package com.fillmore.callcenterlist.repository;

import org.springframework.data.repository.CrudRepository;

import com.fillmore.callcenterlist.domain.Account;

public interface AccountRepository extends CrudRepository<Account, String > {
	

}
