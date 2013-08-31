package com.opam.request.types;

import java.util.ArrayList;

public class AccountCollection {
	private ArrayList<Account> AccountCollection;
	private int count;
	
	public AccountCollection(){};

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