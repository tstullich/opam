package com.opam.request.types;

import java.util.ArrayList;

public class ServerStatus {

	public String Requestor;
	ArrayList<String> RequestorGroups;
	
	public ServerStatus(){};
	
	public int getStatusCodeInt() {
		return ServerState.getStatusCode();
	}
	
	public static class ServerState{
		String Status;
		static int StatusCode;
		
		public ServerState(){}

		public String getStatus() {
			return Status;
		}

		public static int getStatusCode() {
			return StatusCode;
		};
	}
}
