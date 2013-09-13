package com.opam.request.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckInStatus {
	private String force;
	private String userid;
	
	@JsonProperty("force")
	public String getForce() {
		return force;
	}
	@JsonProperty("force")
	public void setForce(String force) {
		this.force = force;
	}
	@JsonProperty("userid")
	public String getUserid() {
		return userid;
	}
	@JsonProperty("userid")
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
