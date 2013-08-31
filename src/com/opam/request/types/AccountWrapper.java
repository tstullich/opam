package com.opam.request.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class in order to facilitate JSON parsing
 * 
 * @author Tim
 * 
 */
public class AccountWrapper {
	private String uri;
	private String accountUID;
	private String accountName;
	private String description;
	private String status;
	private String accountlevelstatus;
	private boolean shared;
	private String targetUID;
	private String targetName;
	private String targetType;
	private String domain;

	public AccountWrapper() {};

	@JsonProperty("uri")
	public String getUri() {
		return uri;
	}

	@JsonProperty("uri")
	public void setUri(String uri) {
		this.uri = uri;
	}

	@JsonProperty("accountUID")
	public String getAccountUID() {
		return accountUID;
	}

	@JsonProperty("accountUID")
	public void setAccountUID(String accountUID) {
		this.accountUID = accountUID;
	}

	@JsonProperty("accountName")
	public String getAccountName() {
		return accountName;
	}

	@JsonProperty("accountName")
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("accountlevelstatus")
	public String getAccountlevelstatus() {
		return accountlevelstatus;
	}

	@JsonProperty("accountlevelstatus")
	public void setAccountlevelstatus(String accountlevelstatus) {
		this.accountlevelstatus = accountlevelstatus;
	}

	@JsonProperty("shared")
	public boolean isShared() {
		return shared;
	}

	@JsonProperty("shared")
	public void setShared(boolean shared) {
		this.shared = shared;
	}

	@JsonProperty("targetUID")
	public String getTargetUID() {
		return targetUID;
	}

	@JsonProperty("targetUID")
	public void setTargetUID(String targetUID) {
		this.targetUID = targetUID;
	}

	@JsonProperty("targetName")
	public String getTargetName() {
		return targetName;
	}

	@JsonProperty("targetName")
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@JsonProperty("targetType")
	public String getTargetType() {
		return targetType;
	}

	@JsonProperty("targetType")
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	@JsonProperty("domain")
	public String getDomain() {
		return domain;
	}

	@JsonProperty("domain")
	public void setDomain(String domain) {
		this.domain = domain;
	}
}