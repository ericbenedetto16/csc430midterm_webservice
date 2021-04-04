package edu.cuny.csi.csc430.professionalGooglersAPI.api.utils;

public class AuthenticationObject {
	private Boolean success;
	private String sessionToken;
	
	public AuthenticationObject(Boolean success, String sessionToken) {
		this.success = success;
		this.sessionToken = sessionToken;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public String getSessionToken() {
		return sessionToken;
	}
}
