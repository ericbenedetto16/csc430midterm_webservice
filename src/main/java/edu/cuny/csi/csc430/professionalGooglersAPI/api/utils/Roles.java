package edu.cuny.csi.csc430.professionalGooglersAPI.api.utils;

public enum Roles {
	ADMIN("admin"),
	FACULTY("faculty"),
	PARENT("parent"),
	STUDENT("student");
	
	private String role;
	
	Roles(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
}
