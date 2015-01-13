package com.fillmore.callcenterlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fillmore.callcenterlist.repository.AccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService{
	
	@Autowired
	private AccountRepository accountRepository;

	public UserDetails loadUserByUsername(String lookUpName)
			throws UsernameNotFoundException {
		return accountRepository.findOne(lookUpName);
	}

}
