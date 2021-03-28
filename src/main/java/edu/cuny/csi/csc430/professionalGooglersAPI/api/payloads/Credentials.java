package edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Credentials {
	private String username;
	private String password;
	
	@JsonCreator
	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUserName() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
