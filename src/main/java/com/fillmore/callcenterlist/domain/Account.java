package com.fillmore.callcenterlist.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fillmore.callcenterlist.controllers.StoreMainController;



@Entity
public class Account implements UserDetails {

	private static final Log log = LogFactory.getLog(StoreMainController.class);
	
	@Id
	private String username;
	
	private String password;
	private boolean enabled;
	private String firstName;
	private String lastName;
	private boolean callsCert;
	private boolean dataEntryCert;
	private boolean reportCert;
	private String role;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isCallsCert() {
		return callsCert;
	}
	public void setCallsCert(boolean callsCert) {
		this.callsCert = callsCert;
	}
	public boolean isDataEntryCert() {
		return dataEntryCert;
	}
	public void setDataEntryCert(boolean dataEntryCert) {
		this.dataEntryCert = dataEntryCert;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isReportCert() {
		return reportCert;
	}
	public void setReportCert(boolean reportCert) {
		this.reportCert = reportCert;
	}
	
	public boolean getCert(String type) {
		//log.info(type);
		if(!this.role.equals("USER")){
			return true;
		}else{
			if(type.equals( "Calls")){
				//log.info(this.isCallsCert());
				return this.isCallsCert();
			}else if(type.equals("Reports")){
				//log.info(this.isReportCert());
				return this.isReportCert();
			}else if(type.equals("DataEntry")){
				//log.info(this.isDataEntryCert());
				return this.isDataEntryCert();
			}else{
				return false;
			}
			
		}
	}
	
	public String getFullName(){
		return this.firstName.concat(" ").concat(this.lastName);
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
