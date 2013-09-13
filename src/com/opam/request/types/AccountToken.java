package com.opam.request.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountToken {
	private AccountTokenWrapper wrapper;
	
	public AccountToken(){};

	@JsonProperty("accountToken")
	public AccountTokenWrapper getWrapper() {
		return wrapper;
	}
	
}
