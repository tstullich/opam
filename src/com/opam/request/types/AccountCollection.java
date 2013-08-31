package com.opam.request.types;

import java.util.ArrayList;

public class AccountCollection {
	
	public AccountCollection(){};

	ArrayList<Account> AccountCollection;
	int count;
	
	@Override
	public String toString() {
		return "what the dicks";
	}

	public ArrayList<Account> getAccountCollection() {
		return AccountCollection;
	}

	public void setAccountCollection(ArrayList<Account> accountCollection) {
		AccountCollection = accountCollection;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
