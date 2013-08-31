package com.opam.request.types;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountsCollection {
	
	private ArrayList<Account> accountCollection;
	private int count;

	public AccountsCollection() {
	};

	@JsonProperty("AccountCollection")
	public ArrayList<Account> getAccountCollection() {
		return accountCollection;
	}

	@JsonProperty("AccountCollection")
	public void setAccountCollection(ArrayList<Account> accountCollection) {
		this.accountCollection = accountCollection;
	}

	@JsonProperty("count")
	public int getCount() {
		return count;
	}

	@JsonProperty("count")
	public void setCount(int count) {
		this.count = count;
	}
}