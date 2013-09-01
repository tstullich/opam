package com.opam.request.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {

	@JsonProperty("account")
	private AccountWrapper account;

	public AccountWrapper getAccount() {
		return account;
	}

	public void setAccount(AccountWrapper account) {
		this.account = account;
	}
	
	public String getName(){
		return account.getAccountName();
	}
}