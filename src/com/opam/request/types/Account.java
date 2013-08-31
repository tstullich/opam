package com.opam.request.types;

/**
 * Wrapper class in order to facilitate JSON parsing
 * @author Tim
 *
 */
public class Account {
	String uri;
	String accountUID;
	String accountName;
	String description;
	String status;
	String accountlevelstatus;
	boolean shared;
    String targetUID;
	String targetName;
	String targetType;
	String domain;
	
	public Account(){};
	
	@Override
	public String toString() {
		return "What the dicks";
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAccountUID() {
		return accountUID;
	}

	public void setAccountUID(String accountUID) {
		this.accountUID = accountUID;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountlevelstatus() {
		return accountlevelstatus;
	}

	public void setAccountlevelstatus(String accountlevelstatus) {
		this.accountlevelstatus = accountlevelstatus;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getTargetUID() {
		return targetUID;
	}

	public void setTargetUID(String targetUID) {
		this.targetUID = targetUID;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}