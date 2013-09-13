package com.opam.request.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountTokenWrapper {
	private String accountName;
	private String accountUID;
	private String accountPassword;
	
	public AccountTokenWrapper() {};
	
	@JsonProperty("accountName")
	public String getAccountName() {
		return accountName;
	}
	
	@JsonProperty("accountName")
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@JsonProperty("accountUID")
	public String getAccountUID() {
		return accountUID;
	}
	
	@JsonProperty("accountUID")
	public void setAccountUID(String accountUID) {
		this.accountUID = accountUID;
	}
	
	@JsonProperty("accountPassword")
	public String getAccountPassword() {
		return accountPassword;
	}
	
	@JsonProperty("accountPassword")
	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}
}
